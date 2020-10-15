package com.htistelecom.htisinhouse.retrofit;


import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS;

import com.htistelecom.htisinhouse.utilities.Constants;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


public interface ApiInterface {
    //get the all task
    //@Headers("Content-Type: application/json")




    // this is all new code



    @Headers("Content-Type: application/json")
    @POST(Constants.WFMS_DOMAIN_VERIFICATION_API)
    Call<String> methodVerifyDomainWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_EMAIL_VERIFICATION_API)
    Call<String> methodEmailVerificationWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_LOGIN_API)
    Call<String> methodLoginWFMS(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_FORGOT_PASSWORD_API)
    Call<String> methodForgotPasswordWFMS(@Body String params);



    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_PROFILE_API)
    Call<String> methodProfileWFMS(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_CHANGE_PASSWORD_API)
    Call<String> methodChangePasswordWFMS(@Body String params);


    @GET(ConstantsWFMS.WFMS_PROJECT_LIST_API)
    Call<String> methodProjectListWFMS();

    @GET(ConstantsWFMS.WFMS_DEPARTMENT_LIST_API)
    Call<String> methodDepartmentListWFMS();

    @GET(ConstantsWFMS.WFMS_FILTER_TYPE_API)
    Call<String> methodFilterTypeWFMS();


    @GET(ConstantsWFMS.WFMS_SELECT_TYPE_TAP_LOCATION_API)
    Call<String> methodSelectTypeTapLocationWFMS();

    @GET(ConstantsWFMS.WFMS_INDUSTRY_TAP_LOCATION_API)
    Call<String> methodSelectIndustryTapLocationWFMS();

    @GET(ConstantsWFMS.WFMS_TASK_STATUS_LIST_API)
    Call<String> methodTaskStatusListWFMS();

    @GET(ConstantsWFMS.WFMS_LEAVE_DAY_TYPE_API)
    Call<String> methodLeaveDayTypeWFMS();

    @GET(ConstantsWFMS.WFMS_TRANSPORT_MODE_API)
    Call<String> methodTransportModeWFMS();

    @GET(ConstantsWFMS.WFMS_DOCUMENT_TYPE_LIST_API)
    Call<String> methodDocumentTypeWFMS();






    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_ACTIVITY_LIST_API)
    Call<String> methodActivityListWFMS(@Body String params);
    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_ADD_TASK_API)
    Call<String> methodAddTaskWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_SITE_LIST_API)
    Call<String> methodSiteListWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_SUBMIT_TAP_LOCATION_API)
    Call<String> methodSubmitTapLocationWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_MY_TASK_LIST_API)
    Call<String> methodMyTaskListWFMS(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_MY_TASK_LIST_NEW_API)
    Call<String> methodMyTaskListNewWFMS(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_START_TASK_API)
    Call<String> methodStartTaskWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_USER_FEEDBACK_API)
    Call<String> methodUserFeedbackWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_COMP_OFF_LIST_API)
    Call<String> methodCompOffListWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_APPLY_COMP_OFF_API)
    Call<String> methodApplyCompOffListWFMS(@Body String params);

    @Multipart
    @POST(ConstantsWFMS.WFMS_TASK_STATUS_API)
    Call<String> methodTaskStatusWFMS(@Part MultipartBody.Part file, @Part("TaskId") RequestBody taskId, @Part("EmpId") RequestBody empId, @Part("ActivityId") RequestBody activityId, @Part("Status") RequestBody status);

    @Multipart
    @POST(ConstantsWFMS.WFMS_NEW_TASK_STATUS_API)
    Call<String> methodNewTaskStatusWFMS(@Part MultipartBody.Part file, @Part("TaskId") RequestBody taskId, @Part("EmpId") RequestBody empId, @Part("ActivityId") RequestBody activityId, @Part("Status") RequestBody status, @Part("SubActivityId") RequestBody subActivityId);



    @Multipart
    @POST(ConstantsWFMS.WFMS_PROFILE_IMAGE_API)
    Call<String> methodProfileImageWFMS(@Part MultipartBody.Part file,@Part("EmpId") RequestBody empId);

    @Multipart
    @POST(ConstantsWFMS.WFMS_LOG_REPORT_API)
    Call<Object> methodUploadLogFileWFMS(@Part MultipartBody.Part fileToUpload,@Part("Data") RequestBody id );


    @Multipart
    @POST(ConstantsWFMS.WFMS_CLAIM_SUBMIT_API)
    Call<String> methodAddClaimWFMS(@Part MultipartBody.Part[] fileToUpload,@Part("ClaimData") RequestBody id );

    @Multipart
    @POST(ConstantsWFMS.WFMS_DOCUMENT_UPLOAD_API)
    Call<String> methodUploadDocumentWFMS(@Part MultipartBody.Part[] fileToUpload,@Part("ReqDocData") RequestBody id );





    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_MY_TEAM_API)
    Call<String> methodMyTeamWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_PUNCH_IN_OUT_API)
    Call<String> methodPunchStatusWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_LEAVE_APPLY_API)
    Call<String> methodLeaveApplyWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_LEAVE_TYPE_API)
    Call<String> methodLeaveTypeWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_OUTDOOR_REQUEST_API)
    Call<String> methodOutdoorRequestWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_OUTDOOR_LIST_API)
    Call<String> methodOutdoorListWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_LEAVE_LIST_API)
    Call<String> methodLeaveListWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_ATTENDANCE_LIST_API)
    Call<String> methodAttendanceListWFMS(@Body String params);


    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_CLAIM_SUMMARY_API)
    Call<String> methodClaimSummaryWFMS(@Body String params);



    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_CLAIM_DETAIL_TASK_API)
    Call<String> methodClaimDetailTaskWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_CLAIM_DETAIL_DATE_API)
    Call<String> methodClaimDetailDateWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_REQUEST_ADVANCE_API)
    Call<String> methodRequestAdvanceWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_SHOW_ADVANCE_DETAIL_API)
    Call<String> methodShowAdvanceDetailWFMS(@Body  String params);

//    @Headers("Content-Type: application/json")
//    @POST(ConstantsWFMS.WFMS_SALARY_API)
//    Call<String> methodSalaryWFMS(@Body  String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_DOCUMENTS_API)
    Call<String> methodDocumentsWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_REQUEST_DOCUMENT_API)
    Call<String> methodRequestDocumentWFMS(@Body  String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_SALARY_SLIP_API)
    Call<String> methodSalarySlipWFMS(@Body  String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_MEETING_STATUS_API)
    Call<String> methodMeetingStatusWFMS(@Body String params);

    @Headers("Content-Type: application/json")
    @POST(ConstantsWFMS.WFMS_COMP_OFF_LEAVE_TYPE_API)
    Call<String> methodCompOffLeaveTypeWFMS(@Body String params);


















}