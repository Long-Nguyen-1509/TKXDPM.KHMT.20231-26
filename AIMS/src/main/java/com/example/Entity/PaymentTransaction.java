package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "payment_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
