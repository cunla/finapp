package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by morand3 on 12/23/2015.
 */
@Entity
@Table(name = "F_CATEGORY")
public class Category {
    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue
    private Long id;

    @Column(length = 15)
    private String color;

    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Group group;

    public Category() {

    }

    public Category(Group group, String color, String name) {
        this.group = group;
        this.color = color;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateCategory(Category c) {
        if (null != c.getColor()) {
            this.setColor(c.getColor());
        }
        if (null != c.getName()) {
            this.setName(c.getName());
        }
    }

    public Long getId() {
        return id;
    }
}
