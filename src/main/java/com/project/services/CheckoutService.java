package com.project.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.entities.Checkout;
import com.project.repo.CheckoutRepository;

@Service
public class CheckoutService {
    private final CheckoutRepository checkoutRepository;

    public CheckoutService(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Transactional
    public Checkout saveCheckout(Checkout checkout) {
        return checkoutRepository.save(checkout);
    }
}

