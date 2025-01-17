package com.project.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entities.Item;
import com.project.entities.OrderRequest;
import com.project.entities.ScheduledOrders;
import com.project.repo.OrderRequestRepository;
import com.project.repo.ScheduledOrdersRepository;

@Service
public class OrderService {

	@Autowired
	private ScheduledOrdersRepository scheduledOrdersRepository;

	@Autowired
	private OrderRequestRepository orderRequestRepository;

	public void saveExecutionDetails(String orderId) {

		ScheduledOrders scheduledOrder = new ScheduledOrders();

		 OrderRequest orderRequest = orderRequestRepository.findByOrderId(orderId)
	                .orElseThrow(() -> new RuntimeException("OrderRequest not found"));

		// Update order details
		scheduledOrder.setOrderId(orderId);
		
		scheduledOrder.setExecutionTime(LocalDateTime.now());
		scheduledOrder.setOrderRequest(orderRequest); // Set the relationship

		System.out.println("Order is placed");
		// Save the updated order
		scheduledOrdersRepository.save(scheduledOrder);

		System.out.println("Updated execution time for order ID: " + orderId);
	}

	public void saveOrderDetails(String orderId, String frequency, String pickupDate, String orderTime,
			int totalItems, double totalPrice, LocalDateTime scheduledTime, Integer intervalDays, Integer intervalTime,
			List<Item> items) {

		OrderRequest scheduledOrders = new OrderRequest();

		// Update order details
		scheduledOrders.setOrderId(orderId);
		scheduledOrders.setScheduled(true);
		scheduledOrders.setFrequency(frequency);
		scheduledOrders.setPickupDate(pickupDate);
		scheduledOrders.setOrderTime(orderTime);
		scheduledOrders.setTotalItems(totalItems);
		scheduledOrders.setTotalPrice(totalPrice);
		scheduledOrders.setExecutionTime(LocalDateTime.now());
		scheduledOrders.setIntervalDays(intervalDays);
		scheduledOrders.setIntervalTime(intervalTime);

		// Re-attach and set items to the new order
		for (Item item : items) {
			// Ensure it's treated as a new entity
			item.setId(null); // Ensure it's treated as a new entity
			item.setOrderRequest(scheduledOrders);
			scheduledOrders.setItems(items);// Associate item with the new order
		}

		// Save the updated order
		orderRequestRepository.save(scheduledOrders);
	}
}
