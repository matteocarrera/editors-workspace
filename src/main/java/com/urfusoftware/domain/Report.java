package com.urfusoftware.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reports")
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

    public Report() {
    }

    public Report(String title, Date reportDate, int timeSpent, String reportLink, String resultLink, String comments, Boolean accepted, User user) {
        this.title = title;
        this.reportDate = reportDate;
        this.timeSpent = timeSpent;
        this.reportLink = reportLink;
        this.resultLink = resultLink;
        this.comments = comments;
        this.accepted = accepted;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return reportDate;
    }

    public void setDate(Date date) {
        this.reportDate = date;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public String getResultLink() {
        return resultLink;
    }

    public void setResultLink(String resultLink) {
        this.resultLink = resultLink;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}