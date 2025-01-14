package com.project.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.entities.Products;

@Service
public class ProductsSevice {

	public List<Products> getAllPosts() {
		return Arrays.asList(new Products(1, "Apple", "apple.webp", "10", "1"),
				new Products(2, "Banana", "banana.jpg", "10", "1"), 
				new Products(3, "Orange", "orange.webp", "10", "1"),
				new Products(4, "Pineapple", "Pineapple.webp", "10", "1"),
				new Products(5, "Onions", "onions.webp", "10", "1"),
				new Products(6, "Tomato", "tomato.webp", "10", "1"),
				new Products(7, "Ginger", "ginger.webp", "10", "1"), 
				new Products(8, "Potato", "potato.webp", "10", "2")

		);
	}

	public List<Products> getAllPosts1() {
		return Arrays.asList(new Products(1, "Toothbrush", "toothbrush.jpg", "10", "1"),
				new Products(2, "Soap", "dettolsoap.webp", "10", "1"),
				new Products(3, "Dettol", "dettol.webp", "10", "1"),
				new Products(4, "Coconut oil", "coconutoil.webp", "10", "1")

		);
	}

}
