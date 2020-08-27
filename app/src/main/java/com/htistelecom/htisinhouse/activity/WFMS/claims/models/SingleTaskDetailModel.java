package com.htistelecom.htisinhouse.activity.WFMS.claims.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class SingleTaskDetailModel {

    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("WorkDate")
    @Expose
    private String workDate;
    @SerializedName("Role")
    @Expose
    private String role;
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
    @SerializedName("FromLocation")
    @Expose
    private String fromLocation;
    @SerializedName("ToLocation")
    @Expose
    private String toLocation;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("LevelDone")
    @Expose
    private String levelDone;
    @SerializedName("ExpenseDetailId")
    @Expose
    private String expenseDetailId;
    @SerializedName("DistanceTravelled")
    @Expose
    private String distanceTravelled;
    @SerializedName("TransportMode")
    @Expose
    private String transportMode;
    @SerializedName("PMRemarks")
    @Expose
    private String pMRemarks;
    @SerializedName("Images")
    @Expose
    private String images;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLevelDone() {
        return levelDone;
    }

    public void setLevelDone(String levelDone) {
        this.levelDone = levelDone;
    }

    public String getExpenseDetailId() {
        return expenseDetailId;
    }

    public void setExpenseDetailId(String expenseDetailId) {
        this.expenseDetailId = expenseDetailId;
    }

    public String getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(String distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getPMRemarks() {
        return pMRemarks;
    }

    public void setPMRemarks(String pMRemarks) {
        this.pMRemarks = pMRemarks;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
