package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "media_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private MediaCategory category;

    private String title;
    private String value;
    private double price;
    private int quantity;

    @Column(name = "image_url")
    private String imageURL;

    @Temporal(TemporalType.DATE)
    @Column(name = "receipt_date")
    private Date receiptDate;

    private String description;
    private String dimensions;
    private boolean rushSupport;
}
