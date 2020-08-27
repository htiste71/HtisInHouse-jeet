package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveTypeModel {
    @SerializedName("LeaveTypeId")
    @Expose
    private String leaveTypeId;
    @SerializedName("LeaveTypeName")
    @Expose
    private String leaveTypeName;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("TotalBalance")
    @Expose
    private String totalBalance;
    @SerializedName("UnApproved")
    @Expose
    private String unApproved;
    @SerializedName("Availed")
    @Expose
    private String availed;
    @SerializedName("CurrentBalance")
    @Expose
    private String currentBalance;


    @SerializedName("CanApply")
    @Expose
    private String canApply;


    public String getCanApply() {
        return canApply;
    }

    public void setCanApply(String canApply) {
        this.canApply = canApply;

    }

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getUnApproved() {
        return unApproved;
    }

    public void setUnApproved(String unApproved) {
        this.unApproved = unApproved;
    }

    public String getAvailed() {
        return availed;
    }

    public void setAvailed(String availed) {
        this.availed = availed;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }
}
