package com.example.DTO;

import com.example.Service.MediaService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private Map<CartItem, Integer> cartItems = new HashMap<>();

    MediaService mediaService = new MediaService();

    public void addCartItem(CartItem cartItem, int quantity) {
        if (quantity > 0) {
            if (!cartItems.containsKey(cartItem)) {
                cartItems.put(cartItem, quantity);
            } else {
                int newQuantity = cartItems.get(cartItem) + quantity;
                cartItems.put(cartItem, newQuantity);
            }
        }

    }

    public double getTotal() {
        double total = 0;
        for (CartItem cartItem: cartItems.keySet()) {
            total += cartItems.get(cartItem)*cartItem.getPrice();
        }
        return total;
    }

    public double getTotalWeight() {
        double total = 0;
        for (CartItem cartItem: cartItems.keySet()) {
            total += cartItems.get(cartItem)*cartItem.getWeight();
        }
        return total;
    }

    public String checkItemAvailability() {
        String unavai = "";
        String message = "";
        for (CartItem cartItem:
             cartItems.keySet()) {
            if (!mediaService.checkAvailability(cartItem.getId(), cartItems.get(cartItem))) {
                String name = cartItem.getName();
                unavai += "\n" + name;
            }
        }
        if (!unavai.isEmpty()) {
            message = "Unavailable items:" + unavai;
        } else {
            message = "Success";
        }
        return message;
    }
}
