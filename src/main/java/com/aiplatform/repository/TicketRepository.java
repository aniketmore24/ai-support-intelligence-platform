package com.aiplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aiplatform.hibernate.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
