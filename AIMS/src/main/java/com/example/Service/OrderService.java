package com.example.Service;

import com.example.Entity.Order;
import com.example.Repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    public Order getOrder(int id) {
        return orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
