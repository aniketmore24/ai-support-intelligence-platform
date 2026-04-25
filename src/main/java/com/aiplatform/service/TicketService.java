package com.aiplatform.service;



import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



	@SuppressWarnings("unchecked")
	public Page<TicketResponse> getAllTickets(
			int page,
			int size) {

		Pageable pageable = PageRequest.of(page,  size, Sort.by("createdAt").descending());
		return ticketRepository.findAll(pageable)
				.map(this::mapToResponse);
	}

	public Page<TicketResponse> getTicketsByPriority(
			String priority,
			int page,
			int size) {

		Pageable pageable = PageRequest.of(page, size);

		return ticketRepository
				.findByPriority(priority, pageable)
				.map(this::mapToResponse);
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
