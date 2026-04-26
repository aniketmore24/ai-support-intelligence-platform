package com.aiplatform.event;

public class TicketCreatedEvent {

    private Long ticketId;
    private String description;

    public TicketCreatedEvent() {
    }

    public TicketCreatedEvent(Long ticketId, String description) {
        this.ticketId = ticketId;
        this.description = description;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
