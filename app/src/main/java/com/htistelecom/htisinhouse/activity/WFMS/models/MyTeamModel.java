package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyTeamModel implements Serializable {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("EmpCode")
    @Expose
    private String empCode;
    @SerializedName("CheckInTime")
    @Expose
    private String checkInTime;
    @SerializedName("CheckInLocation")
    @Expose
    private String checkInLocation;
    @SerializedName("CheckOutTime")
    @Expose
    private String checkOutTime;
    @SerializedName("CheckOutLocation")
    @Expose
    private String checkOutLocation;
    @SerializedName("Tasks")
    @Expose
    private String tasks;
    @SerializedName("DomainId")
    @Expose
    private Object domainId;
    @SerializedName("DeviceIMEI")
    @Expose
    private Object deviceIMEI;
    @SerializedName("DeviceToken")
    @Expose
    private Object deviceToken;
    @SerializedName("EmpToken")
    @Expose
    private Object empToken;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("EmpDesignation")
    @Expose
    private String empDesignation;
    @SerializedName("EmpDepartment")
    @Expose
    private String empDepartment;
    @SerializedName("EmpPassword")
    @Expose
    private Object empPassword;
    @SerializedName("EmpEmail")
    @Expose
    private Object empEmail;
    @SerializedName("EmpMobileNo")
    @Expose
    private Object empMobileNo;
    @SerializedName("EmpLoginCategory")
    @Expose
    private Object empLoginCategory;
    @SerializedName("EmpDOJ")
    @Expose
    private Object empDOJ;
    @SerializedName("EmpImg")
    @Expose
    private String empImg;
    @SerializedName("EmpImgPath")
    @Expose
    private String empImgPath;
    @SerializedName("EmpLevelId")
    @Expose
    private Object empLevelId;
    @SerializedName("EmpNewPassword")
    @Expose
    private Object empNewPassword;
    @SerializedName("AssignedTasks")
    @Expose
    private Object assignedTasks;
    @SerializedName("CompletedTasks")
    @Expose
    private Object completedTasks;
    @SerializedName("EmpReportingManager")
    @Expose
    private String empReportingManager;
    @SerializedName("EmpClaims")
    @Expose
    private String empClaims;
    @SerializedName("EmpPunchCity")
    @Expose
    private String empPunchCity;

    @SerializedName("Latitude")
    @Expose
    private String latitude;

    @SerializedName("Longitude")
    @Expose
    private String longitude;

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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public Object getDomainId() {
        return domainId;
    }

    public void setDomainId(Object domainId) {
        this.domainId = domainId;
    }

    public Object getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(Object deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Object getEmpToken() {
        return empToken;
    }

    public void setEmpToken(Object empToken) {
        this.empToken = empToken;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = empDesignation;
    }

    public String getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(String empDepartment) {
        this.empDepartment = empDepartment;
    }

    public Object getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(Object empPassword) {
        this.empPassword = empPassword;
    }

    public Object getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(Object empEmail) {
        this.empEmail = empEmail;
    }

    public Object getEmpMobileNo() {
        return empMobileNo;
    }

    public void setEmpMobileNo(Object empMobileNo) {
        this.empMobileNo = empMobileNo;
    }

    public Object getEmpLoginCategory() {
        return empLoginCategory;
    }

    public void setEmpLoginCategory(Object empLoginCategory) {
        this.empLoginCategory = empLoginCategory;
    }

    public Object getEmpDOJ() {
        return empDOJ;
    }

    public void setEmpDOJ(Object empDOJ) {
        this.empDOJ = empDOJ;
    }

    public String getEmpImg() {
        return empImg;
    }

    public void setEmpImg(String empImg) {
        this.empImg = empImg;
    }

    public String getEmpImgPath() {
        return empImgPath;
    }

    public void setEmpImgPath(String empImgPath) {
        this.empImgPath = empImgPath;
    }

    public Object getEmpLevelId() {
        return empLevelId;
    }

    public void setEmpLevelId(Object empLevelId) {
        this.empLevelId = empLevelId;
    }

    public Object getEmpNewPassword() {
        return empNewPassword;
    }

    public void setEmpNewPassword(Object empNewPassword) {
        this.empNewPassword = empNewPassword;
    }

    public Object getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Object assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public Object getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Object completedTasks) {
        this.completedTasks = completedTasks;
    }

    public String getEmpReportingManager() {
        return empReportingManager;
    }

    public void setEmpReportingManager(String empReportingManager) {
        this.empReportingManager = empReportingManager;
    }

    public String getEmpClaims() {
        return empClaims;
    }

    public void setEmpClaims(String empClaims) {
        this.empClaims = empClaims;
    }

    public String getEmpPunchCity() {
        return empPunchCity;
    }

    public void setEmpPunchCity(String empPunchCity) {
        this.empPunchCity = empPunchCity;
    }
}
