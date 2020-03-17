package com.urfusoftware;

public class SelectedRole {
    public Long id;
    public String name;
    public String selected;

    public SelectedRole() {
    }

    public SelectedRole(Long id, String name, Boolean isSelected) {
        this.id = id;
        this.name = name;
        if (isSelected) this.selected = "selected";
        else this.selected = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
