package com.htistelecom.htisinhouse.activity.WFMS.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchListModel {

    @SerializedName("BranchId")
    @Expose
    private String branchId;
    @SerializedName("BranchCode")
    @Expose
    private String branchCode;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("BranchAddress")
    @Expose
    private String branchAddress;
    @SerializedName("BranchCountry")
    @Expose
    private String branchCountry;
    @SerializedName("BranchState")
    @Expose
    private String branchState;
    @SerializedName("BranchCity")
    @Expose
    private String branchCity;
    @SerializedName("BranchPostalCode")
    @Expose
    private String branchPostalCode;
    @SerializedName("BranchContactNo")
    @Expose
    private String branchContactNo;
    @SerializedName("BranchMobileNo")
    @Expose
    private String branchMobileNo;
    @SerializedName("BranchEmail")
    @Expose
    private String branchEmail;

    private boolean isChecked;

    private boolean isAllChecked=false;

    public boolean isAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(boolean allChecked) {
        isAllChecked = allChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchCountry() {
        return branchCountry;
    }

    public void setBranchCountry(String branchCountry) {
        this.branchCountry = branchCountry;
    }

    public String getBranchState() {
        return branchState;
    }

    public void setBranchState(String branchState) {
        this.branchState = branchState;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getBranchPostalCode() {
        return branchPostalCode;
    }

    public void setBranchPostalCode(String branchPostalCode) {
        this.branchPostalCode = branchPostalCode;
    }

    public String getBranchContactNo() {
        return branchContactNo;
    }

    public void setBranchContactNo(String branchContactNo) {
        this.branchContactNo = branchContactNo;
    }

    public String getBranchMobileNo() {
        return branchMobileNo;
    }

    public void setBranchMobileNo(String branchMobileNo) {
        this.branchMobileNo = branchMobileNo;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

}
