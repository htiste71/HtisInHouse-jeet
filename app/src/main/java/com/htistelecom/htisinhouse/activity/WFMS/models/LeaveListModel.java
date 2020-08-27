package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveListModel {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("LeaveFromTo")
    @Expose
    private String leaveFromTo;

    @SerializedName("LeaveFrom")
    @Expose
    private String leaveFrom;

    @SerializedName("LeaveTo")
    @Expose
    private String leaveTo;

    public String getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(String leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    @SerializedName("LeaveType")
    @Expose
    private String leaveType;
    @SerializedName("RequestedOn")
    @Expose
    private String requestedOn;
    @SerializedName("RespondedOn")
    @Expose
    private String respondedOn;
    @SerializedName("RespondedBy")
    @Expose
    private String respondedBy;
    @SerializedName("ApprovedRemarks")
    @Expose
    private String approvedRemarks;
    @SerializedName("LeaveRemarks")
    @Expose
    private String leaveRemarks;
    @SerializedName("LeaveStatusId")
    @Expose
    private String leaveStatusId;
    @SerializedName("LeaveStatus")
    @Expose
    private String leaveStatus;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLeaveFromTo() {
        return leaveFromTo;
    }

    public void setLeaveFromTo(String leaveFromTo) {
        this.leaveFromTo = leaveFromTo;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getRespondedOn() {
        return respondedOn;
    }

    public void setRespondedOn(String respondedOn) {
        this.respondedOn = respondedOn;
    }

    public String getRespondedBy() {
        return respondedBy;
    }

    public void setRespondedBy(String respondedBy) {
        this.respondedBy = respondedBy;
    }

    public String getApprovedRemarks() {
        return approvedRemarks;
    }

    public void setApprovedRemarks(String approvedRemarks) {
        this.approvedRemarks = approvedRemarks;
    }

    public String getLeaveRemarks() {
        return leaveRemarks;
    }

    public void setLeaveRemarks(String leaveRemarks) {
        this.leaveRemarks = leaveRemarks;
    }

    public String getLeaveStatusId() {
        return leaveStatusId;
    }

    public void setLeaveStatusId(String leaveStatusId) {
        this.leaveStatusId = leaveStatusId;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}