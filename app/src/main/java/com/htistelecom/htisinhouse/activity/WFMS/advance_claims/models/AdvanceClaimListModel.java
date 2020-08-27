package com.htistelecom.htisinhouse.activity.WFMS.advance_claims.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvanceClaimListModel {

    @SerializedName("RequestId")
    @Expose
    private String requestId;
    @SerializedName("RequestDate")
    @Expose
    private String requestDate;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("RequestStatusId")
    @Expose
    private String requestStatusId;
    @SerializedName("RequestStatus")
    @Expose
    private String requestStatus;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequestStatusId() {
        return requestStatusId;
    }

    public void setRequestStatusId(String requestStatusId) {
        this.requestStatusId = requestStatusId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }


}
