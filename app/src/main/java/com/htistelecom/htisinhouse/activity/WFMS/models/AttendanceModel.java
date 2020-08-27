package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceModel {
    @SerializedName("EmpId")
    @Expose
    private Integer empId;
    @SerializedName("DayId")
    @Expose
    private Integer dayId;
    @SerializedName("DayStatus")
    @Expose
    private String dayStatus;
    @SerializedName("AttDate")
    @Expose
    private String date;
    @SerializedName("WeekOff")
    @Expose
    private String weekOff;
    @SerializedName("Leave")
    @Expose
    private String onLeave;
    @SerializedName("Title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getDayId() {
        return dayId;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public String getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeekOff() {
        return weekOff;
    }

    public void setWeekOff(String weekOff) {
        this.weekOff = weekOff;
    }

    public String getOnLeave() {
        return onLeave;
    }

    public void setOnLeave(String onLeave) {
        this.onLeave = onLeave;
    }

}
