package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Member;
import java.util.ArrayList;

/**
 * Created by Developer on 10/3/2017.
 */

public class ResponseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("UserData")
    private User UserData;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("activityFeed")
    private ArrayList<Feed> activityFeed;
    @SerializedName("aCountryList")
    private ArrayList<CountryList> aCountryList;
    @SerializedName("aUsersList")
    private ArrayList<Members> aUsersList;
    @SerializedName("aUploadedPhotos")
    private ArrayList<Gallery> aUploadedPhotos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CountryList> getaCountryList() {
        return aCountryList;
    }

    public void setaCountryList(ArrayList<CountryList> aCountryList) {
        this.aCountryList = aCountryList;
    }

    public ArrayList<Members> getaUsersList() {
        return aUsersList;
    }

    public void setaUsersList(ArrayList<Members> aUsersList) {
        this.aUsersList = aUsersList;
    }

    public User getUserData() {
        return UserData;
    }

    public void setUserData(User userData) {
        UserData = userData;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ArrayList<Feed> getActivityFeed() {
        return activityFeed;
    }

    public void setActivityFeed(ArrayList<Feed> activityFeed) {
        this.activityFeed = activityFeed;
    }

    public ArrayList<Gallery> getaUploadedPhotos() {
        return aUploadedPhotos;
    }

    public void setaUploadedPhotos(ArrayList<Gallery> aUploadedPhotos) {
        this.aUploadedPhotos = aUploadedPhotos;
    }
}
