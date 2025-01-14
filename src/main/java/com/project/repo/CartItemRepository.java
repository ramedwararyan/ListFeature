package com.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
