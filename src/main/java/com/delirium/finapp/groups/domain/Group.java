package com.delirium.finapp.groups.domain;

import com.delirium.finapp.finance.domain.Transaction;
import com.delirium.finapp.users.domain.User;
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
    private String avatar = "/100x100.png";

    @ManyToOne
    @JoinColumn
    private User admin;

    @Column
    @JoinTable(name = "T_USER_GROUP")
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> members;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

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

    @JsonProperty("members")
    public List<Member> getMemberNames() {
        List<Member> members = new LinkedList<>();
        for (User user : this.members) {
            members.add(new Member(user));
        }
        return members;
    }

    @JsonProperty("balance")
    public Double getBalance() {
        Double sum = 0.0;
        for (Transaction t : transactions) {
            sum += t.getAmount();
        }
        return sum;
    }

    @JsonProperty("currency")
    public String getDefaultCurrency() {
        return "ILS";
    }

    @JsonProperty("currencySymbol")
    public String getCurrencySymbol() {
        return "ILS";
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

    private class Member {
        public Long id;
        public String name;
        public String email;
        public String avatar;
        public boolean isAdmin;


        public Member(User user) {
            this.name = user.getName();
            this.id = user.getId();
            this.email = user.getEmail();
            this.avatar = user.getAvatar();
            this.isAdmin = (admin.getId() == this.id);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }
    }
}
