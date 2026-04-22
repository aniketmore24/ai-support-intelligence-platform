package com.aiplatform.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TicketResponse  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 	private Long id;
	    private String description;
	    private String category;
	    private String sentiment;
	    private String priority;
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
