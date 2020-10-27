package com.urfusoftware.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String title;
    private Date reportDate;

    @Transient
    private String stringDate;

    private int timeSpent;
    private String reportLink;
    private String resultLink;
    private String comments;
    private Boolean accepted;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Report(String title,
                  Project project,
                  Date reportDate,
                  int timeSpent,
                  String reportLink,
                  String resultLink,
                  String comments,
                  Boolean accepted,
                  User user) {
        this.title = title;
        this.project = project;
        this.reportDate = reportDate;
        this.timeSpent = timeSpent;
        this.reportLink = reportLink;
        this.resultLink = resultLink;
        this.comments = comments;
        this.accepted = accepted;
        this.user = user;
    }
}