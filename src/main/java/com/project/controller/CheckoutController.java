package com.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.entities.Checkout;
import com.project.services.CheckoutService;

@RestController
@RequestMapping("/api")
public class CheckoutController {
	private final CheckoutService checkoutService;

	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = checkoutService;
	}

	@PostMapping("/checkout")
	public ResponseEntity<String> handleCheckout(@RequestBody Checkout checkout) {
		try {
			checkoutService.saveCheckout(checkout);
			return ResponseEntity.ok("Order placed successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to place the order.");
		}
	}

}
