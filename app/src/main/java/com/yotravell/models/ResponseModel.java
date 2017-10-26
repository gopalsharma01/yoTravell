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
    @SerializedName("aCountryList")
    private ArrayList<CountryList> aCountryList;
    @SerializedName("aUsersList")
    private ArrayList<Members> aUsersList;

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
}
