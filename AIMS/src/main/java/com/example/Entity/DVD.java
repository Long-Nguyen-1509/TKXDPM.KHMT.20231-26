package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@DiscriminatorValue("DVD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DVD extends Media {
    private String type;
    private String genre;

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    private Date releaseDate;

    private String studio;
    private String director;
    private String language;
    private String subtitle;
    private int runtime;
}
