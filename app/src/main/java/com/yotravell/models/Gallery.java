package com.yotravell.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Developer on 9/13/2017.
 */

public class Gallery implements Serializable {
    @SerializedName("value")
    private String value;
    @SerializedName("name")
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}