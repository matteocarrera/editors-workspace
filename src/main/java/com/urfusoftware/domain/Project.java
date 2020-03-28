package com.urfusoftware.domain;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String title;
    private String status;

    @Transient
    private boolean opened;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "translator_id", nullable = false)
    private User translator;

    @ManyToOne
    @JoinColumn(name = "editor_id", nullable = false)
    private User editor;

    public Project() {
    }

    public Project(String title, String status, User manager, User translator, User editor) {
        this.title = title;
        this.status = status;
        this.manager = manager;
        this.translator = translator;
        this.editor = editor;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened() {
        if (this.status.equals("В работе")) this.opened = true;
        else this.opened = false;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public User getTranslator() {
        return translator;
    }

    public void setTranslator(User translator) {
        this.translator = translator;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }
}
