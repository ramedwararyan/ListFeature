package com.project.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.OrderRequest;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {
	Optional<OrderRequest> findByOrderId(String orderId);
   
}
