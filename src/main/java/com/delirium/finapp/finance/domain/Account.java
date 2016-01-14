package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

/**
 * Created by morand3 on 12/23/2015.
 */
@Entity
@Table(name = "F_ACCOUNT")
public class Account {
    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    @JsonIgnore
    private Group group;

    @Column(length = 15)
    private String color;

    @Column
    private String name;

    @Column
    private String icon;

    @Column
    private Date lastValidated;

    @Column
    private Double startingBalance;

    @Transient
    private TransactionRepository transactionsRepo;
    @JsonProperty
    @Transient
    private Double balance;

    @JsonProperty
    @Transient
    private Integer count;

    public Account() {
    }

    public Account(Group group, String color, String icon, String name, Double startingBalance) {
        this.group = group;
        this.color = color;
        this.icon = icon;
        this.name = name;
        this.startingBalance = (null == startingBalance) ? 0.0 : startingBalance;
        this.lastValidated = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTransactionsRepo(TransactionRepository transactionsRepo) {
        this.transactionsRepo = transactionsRepo;
    }

    public Double getBalance() {
        return (null != this.transactionsRepo) ? this.transactionsRepo.balanceForAccount(this) : balance;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setBalance(Double balance, Integer count) {
        this.balance = balance;
        this.count = count;
    }

    public Date getLastValidated() {
        return lastValidated;
    }

    public void setLastValidated(Date lastValidated) {
        this.lastValidated = lastValidated;
    }

    public Double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(Double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public void updateAccount(Account acc) {
        if (null != acc.getColor()) {
            this.setColor(acc.getColor());
        }
        if (null != acc.getName()) {
            this.setName(acc.getName());
        }
        if (null != acc.getIcon()) {
            this.setIcon(acc.getIcon());
        }
        if (null != acc.getStartingBalance()) {
            this.setStartingBalance(acc.getStartingBalance());
        }
    }

}
