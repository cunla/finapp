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
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Group group;
}
