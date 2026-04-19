package com.aiplatform.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiplatform.model.TicketAnalysisResponse;
import com.aiplatform.model.TicketRequest;
import com.aiplatform.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/analyze")
    public TicketAnalysisResponse analyze(@RequestBody TicketRequest request) {
        return ticketService.analyzeTicket(request.getDescription());
    }
}
