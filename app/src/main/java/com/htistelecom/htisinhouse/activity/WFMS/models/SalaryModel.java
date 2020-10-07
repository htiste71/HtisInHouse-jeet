package com.htistelecom.htisinhouse.activity.WFMS.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalaryModel {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("ChargeId")
    @Expose
    private String chargeId;
    @SerializedName("Charges")
    @Expose
    private String charges;
    @SerializedName("ChargeAmount")
    @Expose
    private String chargeAmount;
    @SerializedName("CTCType")
    @Expose
    private String cTCType;
    @SerializedName("Allowance")
    @Expose
    private String allowance;
    @SerializedName("Deduction")
    @Expose
    private String deduction;
    @SerializedName("NetPayable")
    @Expose
    private String netPayable;
    @SerializedName("PayDays")
    @Expose
    private String payDays;
    @SerializedName("MonthYear")
    @Expose
    private String monthYear;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getCTCType() {
        return cTCType;
    }

    public void setCTCType(String cTCType) {
        this.cTCType = cTCType;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(String netPayable) {
        this.netPayable = netPayable;
    }

    public String getPayDays() {
        return payDays;
    }

    public void setPayDays(String payDays) {
        this.payDays = payDays;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
