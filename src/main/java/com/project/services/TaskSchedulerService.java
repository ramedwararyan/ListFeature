package com.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entities.OrderRequest;
import com.project.repo.OrderRequestRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Service class to handle task scheduling for orders. It uses a
 * ScheduledExecutorService to schedule tasks based on the order's frequency and
 * timing.
 */
@Service
public class TaskSchedulerService {

	@Autowired
	private OrderRequestRepository orderRequestRepository;

	// Thread pool for scheduling tasks
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
	// Map to keep track of scheduled tasks and their corresponding order IDs
	private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	// Method to schedule tasks
	public void scheduleTask(String orderId, Runnable task, LocalDateTime pickupDateTime, String frequency,
			Integer integer, Integer integer2) {
		try {
			// Calculate the initial delay for the task
			long initialDelay = calculateInitialDelay(pickupDateTime);

			ScheduledFuture<?> futureTask;
			// Schedule the task based on the specified frequency
			String normalizedFrequency = frequency.toLowerCase(); // Normalize input once for efficiency
switch (normalizedFrequency) {
    case "daily":
        futureTask = scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        break;
    case "weekly":
        futureTask = scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toSeconds(7), TimeUnit.SECONDS);
        break;
    case "monthly":
        futureTask = scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toSeconds(30), TimeUnit.SECONDS);
        break;
    case "n/a":
        if (integer != null) {
            futureTask = scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toSeconds(integer), TimeUnit.SECONDS);
        } else if (integer2 != null) {
            futureTask = scheduler.scheduleAtFixedRate(task, initialDelay, integer2, TimeUnit.SECONDS);
        } else {
            futureTask = scheduler.schedule(task, initialDelay, TimeUnit.SECONDS); // One-time task
        }
        break;
    default:
        futureTask = scheduler.schedule(task, initialDelay, TimeUnit.SECONDS); // Default one-time task
        break;
}

			// Store the scheduled task for future reference
			scheduledTasks.put(orderId, futureTask);
			System.out.println("Task scheduled for order ID: " + orderId);
		} catch (Exception e) {
			System.err.println("Error while scheduling order: " + e.getMessage());
		}
	}

	/**
	 * Method to stop a scheduled task for a given order ID.
	 *
	 * @param orderId The unique ID of the order whose task is to be stopped.
	 * @return True if the task was successfully stopped, false otherwise.
	 */
	public boolean stopTask(String orderId) {
		ScheduledFuture<?> futureTask = scheduledTasks.get(orderId);
		if (futureTask != null) {
			// Cancel the task and remove it from the map
			futureTask.cancel(true);
			scheduledTasks.remove(orderId);

			// Update the order's scheduled status in the database
			Optional<OrderRequest> existingOrderRequest = orderRequestRepository.findByOrderId(orderId);
			if (existingOrderRequest.isPresent()) {
				OrderRequest orderRequest = existingOrderRequest.get();
				orderRequest.setScheduled(false); // Update only the scheduled field
				orderRequestRepository.save(orderRequest); // Save the updated entity
				System.out.println("Task stopped for order ID: " + orderId);
				return true;
			} else {
				System.out.println("Order ID not found in the database: " + orderId);
				return false;
			}
		}
		System.out.println("No task found for order ID: " + orderId);
		return false;
	}

	// Calculate initial delay
	private long calculateInitialDelay(LocalDateTime pickupDateTime) {
		LocalDateTime now = LocalDateTime.now();
		return now.isBefore(pickupDateTime) ? java.time.Duration.between(now, pickupDateTime).getSeconds() : 0;
	}

}
