package com.example.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@DiscriminatorValue("CD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CD extends Media{
    private String genre;
    private String artists;

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    private Date releaseDate;

    private String tracklist;

    @Column(name = "record_label")
    private String recordLabel;
}
