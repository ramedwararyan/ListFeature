package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.entities.OrderRequest;
import com.project.repo.OrderRequestRepository;
import com.project.services.ProductsSevice;
import com.project.services.TaskSchedulerService;

@Controller
public class ProductsController {

	@Autowired
	private ProductsSevice productsService;

	@Autowired
	private OrderRequestRepository orderRequestRepository;

	@Autowired
	private TaskSchedulerService taskSchedulerService;

	@GetMapping("/")
	public String getPosts(Model model) {
		model.addAttribute("products", productsService.getAllPosts());
		model.addAttribute("dailyproduct", productsService.getAllPosts1());
		return "index"; // The name of the HTML file to render
	}

	@GetMapping("/all-orders")
	public String getAllOrdersForView(Model model) {
		List<OrderRequest> orders = orderRequestRepository.findAll();
		model.addAttribute("orders", orders);
		return "index2"; // The name of your Thymeleaf template
	}

	@PostMapping("/stop-order/{orderId}")
	public String stopOrderExecution(@PathVariable String orderId, Model model) {
		boolean stopped = taskSchedulerService.stopTask(orderId);
		System.out.println("Order is stopped");
		if (stopped) {
			model.addAttribute("message", "Order execution stopped for order ID: " + orderId);
		} else {
			model.addAttribute("message", "No scheduled task found for order ID: " + orderId);
		}
		return "redirect:/all-orders";// Redirect back to the order list
	}

}
