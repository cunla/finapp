package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;

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


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Group group;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
