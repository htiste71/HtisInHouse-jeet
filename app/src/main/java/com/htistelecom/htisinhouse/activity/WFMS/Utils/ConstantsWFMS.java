package com.htistelecom.htisinhouse.activity.WFMS.Utils;

import org.jetbrains.annotations.Nullable;

public class ConstantsWFMS {

    //public static String WFMSCOMPANY_DOMAIN = "";
    public static String WFMS_BASE_URL_STATIC = "http://wfmapi.htistelecom.in/api/";

    // public static String WFMS_BASE_URL = WFMSCOMPANY_DOMAIN + ".htistelecom.in/api/";
    public static final String WFMS_DOMAIN_VERIFICATION_API = "Account/DomainVerification";
    public static final String WFMS_EMAIL_VERIFICATION_API = "Account/EmpEmailVerification";
    public static final String WFMS_LOGIN_API = "Account/EmpEmailLogin";
    public static final String WFMS_PROFILE_API = "Service/EmpProfile";
    public static final String WFMS_FORGOT_PASSWORD_API = "Account/EmpForgotPassword";
    public static final String WFMS_CHANGE_PASSWORD_API = "Account/EmpChangePassword";
    public static final String WFMS_PROJECT_LIST_API = "Service/ProjectList";
    public static final String WFMS_ACTIVITY_LIST_API = "Service/ActivityList";
    public static final String WFMS_SITE_LIST_API = "Service/SiteList";
    public static final String WFMS_ADD_TASK_API = "Service/AddNewTask";
    public static final String WFMS_SELECT_TYPE_TAP_LOCATION_API = "Service/TapLocationTypeList";
    public static final String WFMS_INDUSTRY_TAP_LOCATION_API  ="Service/CompanyNatureList";
    public static final String WFMS_SUBMIT_TAP_LOCATION_API  ="Service/TapLocationSaveUpdate";
    public static final String WFMS_MY_TASK_LIST_API  ="Service/TaskAssignedList";
    public static final String WFMS_START_TASK_API  ="Service/TaskStart";
    public static final String WFMS_TASK_STATUS_API="Service/TaskStatusUpdate";
    public static final String WFMS_TASK_STATUS_LIST_API="Service/SiteEndStatus";
    public static final String WFMS_MY_TEAM_API="Service/EmpTeamList";
    public static final String WFMS_PUNCH_IN_OUT_API="Service/EmpPunchInOut";
    public static final String WFMS_LEAVE_APPLY_API="Service/LeaveApply";
    public static final String WFMS_LEAVE_DAY_TYPE_API="Service/LeaveForDayType";
    public static final String WFMS_LEAVE_TYPE_API ="Service/LeaveTypeBalanceList";
    public static final String WFMS_OUTDOOR_REQUEST_API="Service/OutdoorAttendanceRequest";
    public static final String WFMS_OUTDOOR_LIST_API="Service/OutdoorAttendanceRequestList";
    public static final String WFMS_LEAVE_LIST_API="Service/LeaveHistory";
    public static final String WFMS_ATTENDANCE_LIST_API="Service/EmpMonthlyAttendance";
    public static final String WFMS_TRANSPORT_MODE_API="Service/ConveyanceModeList";
    public static final String WFMS_CLAIM_SUMMARY_API="Service/ClaimSummary";
    public static final String WFMS_CLAIM_SUBMIT_API="Service/ExpenseClaimSubmit";
    public static final String WFMS_CLAIM_DETAIL_TASK_API="Service/ClaimDetailsviaTaskId";
    public static final String WFMS_CLAIM_DETAIL_DATE_API="Service/ClaimDetailsviaDate";
    public static final String WFMS_REQUEST_ADVANCE_API="Service/AdvancePaymentRequest";
    public static final String WFMS_SHOW_ADVANCE_DETAIL_API="Service/AdvancePaymentRequestList";
    public static final String WFMS_PROFILE_IMAGE_API="Service/EmpProfileImgUpload";
    public static final String WFMS_DOCUMENTS_API="Service/EmpDocumentRequestList";
    public static final String WFMS_REQUEST_DOCUMENT_API = "Service/EmpDocumentRequest";;
    public static final String WFMS_DOCUMENT_TYPE_LIST_API = "Service/DocumentTypeList";;
    public static final String WFMS_DOCUMENT_UPLOAD_API = "Service/RequestedDocUpload";;
    public static final String WFMS_DEPARTMENT_LIST_API = "Service/DepartmentList";
    public static final String WFMS_SALARY_SLIP_API ="Service/EmpSalarySlip";
    public static final String WFMS_MEETING_STATUS_API  ="Service/EmpMeetingStatus";
    public static final String WFMS_LOG_REPORT_API  ="Service/DailyLatLongReportUpload";
    public static final String WFMS_FILTER_TYPE_API ="Service/MyTeamStatusList";
    public static final String WFMS_USER_FEEDBACK_API ="Service/UserFeedback";


    public static int DOMAIN_VERIFICATION_WFMS = 1;
    public static int EMAIL_VERIFICATION_WFMS = 2;
    public static int LOGIN_WFMS = 3;
    public static int PROFILE_WFMS = 4;
    public static final int FORGOT_PASSWORD_WFMS = 5;
    public static final int CHANGE_PASSWORD_WFMS = 6;


    public static final int PROJECT_LIST_WFMS = 7;
    public static final int SITE_LIST_WFMS = 8;

    public static final int ACTIVITY_LIST_WFMS = 9;
    public static final int ADD_TASK_WFMS = 10;
    public static final int SELECT_TYPE_TAP_LOCATION_WFMS = 11;
    public static final int SELECT_INDUSTRY_TAP_LOCATION_WFMS = 12;
    public static final int SUBMIT_TAP_LOCATION_WFMS = 13;
    public static final int MY_TASK_LIST_WFMS=14;
    public static final int START_TASK_WFMS = 15;
    public static final int TASK_STATUS_WFMS = 16;
    public static final int TASK_STATUS_LIST_WFMS=17;
    public static final int MY_TEAM_WFMS=18;
    public static final int PUNCH_IN_OUT_WFMS=19;
    public static final int APPLY_LEAVE_WFMS=20;
    public static final int LEAVE_TYPE_DAY_WFMS=21;
    public static final int LEAVE_TYPE_WFMS=22;
    public static final int OUTDOOR_REQUEST_WFMS=23;
    public static final int OUTDOOR_LIST_WFMS=24;
    public static final int LEAVE_LIST_WFMS=25;
    public static final int ATTENDANCE_LIST_WFMS=26;
    public static final int TRANSPORT_MODE_WFMS=27;
    public static final int DOCUMENTS_WFMS=28;
    public static final int DOCUMENT_TYPE_WFMS=29;
   // public static final int DOCUMENT_SEND_TO_WFMS=30;
    public static final int REQUEST_DOCUMENT_WFMS=31;
    public static final int UPLOAD_DOCUMENT_WFMS=32;
    public static final int CLAIM_SUMMARY_WFMS=33;
    public static final int CLAIM_SUBMIT_WFMS=34;
    public static final int CLAIM_DETAIL_TASK_WFMS=35;
    public static final int CLAIM_DETAIL_DATE_WFMS=36;
    public static final int REQUEST_ADVANCE_WFMS =37 ;
    public static final int SHOW_ADVANCE_DETAIL_WFMS=38;
    public static final int SALARY_SLIP_WFMS=39;
    public static final int PROFILE_IMAGE_WFMS=40;
    public static final int DEPARTMENT_LIST_WFMS=41;
    public static final int MEETING_STATUS_WFMS=42;
    public static final int FILTER_TYPE_WFMS=43;
    public static final int USER_FEEDBACK_WFMS=44;


    public static final String TINYDB_DOMAIN_ID = "DOMAIN_ID";
    public static final String TINYDB_EMAIL = "TINYDB_EMAIL";

    public static final String TINYDB_EMP_ID = "TINYDB_EMP_ID";
    public static final String TINYDB_EMP_CODE = "TINYDB_EMP_CODE";
    public static final String TINYDB_EMP_NAME = "TINYDB_EMP_NAME";
    public static final String TINYDB_EMP_DESIGNATION = "TINYDB_EMP_DESIGNATION";
    public static final String TINYDB_EMP_DEPT = "TINYDB_EMP_DEPT";
    public static final String TINYDB_EMP_MOBILE = "TINYDB_EMP_MOBILE";
    public static final String TINYDB_PASSWORD="TINYDB_PASSWORD";
    public static final String TINYDB_PUNCH_STATUS="TINYDB_PUNCH_STATUS";

    public static final String TINYDB_IMEI_NUMBER=" TINYDB_IMEI_NUMBER";
    public static final String TINYDB_IS_PUNCH_IN=" TINYDB_IS_PUNCH_IN";
    public static final String TINYDB_PUNCH_IN_TIME=" TINYDB_PUNCH_IN_TIME";
    public static final String TINYDB_PUNCH_OUT_TIME=" TINYDB_PUNCH_OUT_TIME";
    public static final String TINYDB_EMP_PROFILE_IMAGE="TINYDB_EMP_PROFILE_IMAGE";
    public static final String TINYDB_MEETING_STATUS="TINYDB_MEETING_STATUS";
    public static final String TINYDB_MYFILE="TINYDB_MYFILE";
    public static String TINYDB_URL_VERIFIED="TINYDB_URL_VERIFIED";

    public static String TINYDB_EMAIL_VERIFIED="TINYDB_EMAIL_VERIFIED";
    public static String TINYDB_IS_LOGIN="TINYDB_IS_LOGIN";
    @Nullable
    public static final String TINYDB_SYNC_TIME="TINYDB_SYNC_TIME";
}
