package com.urfusoftware;

import com.urfusoftware.domain.User;

import java.util.Date;

public class SelectedReport {
    private Long id;
    private String title;
    private Date reportDate;
    private int timeSpent;
    private String reportLink;
    private String resultLink;
    private String comments;
    private Boolean accepted;
    private User user;
    private Boolean isManager;

    public SelectedReport() {
    }

    public SelectedReport(String title, Date reportDate, int timeSpent, String reportLink, String resultLink, String comments, Boolean accepted, User user, Boolean isManager) {
        this.title = title;
        this.reportDate = reportDate;
        this.timeSpent = timeSpent;
        this.reportLink = reportLink;
        this.resultLink = resultLink;
        this.comments = comments;
        this.accepted = accepted;
        this.user = user;
        this.isManager = isManager;
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

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
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

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }
}
