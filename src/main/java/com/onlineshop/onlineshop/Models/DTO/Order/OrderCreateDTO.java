package com.onlineshop.onlineshop.Models.DTO.Order;

import java.util.List;

public class OrderCreateDTO {
    private float totalAmount;
    //private String creationDate;
    private PaymentMethodCompositeDTO paymentMethod;
    private ShippingMethodCompositeDTO shippingMethod;
    private List<OrderItemCreateDTO> orderItems;

    public OrderCreateDTO() {
    }

    public float getTotalAmount() {
        return totalAmount;
    }

//    public String getCreationDate() {
//        return creationDate;
//    }

    public PaymentMethodCompositeDTO getPaymentMethod() {
        return paymentMethod;
    }

    public ShippingMethodCompositeDTO getShippingMethod() {
        return shippingMethod;
    }

    public List<OrderItemCreateDTO> getOrderItems() {
        return orderItems;
    }
}
