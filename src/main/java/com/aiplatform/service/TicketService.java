package com.aiplatform.service;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aiplatform.aiClient.AIClient;
import com.aiplatform.hibernate.Ticket;
import com.aiplatform.model.TicketResponse;
import com.aiplatform.repository.TicketRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class TicketService {

	private final AIClient aiClient;
	private final ObjectMapper objectMapper;
	private final TicketRepository ticketRepository;

	public TicketService(AIClient aiClient, ObjectMapper objectMapper, TicketRepository ticketRepository) {
		this.aiClient = aiClient;
		this.objectMapper = objectMapper;
		this.ticketRepository = ticketRepository;
	}

	@Transactional
	//@Cacheable(value = "ticketAnalysis", key = "#description")
	public TicketResponse createAndAnalyzeTicket(String description) {

		// Create Ticket
		Ticket ticket = new Ticket();
		ticket.setDescription(description);
		ticket.setStatus("NEW");
		ticket.setCreatedAt(LocalDateTime.now());

		ticket = ticketRepository.save(ticket);

		// 2️⃣ Call AI

		String prompt = buildPrompt(description);

		// call api
		String rawResponse = aiClient.analyze(prompt);
		String extractedJson = extractContent(rawResponse);

		TicketResponse aiResponse;

		try {
			aiResponse = objectMapper.readValue(extractedJson, TicketResponse.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse AI response");
		}

		// Update Ticket
		ticket.setCategory(aiResponse.getCategory());
		ticket.setSentiment(aiResponse.getSentiment());
		ticket.setPriority(aiResponse.getPriority());
		ticket.setStatus("ANALYZED");

		ticketRepository.save(ticket);

		return mapToResponse(ticket);


	}
	
	
	
	public List<TicketResponse> getAllTickets() {
	    return ticketRepository.findAll()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}

	public TicketResponse getTicketById(Long id) {
	    Ticket ticket = ticketRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Ticket not found"));

	    return mapToResponse(ticket);
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

	private TicketResponse mapToResponse(Ticket ticket) {
		TicketResponse response = new TicketResponse();

		response.setId(ticket.getId());
		response.setDescription(ticket.getDescription());
		response.setCategory(ticket.getCategory());
		response.setSentiment(ticket.getSentiment());
		response.setPriority(ticket.getPriority());
		response.setStatus(ticket.getStatus());
		response.setCreatedAt(ticket.getCreatedAt());

		return response;
	}
}
