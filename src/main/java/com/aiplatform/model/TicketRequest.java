package com.aiplatform.model;

import org.antlr.v4.runtime.misc.NotNull;


public class TicketRequest {

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
