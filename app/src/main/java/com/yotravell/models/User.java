package com.yotravell.models;

/**
 * Created by Developer on 9/13/2017.
 */

public class User {

    private String username;
    private String email;
    private String fullName;
    private Integer id;
    private String totalMember;
    private String totalFriend;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    private String profileImage;

    public String getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(String totalMember) {
        this.totalMember = totalMember;
    }

    public String getTotalFriend() {
        return totalFriend;
    }

    public void setTotalFriend(String totalFriend) {
        this.totalFriend = totalFriend;
    }

    public User(String username, String email, String fullName, Integer id,String totalMember, String totalFriend,String profileImage) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.totalMember = totalMember;
        this.totalFriend = totalFriend;
        this.profileImage = profileImage;
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