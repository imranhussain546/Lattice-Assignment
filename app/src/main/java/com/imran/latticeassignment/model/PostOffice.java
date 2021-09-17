package com.imran.latticeassignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostOffice {


    @SerializedName("District")
    @Expose
    private String district;

    @SerializedName("State")
    @Expose
    private String state;


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}

