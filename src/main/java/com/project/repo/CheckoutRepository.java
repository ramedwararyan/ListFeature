package com.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.Checkout;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
}



