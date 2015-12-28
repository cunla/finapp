package com.delirium.finapp.users.domain;

import com.delirium.finapp.auditing.AbstractAuditingEntity;
import com.delirium.finapp.config.JView;
import com.delirium.finapp.exceptions.FinappUrlException;
import com.delirium.finapp.exceptions.UserCreationException;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.images.FinImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable, UserDetails {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    @JsonView(JView.UserSummary.class)
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @Size(min = 0, max = 100)
    @Column(length = 100)
    private String password;

    @Size(min = 0, max = 50)
    @Column(name = "name", length = 50)
    @JsonView(JView.UserSummary.class)
    private String name;

    @Email
    @Size(min = 0, max = 100)
    @Column(length = 100)
    @JsonView(JView.UserSummary.class)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    @JsonView(JView.UserSummary.class)
    private String langKey;

    @JsonIgnore
    @Size(min = 0, max = 20)
    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @Size(min = 0, max = 100)
    @Column(length = 100)
    private String permission;

    @Column(length = 100)
    @JsonView(JView.UserSummary.class)
    private String country;

    @Column(length = 100)
    @JsonView(JView.UserSummary.class)
    private String phone;

    @Column(length = 100)
    @JsonView(JView.UserSummary.class)
    private Date birthday;

    @Column(length = 2)
    @JsonView(JView.UserSummary.class)
    private String gender;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    @JsonView(JView.UserWithImage.class)
    private FinImage image;

    @JsonManagedReference
    @JsonIgnore
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<Group> groups;

    @Column(length = 255)
    @JsonView(JView.UserSummary.class)
    private String avatar;

    public User() {
        super();
    }

    public User(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.getPermission());
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public void setEncodedPassword(String password) throws UserCreationException {
        if (null == password) {
            log.warn("Password not valid: '{}'", password);
            throw new UserCreationException("Password not valid");
        }
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    private void encodePassword() {
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

    @JsonView(JView.UserWithGroups.class)
    public List<Group> getGroups() {
        return groups;
    }

    @JsonIgnore
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
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


    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
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
            ", gender=" + gender +
            ", groups=" + groups +
            '}';
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        try {
            this.image = FinImage.createFromUrl(avatar);
        } catch (FinappUrlException e) {
            log.warn("Couldn't parse URL {}", avatar);
        }
    }

    @JsonIgnore
    public void setEncodedPasswordNoException(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
