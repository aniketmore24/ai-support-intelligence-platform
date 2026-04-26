package com.aiplatform.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.aiplatform.event.TicketCreatedEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTicketCreated(TicketCreatedEvent event) {

        kafkaTemplate.send("ticket-created", event);

        System.out.println("Event published for ticket: " + event.getTicketId());
    }
}
