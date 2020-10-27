package com.urfusoftware.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
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

    public Project(String title, String status, User manager, User translator, User editor) {
        this.title = title;
        this.status = status;
        this.manager = manager;
        this.translator = translator;
        this.editor = editor;
    }

    public void setOpened() {
        this.opened = this.status.equals("В работе");
    }
}
