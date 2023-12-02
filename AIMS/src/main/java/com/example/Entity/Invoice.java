package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double total;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentTransaction paymentTransaction;

}
