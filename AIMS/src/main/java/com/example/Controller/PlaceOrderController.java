package com.example.Controller;

import com.example.DTO.Cart;
import com.example.DTO.CartItem;
import com.example.DTO.DeliveryForm;
import com.example.Entity.*;
import com.example.Service.MediaService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class PlaceOrderController {
    private Order currentOrder;

    private MediaService mediaService = new MediaService();

    // check the item availability to add to the currentOrder
    public String placeOrder(Cart cart) {
        if (!cart.getCartItems().isEmpty() && cart.checkItemAvailability().equals("Success")) {
            double totalPrice = 0;

            // loop through cart items, find corresponding media,
            // create orderMedia add to the order, calculate the total price
            for (CartItem cartItem:
                 cart.getCartItems().keySet()) {
                int quantity = cart.getCartItems().get(cartItem);
                Media media = mediaService.getMedia(cartItem.getId());
                double mediaPrice = quantity * media.getPrice();
                OrderMedia orderMedia = OrderMedia.builder()
                        .order(currentOrder)
                        .media(media)
                        .quantity(quantity)
                        .price(mediaPrice)
                        .build();
                currentOrder.getOrderMedias().add(orderMedia);
                totalPrice += mediaPrice;
            }
            currentOrder.setTotal(totalPrice);
            return "To delivery info";
        }
        return cart.checkItemAvailability();
    }

    // create a deliveryInfo entity to add to the order then call to calculate the shipping fees
    // attach to submit delivery info button
    public String processDeliveryInfo(Cart cart, DeliveryForm deliveryForm) {
        int numberOfRushItems = 0;
        if (validateDeliveryInfo(deliveryForm).equals("Success")) {
            DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                    .name(deliveryForm.getName())
                    .phone(deliveryForm.getPhone())
                    .province(deliveryForm.getProvince())
                    .address(deliveryForm.getAddress())
                    .email(deliveryForm.getEmail())
                    .rush(deliveryForm.isRush())
                    .deliveryIntruction(deliveryForm.getDeliveryInstruction())
                    .build();
            if (deliveryForm.isRush()) {
                numberOfRushItems = processRushOrder(cart, deliveryForm, deliveryInfo);
            }
            currentOrder.setDeliveryInfo(deliveryInfo);
            calculateShippingFees(cart, deliveryForm, numberOfRushItems);
            return "To Invoice";
        }
        return validateDeliveryInfo(deliveryForm);
    }

    public String validateDeliveryInfo(DeliveryForm deliveryForm) {
        String phoneNumber = deliveryForm.getPhone();
        String email = deliveryForm.getEmail();

        // check phone number contains only digits
        if (!phoneNumber.matches("\\d+")) {
            return "Invalid phone number";
        }

        // check phone number 10 or 11
        if (phoneNumber.length() != 10 && phoneNumber.length() !=11) {
            return "Phone number must be between 10 or 11 digits";
        }

        // check email format
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return "Invalid email";
        }
        return "Success";
    }

    // attach to confirm invoice button
    public int processRushOrder(
            Cart cart, DeliveryForm deliveryForm, DeliveryInfo deliveryInfo
            ) {
        Map<CartItem, Integer> cartItems = cart.getCartItems();
        int numberOfRushItems = 0;
        String deliveryInstruction = "Rushed: ";
        if (deliveryForm.getProvince().equals("Ha Noi")) {
            for (CartItem cartItem:
                 cartItems.keySet()) {
                Media media = mediaService.getMedia(cartItem.getId());
                if (media.isRushSupport()) {
                    numberOfRushItems++;
                    String rushCartItems = cartItem.getName()
                            + "(" + cartItems.get(cartItem).toString()
                            + "), ";
                    deliveryInstruction += rushCartItems;
                }
            }
        }
        deliveryInfo.setDeliveryIntruction(deliveryInstruction);
        return numberOfRushItems;
    }

    public void calculateShippingFees(Cart cart, DeliveryForm deliveryForm, int numberOfRushItems) {
        double shippingFees = 0;
        double totalWeight = cart.getTotalWeight();
        if (cart.getTotal() < 100000) {
            if (deliveryForm.getProvince().equals("Ha Noi") || deliveryForm.getProvince().equals("HCM")) {
                shippingFees += 22000;
                if (totalWeight > 3) {
                    shippingFees += Math.ceil((totalWeight-3)/0.5)*2500;
                }
            } else {
                shippingFees += 30000;
                if (totalWeight > 0.5) {
                    shippingFees += Math.ceil((totalWeight-0.5)/0.5)*2500;
                }
            }
        }
        shippingFees += numberOfRushItems*10000;
        currentOrder.setShippingFees(shippingFees);
    }

    public void createInvoice() {
        Invoice invoice = Invoice.builder()
                .order(currentOrder)
                .total(currentOrder.getTotal()*1.1+currentOrder.getShippingFees())
                .build();
        currentOrder.setInvoice(invoice);
    }
}
