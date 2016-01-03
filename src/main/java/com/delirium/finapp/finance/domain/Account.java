package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private Group group;

    public Long getId() {
        return id;
    }
}
