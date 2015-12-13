package com.delirium.finapp.groups.domain;

import com.delirium.finapp.users.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String avatar="https://placehold.it/100x100";

    @ManyToOne
    @JoinColumn
    private User admin;

    @Column
    @JoinTable(name = "T_USER_GROUP")
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> members;


    public Group() {
        super();
        members = new LinkedList<>();
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

    @JsonProperty("membersNames")
    public List<String> getMemberNames() {
        List<String> names = new LinkedList<>();
        for(User user:members){
            names.add(user.getName());
        }
        return names;
    }

    public void setUsers(List<User> users) {
        this.members = users;
    }

    public void addUser(User user) {
        members.add(user);
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Account{" + "id='" + id + '\'' + ", name='" + name + '\''
            + ", label='" + label + "}";
    }

    public boolean hasMembers() {
        return !this.members.isEmpty();
    }

    public boolean hasUser(User user) {
        return this.members.contains(user);
    }

    @JsonIgnore
    public List<User> getMembers() {
        return this.members;
    }
}
