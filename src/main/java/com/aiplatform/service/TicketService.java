package com.aiplatform.service;



import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.aiplatform.aiClient.AIClient;
import com.aiplatform.model.TicketAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TicketService {

    private final AIClient aiClient;
    private final ObjectMapper objectMapper;

    public TicketService(AIClient aiClient, ObjectMapper objectMapper) {
        this.aiClient = aiClient;
        this.objectMapper = objectMapper;
    }

    //@Cacheable(value = "ticketAnalysis", key = "#description")
    public TicketAnalysisResponse analyzeTicket(String description) {

        String prompt = buildPrompt(description);

        // call api
        String rawResponse = aiClient.analyze(prompt);
        String extractedJson = extractContent(rawResponse);
        try {
            return objectMapper.readValue(extractedJson, TicketAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response");
        }
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
