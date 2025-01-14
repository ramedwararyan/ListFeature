package com.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.entities.ScheduledOrders;

@Repository
public interface ScheduledOrdersRepository extends JpaRepository<ScheduledOrders, Long> {

}
