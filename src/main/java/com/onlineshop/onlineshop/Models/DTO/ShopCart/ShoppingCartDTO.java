package com.onlineshop.onlineshop.Models.DTO.ShopCart;

import com.onlineshop.onlineshop.Models.Database.ShoppingCart.ShoppingCart;

import java.util.List;

public class ShoppingCartDTO {
    private int id;
    private List<CartItemViewDTO> cartItems;

    public ShoppingCartDTO(){

    }

    public ShoppingCartDTO(ShoppingCart shoppingCart) {
        this.id = shoppingCart.getId();
        this.cartItems = shoppingCart.getCartItems().stream().map(CartItemViewDTO::new).toList();
    }

    public int getId() {
        return id;
    }

    public List<CartItemViewDTO> getCartItems() {
        return cartItems;
    }
}
