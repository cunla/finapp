package com.delirium.finapp.groups.domain;

import com.delirium.finapp.users.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "T_GROUP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Group implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 0, max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(min = 0, max = 100)
    @Column(name = "label", length = 100)
    private String label;

    @Column
    private User admin;

    @Column
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> users;


    public Group() {
        super();
        users = new LinkedList<>();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Account{" + "id='" + id + '\'' + ", name='" + name + '\''
            + ", label='" + label + "}";
    }
}
