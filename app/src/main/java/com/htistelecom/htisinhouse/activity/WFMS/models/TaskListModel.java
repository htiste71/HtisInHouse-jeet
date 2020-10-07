package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskListModel implements Serializable {

    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("SiteUploadedId")
    @Expose
    private Object siteUploadedId;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("ProjectId")
    @Expose
    private String projectId;
    @SerializedName("ProjectName")
    @Expose
    private String projectName;
    @SerializedName("SiteId")
    @Expose
    private String siteId;
    @SerializedName("SiteName")
    @Expose
    private String siteName;
    @SerializedName("SAPID")
    @Expose
    private String sAPID;
    @SerializedName("EntryType")
    @Expose
    private String entryType;
    @SerializedName("WorkDate")
    @Expose
    private String workDate;
    @SerializedName("ActivityId")
    @Expose
    private String activityId;
    @SerializedName("ActivityName")
    @Expose
    private String activityName;
    @SerializedName("ActivityDemoImage")
    @Expose
    private String activityDemoImage;
    @SerializedName("ActivityDemoImagePath")
    @Expose
    private String activityDemoImagePath;
    @SerializedName("ActivityImage")
    @Expose
    private String activityImage;
    @SerializedName("ActivityImagePath")
    @Expose
    private String activityImagePath;
    @SerializedName("ActivityText")
    @Expose
    private String activityText;
    @SerializedName("DayId")
    @Expose
    private String dayId;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("IsStarted")
    @Expose
    private String isStarted;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("FromDate")
    @Expose
    private Object fromDate;
    @SerializedName("ToDate")
    @Expose
    private Object toDate;
    @SerializedName("TaskRemarks")
    @Expose
    private String taskRemarks;
    @SerializedName("SubActivityId")
    @Expose
    private String subActivityId;
    @SerializedName("SubActivityTitle")
    @Expose
    private String subActivityTitle;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Object getSiteUploadedId() {
        return siteUploadedId;
    }

    public void setSiteUploadedId(Object siteUploadedId) {
        this.siteUploadedId = siteUploadedId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSAPID() {
        return sAPID;
    }

    public void setSAPID(String sAPID) {
        this.sAPID = sAPID;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDemoImage() {
        return activityDemoImage;
    }

    public void setActivityDemoImage(String activityDemoImage) {
        this.activityDemoImage = activityDemoImage;
    }

    public String getActivityDemoImagePath() {
        return activityDemoImagePath;
    }

    public void setActivityDemoImagePath(String activityDemoImagePath) {
        this.activityDemoImagePath = activityDemoImagePath;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public String getActivityImagePath() {
        return activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(String isStarted) {
        this.isStarted = isStarted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getFromDate() {
        return fromDate;
    }

    public void setFromDate(Object fromDate) {
        this.fromDate = fromDate;
    }

    public Object getToDate() {
        return toDate;
    }

    public void setToDate(Object toDate) {
        this.toDate = toDate;
    }

    public String getTaskRemarks() {
        return taskRemarks;
    }

    public void setTaskRemarks(String taskRemarks) {
        this.taskRemarks = taskRemarks;
    }

    public String getSubActivityId() {
        return subActivityId;
    }

    public void setSubActivityId(String subActivityId) {
        this.subActivityId = subActivityId;
    }

    public String getSubActivityTitle() {
        return subActivityTitle;
    }

    public void setSubActivityTitle(String subActivityTitle) {
        this.subActivityTitle = subActivityTitle;
    }

}
