package com.urfusoftware.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String text;

    private Date newsDate;

    @Transient
    private String stringDate;

    public News(String text, Date newsDate) {
        this.text = text;
        this.newsDate = newsDate;
    }
}
