package com.htistelecom.htisinhouse.utilities;


public class Constants {






    public static final String WFMS_DOMAIN_VERIFICATION_API = "Account/DomainVerification";


    public static final String API_LOGIN_WITH_EMAIL = "Account/LoginWithEmail";
    public static final String API_LOGIN_WITH_PHONE = "Account/LoginWithOtp";
    public static final String API_LOGIN_WITH_GETOTP = "Account/LoginGetOtp";
    public static final String API_SIGN_UP = "Account/SignUp";
    public static final String API_TASKASSIGNED_LIST = "service/TaskAssignedList";
    public static final String API_MARKETING_LIST = "service/Marketing_listWithData";


    public static final String API_TASKCOMPLETED_LIST = "service/TaskCompletedList";
    public static final String API_LAT_LONG = "ActionService/LatLongTxtFile";
    public static final String API_LIST_OF_PROJECT = "ActionService/Projects";
    public static final String API_LIST_OF_SITES = "ActionService/Sites";
    public static final String API_SITESTART_STATUS = "Service/SiteStartStatus";
    public static final String API_ATTENDANCE_MONTHLY = "ActionService/attendanceMonthly";
    public static final String API_SITE_STATUS = "ActionService/checklist";
    public static final String API_STATUS_HEAD = "ActionService/statusHeadList";
    public static final String API_START_DAY = "service/startDay";
    public static final String API_START_DAY_STATUS = "service/SiteEndStatus";
    public static final String API_SIDE_STATUS_UPDATE = "service/siteStatusUpdate";
    public static final String API_ClAIM_LIST = "service/ClaimList";
    public static final String API_ClAIM_SUBMIT = "Service/claimSubmit";
    public static final String API_PROFILE_IMAGE_UPLOAD = "Service/profileImageUpload";
    public static final String API_TRANSPORT_MODE_LIST = "Service/transportModeList";
    public static final String API_ADVANCE_CLAIM = "Service/GetAdvanceByEmpId";
    public static final String API_ADVANCE_CLAIM_ACCEPT_REJECT = "Service/AdvanceAcceptReject";
    public static final String API_CLAIM_SUMMARY = "Service/ClaimSummary";
    public static final String API_LEAVES_TYPE = "Service/LeavesType";
    public static final String API_ATTENDANCE_TYPE = "Service/AttendanceType";
    public static final String API_LEAVE_APPLY = "Service/LeaveApply";
    public static final String API_LEAVE_DISPLAY = "Service/LeaveTypeDisplay";
    public static final String API_SALARY_SLIP = "Service/SalarySlip";
    public static final String API_FORGOT_PASSWORD = "Account/ForgetPassword";
    public static final String API_CHANGE_PASSWORD = "Account/ChangePassword";
    public static final String API_NOTIFICATION = "Service/NotificationDisplay";
    public static final String API_START_DAY_MANUAL = "Service/StartdayManual";
    public static final String API_ACTIVITY_LIST = "Service/TaskAssignedActivityList";
    public static final String API_UPDATE_STATUS_ACTIVITY = "Service/_UploadedActivity";
    public static final String API_END_DAY = "Service/EmpEndDay";
    public static final String API_MARKETING_TASK_DETAILS = "Service/Marketing_list";
    public static final String API_CITY_DETAILS = "Service/CityData";
    public static final String API_MARKETING_TASK = "Service/MarketingtaskSave";
    public static final String START_DAY_INFO_API = "service/startDayInfo";
    public static final String API_CHANGE_EMAIL = "Account/ChangeEmail";
    public static final String API_CHANGE_PHONE = "Account/ChangeContactNumber";
    public static final String API_HOME_MESSAGE = "ActionService/HrNewsUpdate";
    public static final String API_EMP_CODE_VERIFY = "Account/EmpVerifiSignup";
    public static final String API_MARKETING_OFFICE = "Service/OfficeAccept";
    public static final String API_MARKETING_TRAVEL = "Service/TravelAccept";

    //Documents
    public static final String API_REQUESTED_DOCUMENT_LIST_OFFICE = "ActionService/DocumentsTypeList";
    public static final String API_REQUEST_DOCUMENT_FORM="ActionService/RequestDocuments";
    public static final String API_DOCUMENT_SEND_TO="ActionService/EmpDepList";
    public static final String API_DOCUMENT_DATA_RECEIVE="ActionService/UploadedDocumentsList";



    //Image Path
    public static final String CLAIM_IMAGES = "http://wfmapi.htistelecom.in/Uploads/claims/";
    public static final String PROFILE_IMAGE_PATH = "http://wfmapi.htistelecom.in/Uploads/profileImages/";
    public static final String ACTIVITY_IMAGE_PATH = "http://wfmapi.htistelecom.in/Uploads/ActivityImages/";


    public static final String CAMERA_METHOD = "Camera";
    public static final String GALLERY_METHOD = "Gallery";
    public static final String PROFILE_METHOD_CAMERA = "ProfileImageCamera";
    public static final String PROFILE_METHOD_GALLERY = "ProfileImageGallery";

    // all String use in app
    public static final String LOCATION_PERMISSION = "Location_permission_allow",
            DEVICE_IEMI = "DeviceIMEI";

    public static final int FOR_LOGIN = 100;
    public static final int FOR_FORGOT_PASSWORD = 101;
    public static final int FOR_LOGIN_PHONE = 102;
    public static final int FOR_VERIFY_OTP = 103;
    public static final int FOR_RESEND_OTP = 104;
    public static final int FOR_GET_OTP = 105;
    public static final int FOR_SIGN_UP = 106;

    public static final int FOR_ATTENDANCE = 107;
    public static final int FOR_CLAIMS_SUMMARY = 108;
    public static final int FOR_CLAIM_ACCEPT_REJECT = 109;


    public static final int FOR_SALARY = 150;
    public static final int FOR_ADVANCE_CLAIM = 151;
    public static final int FOR_CHANGE_PASSWORD = 152;
    public static final int FOR_NOTIFICATION = 153;


    public static final int FOR_MARKETING_LIST = 1001;
    public static final int FOR_TASKS_LIST = 1002;
    public static final int FOR_END_DAY = 1003;

    public static final int FOR_START_DAY = 1004;
    public static final int FOR_START_DAY_INFO = 1005;


    public static final int FOR_DISPLAY_LEAVE = 154;
    public static final int FOR_LEAVE_TYPE = 155;
    public static final int FOR_ATTENDENCE_TYPE = 156;
    public static final int FOR_LEAVE_APPLY = 157;

    public static final int FOR_COMPETED_TASK = 158;
    public static final int FOR_PROJECT_LIST = 159;
    public static final int FOR_START_DAY_MANUAL = 160;
    public static final int FOR_CHECK_LIST = 161;
    public static final int FOR_SITE_LIST = 162;
    public static final int FOR_MARKETING_DATA_LIST = 163;
    public static final int FOR_CITY_LIST = 164;
    public static final int FOR_MARKETIN_TASK = 165;
    public static final int FOR_CHANGE_EMAIL = 166;
    public static final int FOR_CHANGE_PHONE = 167;
    public static final int FOR_HOME_MESSAGE = 168;
    public static final int FOR_EMP_CODE_VERIFY = 169;
    public static final int FOR_MARKETING_OFFICE=170;
    public static final int FOR_MARKETING_TRAVEL=171;



    //Documents
    public static final int FOR_DOCUMENTS=174;
    public static final int FOR_DOCUMENT_TYPE_OFFICE=175;
    public static final int FOR_DOCUMENT_TYPE_UPLOAD=176;
    public static final int FOR_DOCUMENT_SEND_TO=177;
    public static final int FOR_REQUEST_DOCUMENT_FORM=178;
    public static final int FOR_UPLOAD_DOCUMENT_IMAGES_FORM=179;
    public static final int FOR_UPLOAD_DOCUMENT_FORM=180;


    //FOR_LEAVE_MGMT

    public static final int FOR_LEAVE=181;
    public static final int FOR_OUTDOOR=182;


    public static String IsUrlVerified="IsUrlVerified";

    public static String IsEmailVerified="IsEmailVerified";
    public static String IsLogin="IsLogin";
}