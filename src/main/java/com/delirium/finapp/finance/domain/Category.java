package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

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

    @Column
    private String icon;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Group group;

    @Transient
    private TransactionRepository transactionsRepo;
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;

    public Category() {

    }

    public Category(Group group, String color, String icon, String name) {
        this.icon = icon;
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

    public void setCategoryReport(TransactionRepository t, Date startDate, Date endDate) {
        this.transactionsRepo = t;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Double getTotal() {
        if (null != this.transactionsRepo) {
            return transactionsRepo.totalForCategoryInPeriod(this, startDate, endDate);
        }
        return null;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void updateCategory(Category c) {
        if (null != c.getColor()) {
            this.setColor(c.getColor());
        }
        if (null != c.getName()) {
            this.setName(c.getName());
        }
        if (null != c.getIcon()) {
            this.setIcon(c.getIcon());
        }
    }

    public Long getId() {
        return id;
    }
}
