package com.example.management_demo.controller.users.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UserBulkCreateRequest {
    
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity must not exceed 1000")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
