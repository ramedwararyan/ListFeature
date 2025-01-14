document.addEventListener("click", function(event) {
	if (event.target.classList.contains("add-to-cart-button")) {
		const button = event.target;
		const id = button.getAttribute("data-id");
		const productName = button.getAttribute("data-name");
		const price = button.getAttribute("data-price");

		// Check if the ID is retrieved correctly
		if (!id) {
			console.error("Product ID not found!");
			return;
		}

		add_to_cart(id, productName, price);
	}
});

function add_to_cart(id, productName, price) {
	let cart = localStorage.getItem("cart");
	if (cart == null) {
		let products = [];
		let product = { id: id, productName: productName, productQuantity: 1, price: price };
		products.push(product);
		localStorage.setItem("cart", JSON.stringify(products));
	} else {
		let products = JSON.parse(cart);
		let productIndex = products.findIndex((p) => p.id === id);
		if (productIndex > -1) {
			products[productIndex].productQuantity += 1;
		} else {
			let product = { id: id, productName: productName, productQuantity: 1, price: price };
			products.push(product);
		}
		localStorage.setItem("cart", JSON.stringify(products));
	}
}


// Function to update and render the cart in HTML
function updateCart() {
	const cart = JSON.parse(localStorage.getItem("cart")) || [];
	const cartContent = document.querySelector(".cart-content");

	if (cart.length === 0) {
		cartContent.innerHTML = `
		    
            <p>Your cart is empty</p>
            <button class="browse-btn">Browse Products</button>
        `;
		return;
	}

	// Render cart items
	cartContent.innerHTML = `
        <div class="cart-items">
            ${cart
			.map(
				(item) => `
                <div class="cart-item">
                    <div class="item-details">
                        <p class="item-name">${item.productName}</p>
                        <p class="item-price">Price: ₹${item.price}</p>
                        <p class="item-quantity">
                            Quantity: 
                            <button class="quantity-btn decrease" data-id="${item.id}">-</button>
                            ${item.productQuantity}
                            <button class="quantity-btn increase" data-id="${item.id}">+</button>
                        </p>
                    </div>
                    <button class="remove-item" data-id="${item.id}">Remove</button>
                </div>
            `
			)
			.join("")}
        </div>
        <div class="cart-summary">
            <p>Total Items: ${cart.reduce((sum, item) => sum + item.productQuantity, 0)}</p>
            <p>Total Price: ₹${cart.reduce((sum, item) => sum + item.productQuantity * item.price, 0)}</p>
        </div>
		<button 
		       class="add-to-list-button" 
		       data-cart='${JSON.stringify(cart)}'
		   >
		       Add to List
		   </button>
        <button class="checkout-btn">Checkout</button>
    `;

	// Add event listeners for quantity and remove buttons
	document.querySelectorAll(".quantity-btn").forEach((btn) => {
		btn.addEventListener("click", (event) => {
			const id = event.target.getAttribute("data-id");
			if (event.target.classList.contains("increase")) {
				updateCartQuantity(id, 1);
			} else if (event.target.classList.contains("decrease")) {
				updateCartQuantity(id, -1);
			}
		});
	});

	document.querySelectorAll(".remove-item").forEach((btn) => {
		btn.addEventListener("click", (event) => {
			const id = event.target.getAttribute("data-id");
			removeCartItem(id);
		});
	});
}

// Function to update cart item quantity
function updateCartQuantity(id, change) {
	let cart = JSON.parse(localStorage.getItem("cart")) || [];
	const itemIndex = cart.findIndex((item) => item.id === id);

	if (itemIndex > -1) {
		cart[itemIndex].productQuantity += change;

		if (cart[itemIndex].productQuantity <= 0) {
			cart.splice(itemIndex, 1);
		}

		localStorage.setItem("cart", JSON.stringify(cart));
		updateCart();
	}
}

// Function to remove an item from the cart
function removeCartItem(id) {
	let cart = JSON.parse(localStorage.getItem("cart")) || [];
	cart = cart.filter((item) => item.id !== id);

	localStorage.setItem("cart", JSON.stringify(cart));
	updateCart();
}

document.addEventListener("DOMContentLoaded", () => {
	// Initialize the cart on page load
	updateCart();

	const cartButton = document.getElementById("cartButton");
	const cartPanel = document.getElementById("cartPanel");
	const closeCart = document.getElementById("closeCart");
	const overlay = document.getElementById("overlay");

	// Open Cart
	cartButton.addEventListener("click", () => {
		updateCart(); // Ensure the cart is updated when opened
		cartPanel.classList.add("active");
		overlay.classList.add("active");
	});

	// Close Cart
	closeCart.addEventListener("click", () => {
		cartPanel.classList.remove("active");
		overlay.classList.remove("active");
	});

	// Close Cart when clicking outside
	overlay.addEventListener("click", () => {
		cartPanel.classList.remove("active");
		overlay.classList.remove("active");
	});

	// Event delegation for the dynamically added checkout button
	document.body.addEventListener("click", (event) => {
		if (event.target && event.target.classList.contains("checkout-btn")) {
			handleCheckout();
		}
	});


});


// Function to handle checkout
function handleCheckout() {
	// Retrieve cart data from localStorage
	const cart = JSON.parse(localStorage.getItem("cart")) || [];

	if (cart.length === 0) {
		alert("Your cart is empty!");
		return;
	}

	// Prepare the data to be sent to the backend
	const checkoutData = {
		items: cart,
		totalItems: cart.reduce((sum, item) => sum + item.productQuantity, 0),
		totalPrice: cart.reduce((sum, item) => sum + item.productQuantity * item.price, 0),
	};

	console.log("Checkout Data:", checkoutData);
	// Send data to the backend using fetch
	console.log("Sending checkout data to backend...");
	console.log("Checkout Data:", JSON.stringify(checkoutData));
	fetch("/api/checkout", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(checkoutData),
	})
		.then((response) => {
			if (response.ok) {
				alert("Order placed successfully!");
				localStorage.removeItem("cart"); // Clear cart after checkout
				window.location.href = "/"; // Redirect to confirmation page
			} else {
				console.error("Failed to place the order. Status:", response.status);
				alert("Failed to place the order. Please try again.");
			}
		})
		.catch((error) => {
			console.error("Error:", error);
			alert("An error occurred. Please try again.");
		});
}


document.body.addEventListener("click", (event) => {
	if (event.target && event.target.classList.contains("add-to-list-button")) {
		const cartData = JSON.parse(event.target.getAttribute("data-cart"));
		const totalItems = cartData.reduce((sum, item) => sum + item.productQuantity, 0);
		const totalPrice = cartData.reduce((sum, item) => sum + item.productQuantity * item.price, 0);
		addToList(cartData, totalItems, totalPrice);
	}
});
// Function to add items to the list and save to localStorage
function addToList(cartData, totalItems, totalPrice) {
	let listData = JSON.parse(localStorage.getItem("list")) || [];
	alert("Added to list");


	// Create a new entry that includes cartData, totalItems, and totalPrice
	const newListEntry = {

		items: cartData, // Array of cart items
		totalItems: totalItems, // Total number of items
		totalPrice: totalPrice, // Total price of items
	};

	// Append the new entry to the existing list
	listData.push(newListEntry);

	// Save the updated list to localStorage
	localStorage.setItem("list", JSON.stringify(listData));

	// Render the updated list
	renderList(listData);
}

// Function to render the list
function renderList(listData) {
	const listContent = document.querySelector(".list-content");

	if (!listContent) {
		console.error("List content element not found!");
		return;
	}

	if (listData.length === 0) {
		listContent.innerHTML = `<p>Your list is empty</p>`;
		return;
	}

	// Start with an empty string to accumulate the HTML content
	let htmlContent = "";

	listData.forEach((entry, index) => {
		// Adding a line separator between previous and current list
		if (index > 0) {
			htmlContent += `<hr />`;  // Line separator
		}

		htmlContent += `
		<h3>List ${index + 1}</h3> <!-- List number header -->
		<hr/>
                <div class="list-items">
                    ${entry.items
				.map(
					(item) => `
							
                            <div class="list-item">
                                <div class="item-details">
                                    <p class="item-name">${item.productName}</p>
                                    <p class="item-price">Price: ₹${item.price}</p>
                                    <p class="item-quantity">Quantity: ${item.productQuantity}</p>
                                </div>
                               
                            </div>
                        `
				)
				.join("")}
                </div><hr />
				<div class="list-entry">
				               <div class="entry-header">
				                   <p>Total Items: ${entry.totalItems}</p>
				                   <p>Total Price: ₹${entry.totalPrice}</p>
				               </div><hr />
							  		   <label for="pickupDate">Pickup Date:</label>
							   				          <input type="date" name="pickupDate" id="pickupDate" /><br />                                                 
													  <label for="orderTime">Select Pickup Time:</label>
													  <input type="time" id="orderTime" name="orderTime"><br/> 
													  <label for="frequency">Frequency:</label>
													  	  <select name="frequency" id="frequency" >
													  	   <option value="Daily">Daily</option>
													  	    <option value="Weekly">Weekly</option>
													  	<option value="Monthly">Monthly</option>
													  	  <option value="N/A">N/A</option>
													     </select><br />										    
													 <input type="number" id="iterval-days" placeholder="Enter days"><br/>
													 <input type="number" id="interval-time" placeholder="Enter seconds" min="1">
													 <hr />
			 								  
                <button class="remove-entry" data-index="${index}">Remove Entry</button>
				<button class="start-auto-order" data-index="${index}">Start Auto Order</button>
            </div>
        `;
	});

	// Set the inner HTML of the listContent
	listContent.innerHTML = `<div class="list-items">${htmlContent}</div>`;



	// Add event listeners for "Remove Entry" buttons
	document.querySelectorAll(".remove-entry").forEach((btn) => {
		btn.addEventListener("click", (event) => {
			const index = event.target.getAttribute("data-index");
			removeEntry(index);
		});
	});



	// Add event listeners for "Start Auto Order" buttons
	document.body.addEventListener("click", (event) => {
		if (event.target && event.target.classList.contains("start-auto-order")) {
			const index = event.target.getAttribute("data-index");
			const listData = JSON.parse(localStorage.getItem("list")) || [];
			const entry = listData[index];

			if (!entry) {
				alert("Entry not found!");
				return;
			}

			// Get input values for the specific list
			const listEntryElement = event.target.closest(".list-entry");
			const pickupDate = listEntryElement.querySelector("#pickupDate").value;
			const orderTime = listEntryElement.querySelector("#orderTime").value;
			const frequency = listEntryElement.querySelector("#frequency").value;
			const intervalDays = listEntryElement.querySelector("#iterval-days").value;
			const intervalTime = listEntryElement.querySelector("#interval-time").value;

			// Add these values to the entry object
			const orderData = {
				...entry,
				pickupDate,
				orderTime,
				frequency,
				intervalDays: parseInt(intervalDays) || null,
				intervalTime: parseInt(intervalTime) || null,
			};

			// Send the data to the backend
			sendDataToBackend(orderData);
		}
	});
}




// Function to remove an entire entry from the list
function removeEntry(index) {
	let listData = JSON.parse(localStorage.getItem("list")) || [];

	// Remove the entry at the specified index
	listData.splice(index, 1);

	// Save the updated list to localStorage
	localStorage.setItem("list", JSON.stringify(listData));

	// Re-render the updated list
	renderList(listData);
}




// Load list data from localStorage on page load
document.addEventListener("DOMContentLoaded", () => {
	const listData = JSON.parse(localStorage.getItem("list")) || [];
	renderList(listData);
});

// Open List Panel
document.getElementById("mylist").addEventListener("click", () => {
	// Ensure the list is updated when opened
	document.getElementById("listPanel").classList.add("active");
	document.getElementById("overlay").classList.add("active");
});

// Close List Panel
document.getElementById("closeList").addEventListener("click", () => {
	document.getElementById("listPanel").classList.remove("active");
	document.getElementById("overlay").classList.remove("active");
});

// Close List Panel when clicking outside
document.getElementById("overlay").addEventListener("click", () => {
	document.getElementById("listPanel").classList.remove("active");
	document.getElementById("overlay").classList.remove("active");
});

// Function to send data to backend
function sendDataToBackend(orderData) {
	const apiUrl = '/api/order'; // Replace with your backend endpoint URL

	fetch(apiUrl, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(orderData), // Convert the order data to JSON
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.text(); // Expect plain text response
		})
		.then((message) => {
			alert("Order placed");
			alert(message); // Display the success message from the backend
		})
		.catch((error) => {
			console.error('Error sending order:', error);
			alert('Failed to place the order. Please try again.');
		});
}
