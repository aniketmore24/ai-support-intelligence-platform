package com.aiplatform.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ticket response")
public class TicketResponse  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		@Schema(example = "1")
	 	private Long id;
		
		@Schema(example = "Internet not working")
	    private String description;
	    
	    @Schema(example = "Internet Connectivity")
	    private String category;
	    
	    @Schema(example = "Negative")
	    private String sentiment;
	    
	    @Schema(example = "High")
	    private String priority;
	    
	    @Schema(example = "Negative")
	    private String status;
	    private LocalDateTime createdAt;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getSentiment() {
			return sentiment;
		}
		public void setSentiment(String sentiment) {
			this.sentiment = sentiment;
		}
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
    
    

}
