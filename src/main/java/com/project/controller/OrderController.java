package com.project.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.entities.Item;
import com.project.entities.OrderRequest;
import com.project.services.OrderService;
import com.project.services.TaskSchedulerService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private TaskSchedulerService taskSchedulerService;

	@Autowired
	private OrderService orderService;
	

	 /**
     * API endpoint to place an order.
     * Accepts an OrderRequest as input, processes it, and schedules the order based on the specified frequency and timing.
     *
     * @param orderRequest The order details provided in the request body.
     * @return A ResponseEntity with a success message if the order is placed and scheduled successfully.
     */
	@PostMapping
	public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		String orderId = generateID();
		System.out.println("Received order: " + orderRequest);

		// Associate items with the orderRequest and print each item
		for (Item item : orderRequest.getItems()) {
			item.setOrderRequest(orderRequest); // Establish the relationship
			System.out.println("Item Details:");
			System.out.println("  Product Name: " + item.getProductName());
			System.out.println("  Product Quantity: " + item.getProductQuantity());
			System.out.println("  Price: " + item.getPrice());
		}

		orderService.saveOrderDetails(orderId, orderRequest.getFrequency(), orderRequest.getPickupDate(),
				orderRequest.getOrderTime(), orderRequest.getTotalItems(), orderRequest.getTotalPrice(),
				LocalDateTime.now(), orderRequest.getIntervalDays(), orderRequest.getIntervalTime(),
				orderRequest.getItems());

		System.out.println("Order is placed");

		// Define a task to execute the order
		Runnable task = () -> {
			try {
				System.out.println("Executing task for order ID: " + orderId + " at " + LocalDateTime.now());
				
				orderService.saveExecutionDetails(orderId);
			} catch (Exception e) {
				System.err.println("Error during task execution: " + e.getMessage());
			}
		};

		// Parse pickup date and time
		LocalDateTime pickupDateTime = LocalDateTime
				.parse(orderRequest.getPickupDate() + "T" + orderRequest.getOrderTime());

		// Schedule the task
		taskSchedulerService.scheduleTask(orderId, task, pickupDateTime, orderRequest.getFrequency(),
				orderRequest.getIntervalDays(), orderRequest.getIntervalTime());

		return ResponseEntity.ok("Order received and scheduled successfully!");
	}

	/**
     * Utility method to generate a unique order ID.
     * Combines the current timestamp with a random number for uniqueness.
     *
     * @return A unique order ID as a String.
     */
	public static String generateID() {
		long timestamp = System.currentTimeMillis(); // Get the current timestamp
		int randomNum = new Random().nextInt(1000); // Generate a random number between 0 and 999
		return timestamp + "_" + randomNum; // Concatenate timestamp and random number
	}

}
