package com.aiplatform.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<TicketResponse> get() {
        return ticketService.getAllTickets();
    }
    
    @GetMapping("/ticket/{id}")
    public TicketResponse getById(Long id) {
        return ticketService.getTicketById(id);
    }
}
