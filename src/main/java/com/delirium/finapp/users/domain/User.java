package com.delirium.finapp.users.domain;

import com.delirium.finapp.groups.domain.Group;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity @Table(name = "T_USER") @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

    @Id @GeneratedValue private Long id;

    @JsonIgnore @Size(min = 0, max = 100) @Column(length = 100) private String password;

    @Size(min = 0, max = 50) @Column(name = "name", length = 50) private String name;

    @Email @Size(min = 0, max = 100) @Column(length = 100) private String email;

    @JsonIgnore private boolean activated = false;

    @JsonIgnore @Size(min = 2, max = 5) @Column(name = "lang_key", length = 5)
    private String langKey;

    @JsonIgnore @Size(min = 0, max = 20) @Column(name = "activation_key", length = 20)
    private String activationKey;

    @JsonIgnore @Size(min = 0, max = 100) @Column(length = 100) private String permission;

    @JsonIgnore @Column(length = 100) private String country;
    @JsonIgnore @Column(length = 100) private String phone;
    @JsonIgnore @Column(length = 100) private Date birthday;
    @JsonIgnore @Column(length = 100) private Boolean male;

    // @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) private Group group;

    //    public String getLogin() {
    //        return login;
    //    }

    //    public void setLogin(String login) {
    //        this.login = login;
    //    }

    @JsonIgnore public String getPassword() {
        return password;
    }

    @JsonProperty public void setPassword(String password) {
        this.password = password;
    }

    public void setEncodedPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return email.equals(user.email);

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getMale() {
        return male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    @Override public int hashCode() {
        return email.hashCode();
    }

    @Override public String toString() {
        return "User{" +
            "id=" + id +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            ", permission='" + permission + '\'' +
            ", country='" + country + '\'' +
            ", phone='" + phone + '\'' +
            ", birthday=" + birthday +
            ", male=" + male +
            ", group=" + group +
            '}';
    }
}
