package com.aiplatform.Controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiplatform.model.TicketRequest;
import com.aiplatform.model.TicketResponse;
import com.aiplatform.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Ticket API", description = "Operations related to support tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Create a new ticket and trigger AI analysis")
    @PostMapping("/ticket")
    public TicketResponse analyze(@RequestBody TicketRequest request) {
        return ticketService.createAndAnalyzeTicket(request.getDescription());
    }
    
    @Operation(summary = "Get all tickets with pagination")
    @GetMapping("/ticket")
    public Page<TicketResponse> get(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ticketService.getAllTickets(page, size);
    }
    
    @Operation(summary = "Filter tickets by priority")
    @GetMapping("/ticket/filter")
    public Page<TicketResponse> getByPriority(
            @RequestParam String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ticketService.getTicketsByPriority(
                priority, page, size);
    }
    
    @Operation(summary = "Get Ticket by Id")
    @GetMapping("/ticket/{id}")
    public TicketResponse getById(Long id) {
        return ticketService.getTicketById(id);
    }
}
