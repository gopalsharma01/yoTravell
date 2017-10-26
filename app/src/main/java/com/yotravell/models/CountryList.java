package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Developer on 9/13/2017.
 */

public class CountryList {
    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}