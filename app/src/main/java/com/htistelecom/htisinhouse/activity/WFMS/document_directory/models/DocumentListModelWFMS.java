package com.htistelecom.htisinhouse.activity.WFMS.document_directory.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentListModelWFMS {
    @SerializedName("DocRequestId")
    @Expose
    private String docRequestId;
    @SerializedName("RequestType")
    @Expose
    private String requestType;
    @SerializedName("DocRequestDate")
    @Expose
    private String docRequestDate;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("DocTypeId")
    @Expose
    private String docTypeId;
    @SerializedName("DocType")
    @Expose
    private String docType;
    @SerializedName("EmpRemarks")
    @Expose
    private String empRemarks;
    @SerializedName("ReqApprovedBy")
    @Expose
    private String reqApprovedBy;
    @SerializedName("ReqToEmpId")
    @Expose
    private String reqToEmpId;
    @SerializedName("ReqToEmp")
    @Expose
    private String reqToEmp;
    @SerializedName("ReqToDept")
    @Expose
    private String reqToDept;
    @SerializedName("ReqStatusId")
    @Expose
    private String reqStatusId;
    @SerializedName("ReqStatus")
    @Expose
    private String reqStatus;

    public String getDocRequestId() {
        return docRequestId;
    }

    public void setDocRequestId(String docRequestId) {
        this.docRequestId = docRequestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDocRequestDate() {
        return docRequestDate;
    }

    public void setDocRequestDate(String docRequestDate) {
        this.docRequestDate = docRequestDate;
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

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getEmpRemarks() {
        return empRemarks;
    }

    public void setEmpRemarks(String empRemarks) {
        this.empRemarks = empRemarks;
    }

    public String getReqApprovedBy() {
        return reqApprovedBy;
    }

    public void setReqApprovedBy(String reqApprovedBy) {
        this.reqApprovedBy = reqApprovedBy;
    }

    public String getReqToEmpId() {
        return reqToEmpId;
    }

    public void setReqToEmpId(String reqToEmpId) {
        this.reqToEmpId = reqToEmpId;
    }

    public String getReqToEmp() {
        return reqToEmp;
    }

    public void setReqToEmp(String reqToEmp) {
        this.reqToEmp = reqToEmp;
    }

    public String getReqToDept() {
        return reqToDept;
    }

    public void setReqToDept(String reqToDept) {
        this.reqToDept = reqToDept;
    }

    public String getReqStatusId() {
        return reqStatusId;
    }

    public void setReqStatusId(String reqStatusId) {
        this.reqStatusId = reqStatusId;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }
}
