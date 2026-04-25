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

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/ticket")
    public TicketResponse analyze(@RequestBody TicketRequest request) {
        return ticketService.createAndAnalyzeTicket(request.getDescription());
    }
    
    @GetMapping("/ticket")
    public Page<TicketResponse> get(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ticketService.getAllTickets(page, size);
    }
    
    @GetMapping("/tickets/filter")
    public Page<TicketResponse> getByPriority(
            @RequestParam String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ticketService.getTicketsByPriority(
                priority, page, size);
    }
    
    @GetMapping("/ticket/{id}")
    public TicketResponse getById(Long id) {
        return ticketService.getTicketById(id);
    }
}
