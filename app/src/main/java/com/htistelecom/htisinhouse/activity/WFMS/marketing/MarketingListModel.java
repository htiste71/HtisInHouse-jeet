package com.htistelecom.htisinhouse.activity.WFMS.marketing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketingListModel {

    @SerializedName("fvCompanyName")
    @Expose
    private String company_name;


    @SerializedName("fdCustReachTime")
    @Expose
    private String cust_reach_time;

    @SerializedName("fdCustOutTime")
    @Expose
    private String cust_out_time;


    @SerializedName("fvEmployeeName")
    @Expose
    private String employee_name;


    @SerializedName("PositionName")
    @Expose
    private String position_name;


    @SerializedName("EntityName")
    @Expose
    private String entity_name;

    @SerializedName("fvWebLink")
    @Expose
    private String web_link;


    @SerializedName("BusinessNature")
    @Expose
    private String business_nature;

    @SerializedName("fvCompanyOwner")
    @Expose
    private String company_owner;

    @SerializedName("fvRemarks")
    @Expose
    private String remarks;


    @SerializedName("fiAnnualTurnoverID")
    @Expose
    private String annual_turnover;


    @SerializedName("fvCompanyOwnerContact")
    @Expose
    private String owner_contact;


    @SerializedName("fvLeadGenerated")
    @Expose
    private String lead_generated;

    @SerializedName("fvEmailID")
    @Expose
    private String email_id;

    @SerializedName("StateName")
    @Expose
    private String state_name;
    @SerializedName("CityName")
    @Expose
    private String city_name;
    @SerializedName("fvAddress")
    @Expose
    private String address;
    @SerializedName("fvPhone")
    @Expose
    private String phone;
    @SerializedName("fvZipCode")
    @Expose
    private String zip_code;
    @SerializedName("fvEmplyeeId")
    @Expose
    private String emp_id;

    @SerializedName("TaskDate")
    @Expose
    private String task_date;

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCust_reach_time() {
        return cust_reach_time;
    }

    public void setCust_reach_time(String cust_reach_time) {
        this.cust_reach_time = cust_reach_time;
    }

    public String getCust_out_time() {
        return cust_out_time;
    }

    public void setCust_out_time(String cust_out_time) {
        this.cust_out_time = cust_out_time;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    public String getBusiness_nature() {
        return business_nature;
    }

    public void setBusiness_nature(String business_nature) {
        this.business_nature = business_nature;
    }

    public String getCompany_owner() {
        return company_owner;
    }

    public void setCompany_owner(String company_owner) {
        this.company_owner = company_owner;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAnnual_turnover() {
        return annual_turnover;
    }

    public void setAnnual_turnover(String annual_turnover) {
        this.annual_turnover = annual_turnover;
    }

    public String getOwner_contact() {
        return owner_contact;
    }

    public void setOwner_contact(String owner_contact) {
        this.owner_contact = owner_contact;
    }

    public String getLead_generated() {
        return lead_generated;
    }

    public void setLead_generated(String lead_generated) {
        this.lead_generated = lead_generated;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
