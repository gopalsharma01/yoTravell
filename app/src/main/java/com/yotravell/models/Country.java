package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Developer on 9/13/2017.
 */

public class Country {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("aCountryList")
    private ArrayList<CountryList> aCountryList;

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
}