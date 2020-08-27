package com.htistelecom.htisinhouse.activity.WFMS.claims.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClaimSummaryModel implements Serializable {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("ClaimedAmount")
    @Expose
    private String claimedAmount;
    @SerializedName("ApprovedAmount")
    @Expose
    private String approvedAmount;
    @SerializedName("RejectedAmount")
    @Expose
    private String rejectedAmount;
    @SerializedName("AdvancePaid")
    @Expose
    private String advancePaid;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public String getAdvancePaid() {
        return advancePaid;
    }

    public void setAdvancePaid(String advancePaid) {
        this.advancePaid = advancePaid;
    }

}
