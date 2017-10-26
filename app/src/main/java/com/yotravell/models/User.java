package com.yotravell.models;

/**
 * Created by Developer on 9/13/2017.
 */

public class User {

    private String username;
    private String email;
    private String fullName;
    private Integer id;

    public User(String username, String email, String fullName, Integer id) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}