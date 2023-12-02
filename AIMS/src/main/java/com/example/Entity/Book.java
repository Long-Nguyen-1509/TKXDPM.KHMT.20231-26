package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@DiscriminatorValue("BOOK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends Media {
    private String genre;
    private String authors;
    private String publisher;

    @Temporal(TemporalType.DATE)
    @Column(name = "publication_date")
    private Date publicationDate;

    private int pages;
    private String cover;
    private String language;
}
