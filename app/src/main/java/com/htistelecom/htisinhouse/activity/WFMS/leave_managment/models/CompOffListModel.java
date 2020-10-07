package com.htistelecom.htisinhouse.activity.WFMS.leave_managment.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompOffListModel {

    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("CompoffId")
    @Expose
    private String compoffId;
    @SerializedName("AttendanceDayType")
    @Expose
    private String attendanceDayType;
    @SerializedName("LeaveTypeName")
    @Expose
    private String leaveTypeName;
    @SerializedName("EmpCode")
    @Expose
    private String empCode;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("DepartmentName")
    @Expose
    private String departmentName;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("AttendanceDate")
    @Expose
    private String attendanceDate;
    @SerializedName("ShiftName")
    @Expose
    private String shiftName;
    @SerializedName("ApproverRemarks")
    @Expose
    private String approverRemarks;
    @SerializedName("LeavePurpose")
    @Expose
    private String leavePurpose;
    @SerializedName("NoofDays")
    @Expose
    private String noofDays;
    @SerializedName("RespondedOn")
    @Expose
    private String respondedOn;
    @SerializedName("RequestedOn")
    @Expose
    private String requestedOn;
    @SerializedName("LeaveFrom")
    @Expose
    private String leaveFrom;
    @SerializedName("LeaveTo")
    @Expose
    private String leaveTo;
    @SerializedName("LeaveFromDayType")
    @Expose
    private String leaveFromDayType;
    @SerializedName("LeaveToDayType")
    @Expose
    private String leaveToDayType;
    @SerializedName("AppliedCompoffLeaveType")
    @Expose
    private String appliedCompoffLeaveType;
    @SerializedName("StatusId")
    @Expose
    private String statusId;
    @SerializedName("CompOffStatus")
    @Expose
    private String compOffStatus;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCompoffId() {
        return compoffId;
    }

    public void setCompoffId(String compoffId) {
        this.compoffId = compoffId;
    }

    public String getAttendanceDayType() {
        return attendanceDayType;
    }

    public void setAttendanceDayType(String attendanceDayType) {
        this.attendanceDayType = attendanceDayType;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getApproverRemarks() {
        return approverRemarks;
    }

    public void setApproverRemarks(String approverRemarks) {
        this.approverRemarks = approverRemarks;
    }

    public String getLeavePurpose() {
        return leavePurpose;
    }

    public void setLeavePurpose(String leavePurpose) {
        this.leavePurpose = leavePurpose;
    }

    public String getNoofDays() {
        return noofDays;
    }

    public void setNoofDays(String noofDays) {
        this.noofDays = noofDays;
    }

    public String getRespondedOn() {
        return respondedOn;
    }

    public void setRespondedOn(String respondedOn) {
        this.respondedOn = respondedOn;
    }

    public String getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(String requestedOn) {
        this.requestedOn = requestedOn;
    }

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

    public String getLeaveFromDayType() {
        return leaveFromDayType;
    }

    public void setLeaveFromDayType(String leaveFromDayType) {
        this.leaveFromDayType = leaveFromDayType;
    }

    public String getLeaveToDayType() {
        return leaveToDayType;
    }

    public void setLeaveToDayType(String leaveToDayType) {
        this.leaveToDayType = leaveToDayType;
    }

    public String getAppliedCompoffLeaveType() {
        return appliedCompoffLeaveType;
    }

    public void setAppliedCompoffLeaveType(String appliedCompoffLeaveType) {
        this.appliedCompoffLeaveType = appliedCompoffLeaveType;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getCompOffStatus() {
        return compOffStatus;
    }

    public void setCompOffStatus(String compOffStatus) {
        this.compOffStatus = compOffStatus;
    }

}

