package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String phone;
    private String province;
    private String address;
    private String email;
    private boolean rush;

    @Column(name = "delivery_íntruction")
    private String deliveryIntruction;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
