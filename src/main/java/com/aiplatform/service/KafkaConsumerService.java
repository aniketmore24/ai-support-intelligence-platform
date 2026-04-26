package com.aiplatform.service;



import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.aiplatform.aiClient.AIClient;
import com.aiplatform.aiClient.OpenAIClient;
import com.aiplatform.event.TicketCreatedEvent;
import com.aiplatform.hibernate.Ticket;
import com.aiplatform.model.TicketResponse;
import com.aiplatform.repository.TicketRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {

	private final OpenAIClient aiClient;
	private final TicketRepository ticketRepository;
	private final ObjectMapper objectMapper;

	public KafkaConsumerService(
			OpenAIClient aiClient,
			TicketRepository ticketRepository,
			ObjectMapper objectMapper) {
		this.aiClient = aiClient;
		this.ticketRepository = ticketRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(
			topics = "ticket-created",
			groupId = "ticket-group"
			)
	public void consume(TicketCreatedEvent event) {

		System.out.println("Received ticket event:");
		System.out.println("Ticket ID: " + event.getTicketId());
		System.out.println("Description: " + event.getDescription());

		//  Call AI

		String prompt = buildPrompt(event.getDescription());

		// call api
		String rawResponse = aiClient.analyze(prompt);
		String extractedJson = extractContent(rawResponse);

		TicketResponse aiResponse;

		try {
			aiResponse = objectMapper.readValue(extractedJson, TicketResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse AI response");
		}

		Ticket ticket = ticketRepository
				.findById(event.getTicketId())
				.orElseThrow();
				// Update Ticket
				ticket.setCategory(aiResponse.getCategory());
				ticket.setSentiment(aiResponse.getSentiment());
				ticket.setPriority(aiResponse.getPriority());
				ticket.setStatus("ANALYZED");

				ticketRepository.save(ticket);
				
	}
	
	private String extractContent(String rawResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(rawResponse);
			return root.path("choices")
					.get(0)
					.path("message")
					.path("content")
					.asText();
		} catch (Exception e) {
			throw new RuntimeException("Failed to extract AI response");
		}
	}


	private String buildPrompt(String description) {

		return """
				Analyze the following support ticket.

				Classify into:

				category: Billing | Internet Connectivity | Technical | Account | General
				sentiment: Positive | Neutral | Negative
				priority: Low | Medium | High

				Ticket:
				""" + description;
	}
}
