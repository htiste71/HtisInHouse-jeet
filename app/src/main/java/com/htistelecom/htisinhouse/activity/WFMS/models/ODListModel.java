package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ODListModel {
    @SerializedName("ODRequestId")
    @Expose
    private String oDRequestId;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("ApproverRemarks")
    @Expose
    private String approverRemarks;
    @SerializedName("Approver")
    @Expose
    private String approver;
    @SerializedName("ODRequestStatus")
    @Expose
    private String oDRequestStatus;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("UptoDate")
    @Expose
    private String uptoDate;
    @SerializedName("ODType")
    @Expose
    private String oDType;
    @SerializedName("IsNightShift")
    @Expose
    private String isNightShift;
    @SerializedName("FromTime")
    @Expose
    private String fromTime;
    @SerializedName("UptoTime")
    @Expose
    private String uptoTime;
    @SerializedName("ODPurpose")
    @Expose
    private String oDPurpose;

    public String getODRequestId() {
        return oDRequestId;
    }

    public void setODRequestId(String oDRequestId) {
        this.oDRequestId = oDRequestId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getApproverRemarks() {
        return approverRemarks;
    }

    public void setApproverRemarks(String approverRemarks) {
        this.approverRemarks = approverRemarks;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getODRequestStatus() {
        return oDRequestStatus;
    }

    public void setODRequestStatus(String oDRequestStatus) {
        this.oDRequestStatus = oDRequestStatus;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getUptoDate() {
        return uptoDate;
    }

    public void setUptoDate(String uptoDate) {
        this.uptoDate = uptoDate;
    }

    public String getODType() {
        return oDType;
    }

    public void setODType(String oDType) {
        this.oDType = oDType;
    }

    public Object getIsNightShift() {
        return isNightShift;
    }

    public void setIsNightShift(String isNightShift) {
        this.isNightShift = isNightShift;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getUptoTime() {
        return uptoTime;
    }

    public void setUptoTime(String uptoTime) {
        this.uptoTime = uptoTime;
    }

    public String getODPurpose() {
        return oDPurpose;
    }

    public void setODPurpose(String oDPurpose) {
        this.oDPurpose = oDPurpose;
    }

}
