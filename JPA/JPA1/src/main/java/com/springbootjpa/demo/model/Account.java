package com.springbootjpa.demo.model;

import org.hibernate.annotations.CollectionId;

import javax.persistence.*;

@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String username;
    private String password;

    @Embedded
    private Address homeAddress;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
