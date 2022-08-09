package com.misman.start.model;

import com.misman.start.abstracts.BaseEntity;
import com.misman.start.model.enumeration.Authorities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "thermo_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "fist_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;


    @Column(name = "password_refresh_token")
    private String passwordRefreshToken;

    @ElementCollection(targetClass = Authorities.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<Authorities> authorities;

    @Column(name = "lang")
    private String lang = "tr-TR";

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public String getPasswordRefreshToken() {
        return passwordRefreshToken;
    }

    public void setPasswordRefreshToken(String passwordRefreshToken) {
        this.passwordRefreshToken = passwordRefreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Authorities> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authorities> authorities) {
        this.authorities = authorities;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
