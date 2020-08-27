package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedTaskModel {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("WorkDate")
    @Expose
    private String workDate;
    @SerializedName("TaskId")
    @Expose
    private Object taskId;
    @SerializedName("SiteName")
    @Expose
    private String siteName;
    @SerializedName("SAPId")
    @Expose
    private String sAPId;
    @SerializedName("EntryType")
    @Expose
    private String entryType;
    @SerializedName("ActivityText")
    @Expose
    private String activityText;
    @SerializedName("DayId")
    @Expose
    private String dayId;
    @SerializedName("SiteUploadedId")
    @Expose
    private String siteUploadedId;
    @SerializedName("ClaimedAmount")
    @Expose
    private String claimedAmount;
    @SerializedName("ApprovedAmount")
    @Expose
    private String approvedAmount;
    @SerializedName("RejectedAmount")
    @Expose
    private String rejectedAmount;
    @SerializedName("ProjectName")
    @Expose
    private String projectName;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public Object getTaskId() {
        return taskId;
    }

    public void setTaskId(Object taskId) {
        this.taskId = taskId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSAPId() {
        return sAPId;
    }

    public void setSAPId(String sAPId) {
        this.sAPId = sAPId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getSiteUploadedId() {
        return siteUploadedId;
    }

    public void setSiteUploadedId(String siteUploadedId) {
        this.siteUploadedId = siteUploadedId;
    }

    public String getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(String claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getRejectedAmount() {
        return rejectedAmount;
    }

    public void setRejectedAmount(String rejectedAmount) {
        this.rejectedAmount = rejectedAmount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
