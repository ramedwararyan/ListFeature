package com.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.OrderRequest;

@Repository
public interface ItemRepository extends JpaRepository<OrderRequest, Long>{

}
