package com.aiplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aiplatform.hibernate.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> ,JpaSpecificationExecutor<Ticket>{
	
	Page<Ticket> findByPriority(
	        String priority,
	        Pageable pageable);

}
