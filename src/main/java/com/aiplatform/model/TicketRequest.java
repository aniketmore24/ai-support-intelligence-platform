package com.aiplatform.model;

import org.antlr.v4.runtime.misc.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ticket creation request")
public class TicketRequest {

	@Schema(description = "Ticket description", example = "Internet not working")
    @SuppressWarnings("deprecation")
	@NotNull
    private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    

}
