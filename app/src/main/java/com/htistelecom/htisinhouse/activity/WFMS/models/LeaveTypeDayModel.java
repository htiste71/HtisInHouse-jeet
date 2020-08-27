package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveTypeDayModel {
    @SerializedName("DayTypeId")
    @Expose
    private String dayTypeId;
    @SerializedName("DayType")
    @Expose
    private String dayType;
    @SerializedName("DayValue")
    @Expose
    private String dayValue;

    public String getDayTypeId() {
        return dayTypeId;
    }

    public void setDayTypeId(String dayTypeId) {
        this.dayTypeId = dayTypeId;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }
}
