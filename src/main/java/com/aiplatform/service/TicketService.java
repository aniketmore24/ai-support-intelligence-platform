package com.aiplatform.service;



import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aiplatform.aiClient.AIClient;
import com.aiplatform.event.TicketCreatedEvent;
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
	private final KafkaProducerService kafkaProducerService;

	public TicketService(AIClient aiClient, ObjectMapper objectMapper, TicketRepository ticketRepository, KafkaProducerService kafkaProducerService) {
		this.aiClient = aiClient;
		this.objectMapper = objectMapper;
		this.ticketRepository = ticketRepository;
		this.kafkaProducerService = kafkaProducerService;
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
		
		TicketCreatedEvent event =
	            new TicketCreatedEvent(
	                    ticket.getId(),
	                    ticket.getDescription()
	            );

	    kafkaProducerService.publishTicketCreated(event);

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
