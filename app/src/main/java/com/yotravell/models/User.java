package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 9/13/2017.
 */

public class User {
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("id")
    private Integer id;
    @SerializedName("totalMember")
    private String totalMember;
    @SerializedName("totalFriend")
    private String totalFriend;
    @SerializedName("profileImage")
    private String profileImage;
    @SerializedName("fullProfileImage")
    private String fullProfileImage;

    public User(String username, String email, String fullName, Integer id,String totalMember, String totalFriend,String profileImage,String fullProfileImage) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.totalMember = totalMember;
        this.totalFriend = totalFriend;
        this.profileImage = profileImage;
        this.fullProfileImage = fullProfileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

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

    public String getFullProfileImage() {
        return fullProfileImage;
    }

    public void setFullProfileImage(String fullProfileImage) {
        this.fullProfileImage = fullProfileImage;
    }
}