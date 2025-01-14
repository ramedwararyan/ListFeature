package com.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String productName;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private int productQuantity;

	@ManyToOne
	@JoinColumn(name = "order_request_id")
	private OrderRequest orderRequest;

	@ManyToOne
	@JoinColumn(name = "scheduled_order_request_id")
	private ScheduledOrders scheduledOrderRequest;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public OrderRequest getOrderRequest() {
		return orderRequest;
	}

	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}

	public ScheduledOrders getScheduledOrderRequest() {
		return scheduledOrderRequest;
	}

	public void setScheduledOrderRequest(ScheduledOrders scheduledOrderRequest) {
		this.scheduledOrderRequest = scheduledOrderRequest;
	}

}
