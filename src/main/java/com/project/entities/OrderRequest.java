package com.project.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_requests")
public class OrderRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Ensures unique ID for each record
	private Long id;

	private String orderId;

	@Column(nullable = false)
	private String pickupDate;

	@Column(nullable = false)
	private String orderTime;

	private int totalItems;

	private double totalPrice;

	@Column(nullable = false)
	private String frequency;

	private Integer intervalDays;

	private Integer intervalTime;

	private LocalDateTime executionTime;

	@OneToMany(mappedBy = "orderRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Item> items = new ArrayList<>();
	
	@OneToMany(mappedBy = "orderRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledOrders> scheduledOrders = new ArrayList<>();


	private boolean isScheduled;

	public String getStatus() {
		return isScheduled ? "Running" : "Stopped";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(Integer intervalDays) {
		this.intervalDays = intervalDays;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	public LocalDateTime getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(LocalDateTime executionTime) {
		this.executionTime = executionTime;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<ScheduledOrders> getScheduledOrders() {
		return scheduledOrders;
	}

	public void setScheduledOrders(List<ScheduledOrders> scheduledOrders) {
		this.scheduledOrders = scheduledOrders;
	}


	
}
