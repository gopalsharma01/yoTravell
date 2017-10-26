package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 10/25/2017.
 */

public class Members {
    private String profileImage;
    private String name;
    private String username;
    private String email;
    private String friendRequest;
    private String lastActivity;
    private Integer id;
    private Integer requestId;
    private Integer requestSender;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(String friendRequest) {
        this.friendRequest = friendRequest;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(Integer requestSender) {
        this.requestSender = requestSender;
    }
}
