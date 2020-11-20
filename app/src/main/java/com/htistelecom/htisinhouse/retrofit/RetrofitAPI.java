package com.htistelecom.htisinhouse.retrofit;

import android.util.Log;

import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS;
import com.htistelecom.htisinhouse.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.ACTIVITY_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.APPLY_COMP_OFF_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.APPLY_LEAVE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.ATTENDANCE_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_DETAIL_DATE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_DETAIL_TASK_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUBMIT_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUMMARY_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.COMP_OFF_STATUS_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.DEPARTMENT_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.DOCUMENTS_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.LEAVE_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.LEAVE_TYPE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MEETING_STATUS_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MY_TEAM_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.NEW_TASK_STATUS_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.OUTDOOR_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.OUTDOOR_REQUEST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROFILE_IMAGE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROJECT_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PUNCH_IN_OUT_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.REQUEST_ADVANCE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.REQUEST_DOCUMENT_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.SHOW_ADVANCE_DETAIL_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.TASK_STATUS_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.UPLOAD_DOCUMENT_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.USER_FEEDBACK_WFMS;

public class RetrofitAPI {

    static MyInterface commonInterface;

    public static void callAPI(String params, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;

        ApiInterface api = RetrofitClients.createService(ApiInterface.class);

        Call<String> call = null;

        if (TYPE == ConstantsWFMS.DOMAIN_VERIFICATION_WFMS) {
            call = api.methodVerifyDomainWFMS(params);

        } else if (TYPE == ConstantsWFMS.EMAIL_VERIFICATION_WFMS) {
            call = api.methodEmailVerificationWFMS(params);

        } else if (TYPE == ConstantsWFMS.LOGIN_WFMS) {
            call = api.methodLoginWFMS(params);

        } else if (TYPE == ConstantsWFMS.FORGOT_PASSWORD_WFMS) {
            call = api.methodForgotPasswordWFMS(params);

        } else if (TYPE == ConstantsWFMS.PROFILE_WFMS) {
            call = api.methodProfileWFMS(params);

        } else if (TYPE == ConstantsWFMS.CHANGE_PASSWORD_WFMS) {
            call = api.methodChangePasswordWFMS(params);

        } else if (TYPE == ConstantsWFMS.ADD_TASK_WFMS) {
            call = api.methodAddTaskWFMS(params);

        } else if (TYPE == ConstantsWFMS.SITE_LIST_WFMS) {
            call = api.methodSiteListWFMS(params);

        } else if (TYPE == ConstantsWFMS.SUBMIT_TAP_LOCATION_WFMS) {
            call = api.methodSubmitTapLocationWFMS(params);

        } else if (TYPE == ConstantsWFMS.MY_TASK_LIST_WFMS) {
            call = api.methodMyTaskListWFMS(params);

        }
        else if (TYPE == ConstantsWFMS.MY_TASK_LIST_NEW_WFMS) {
            call = api.methodMyTaskListNewWFMS(params);

        }
        else if (TYPE == ConstantsWFMS.START_TASK_WFMS) {
            call = api.methodStartTaskWFMS(params);

        } else if (TYPE == ACTIVITY_LIST_WFMS) {
            call = api.methodActivityListWFMS(params);
        } else if (TYPE == MY_TEAM_WFMS) {
            call = api.methodMyTeamWFMS(params);
        }
        else if (TYPE == PUNCH_IN_OUT_WFMS) {
            call = api.methodPunchStatusWFMS(params);
        }
        else if (TYPE == MY_TEAM_WFMS) {
            call = api.methodMyTeamWFMS(params);
        }
//        else if (TYPE == PUNCH_IN_OUT_WFMS) {
//            call = api.methodPunchStatusWFMS(params);
//        }

        else if (TYPE == APPLY_LEAVE_WFMS) {
            call = api.methodLeaveApplyWFMS(params);
        } else if (TYPE == LEAVE_TYPE_WFMS) {
            call = api.methodLeaveTypeWFMS(params);
        } else if (TYPE == OUTDOOR_REQUEST_WFMS) {
            call = api.methodOutdoorRequestWFMS(params);
        } else if (TYPE == OUTDOOR_LIST_WFMS) {
            call = api.methodOutdoorListWFMS(params);
        } else if (TYPE == LEAVE_LIST_WFMS) {
            call = api.methodLeaveListWFMS(params);
        } else if (TYPE == ATTENDANCE_LIST_WFMS) {
            call = api.methodAttendanceListWFMS(params);
        } else if (TYPE == CLAIM_SUMMARY_WFMS) {
            call = api.methodClaimSummaryWFMS(params);
        }
        else if (TYPE == CLAIM_DETAIL_TASK_WFMS) {
            call = api.methodClaimDetailTaskWFMS(params);
        } else if (TYPE == CLAIM_DETAIL_DATE_WFMS) {
            call = api.methodClaimDetailDateWFMS(params);
        }
        else if (TYPE ==REQUEST_ADVANCE_WFMS) {
            call = api.methodRequestAdvanceWFMS(params);
        }
        else if (TYPE == SHOW_ADVANCE_DETAIL_WFMS) {
            call = api.methodShowAdvanceDetailWFMS(params);
        }

        else if (TYPE == ConstantsWFMS.SALARY_SLIP_WFMS) {
            call = api.methodSalarySlipWFMS(params);
        }
        else if (TYPE == DOCUMENTS_WFMS) {
            call = api.methodDocumentsWFMS(params);
        }

        else if (TYPE == REQUEST_DOCUMENT_WFMS) {
            call = api.methodRequestDocumentWFMS(params);
        }
        else if (TYPE == MEETING_STATUS_WFMS) {
            call = api.methodMeetingStatusWFMS(params);
        }
        else if (TYPE == USER_FEEDBACK_WFMS) {
            call = api.methodUserFeedbackWFMS(params);
        }

        else if (TYPE == COMP_OFF_STATUS_LIST_WFMS) {
            call = api.methodCompOffListWFMS(params);
        }
        else if (TYPE == APPLY_COMP_OFF_WFMS) {
            call = api.methodApplyCompOffListWFMS(params);
        }
        else if (TYPE == ConstantsWFMS.COMP_OFF_LEAVE_TYPE_WFMS) {
            call = api.methodCompOffLeaveTypeWFMS(params);
        }
        else if (TYPE == ConstantsWFMS.SEARCH_TEAM_LIST_WFMS) {
            call = api.methodSearchTeamListWFMS(params);
        }
        callRetrofit(call, TYPE);

    }


    public static void callGetAPI(int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;
        ApiInterface api = RetrofitClients.createService(ApiInterface.class);
        Call<String> call = null;
        if (TYPE == PROJECT_LIST_WFMS) {
            call = api.methodProjectListWFMS();
        } else if (TYPE == ConstantsWFMS.SELECT_TYPE_TAP_LOCATION_WFMS) {
            call = api.methodSelectTypeTapLocationWFMS();

        } else if (TYPE == ConstantsWFMS.SELECT_INDUSTRY_TAP_LOCATION_WFMS) {
            call = api.methodSelectIndustryTapLocationWFMS();
        } else if (TYPE == ConstantsWFMS.TASK_STATUS_LIST_WFMS) {
            call = api.methodTaskStatusListWFMS();
        } else if (TYPE == ConstantsWFMS.LEAVE_TYPE_DAY_WFMS) {
            call = api.methodLeaveDayTypeWFMS();
        }
        else if (TYPE == ConstantsWFMS.TRANSPORT_MODE_WFMS) {
            call = api.methodTransportModeWFMS();
        }
        else if (TYPE == ConstantsWFMS.DOCUMENT_TYPE_WFMS) {
            call = api.methodDocumentTypeWFMS();
        }

        else if (TYPE == DEPARTMENT_LIST_WFMS) {
            call = api.methodDepartmentListWFMS();
        }

        else if (TYPE == ConstantsWFMS.FILTER_TYPE_WFMS) {
            call = api.methodFilterTypeWFMS();
        }
        else if (TYPE == ConstantsWFMS.BRANCH_LIST_WFMS) {
            call = api.methodBranchListWFMS();
        }

        callRetrofit(call, TYPE);

    }


    public static void imageUpload(MultipartBody.Part fileToUpload, String mData, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;

        ApiInterface api = RetrofitClients.createService(ApiInterface.class);

        Call<String> call = null;
        if (TYPE == TASK_STATUS_WFMS) {
            try {

                JSONObject jsonObject = new JSONObject(mData);
                RequestBody mTaskId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("TaskId"));
                RequestBody mEmpId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("EmpId"));
                RequestBody mStatus = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("Status"));
                RequestBody mActivityId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("ActivityId"));
                call = api.methodTaskStatusWFMS(fileToUpload, mTaskId, mEmpId, mActivityId, mStatus);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(TYPE==PROFILE_IMAGE_WFMS)
        {
            RequestBody mEmpId = RequestBody.create(MediaType.parse("text/plain"),mData);
            call = api.methodProfileImageWFMS(fileToUpload, mEmpId);

        }

       else if (TYPE == NEW_TASK_STATUS_WFMS) {
            try {

                JSONObject jsonObject = new JSONObject(mData);
                RequestBody mTaskId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("TaskId"));
                RequestBody mEmpId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("EmpId"));
                RequestBody mStatus = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("Status"));
                RequestBody mActivityId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("ActivityId"));
                RequestBody mSubActivityId = RequestBody.create(MediaType.parse("text/plain"), jsonObject.getString("SubActivityId"));

                call = api.methodNewTaskStatusWFMS(fileToUpload, mTaskId, mEmpId, mActivityId, mStatus,mSubActivityId);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        callRetrofit(call, TYPE);
    }


    public static void imageArrayUpload(MultipartBody.Part[] fileToUpload, RequestBody mData, int TYPE, MyInterface myInterface) {
        commonInterface = myInterface;

        ApiInterface api = RetrofitClients.createService(ApiInterface.class);

        Call<String> call = null;
        if (TYPE == CLAIM_SUBMIT_WFMS) {

            call = api.methodAddClaimWFMS(fileToUpload, mData);

        }
        else if(TYPE==UPLOAD_DOCUMENT_WFMS)

        {
            call = api.methodUploadDocumentWFMS(fileToUpload, mData);

        }
        callRetrofit(call, TYPE);
    }

    public static <T> void callRetrofit(Call call, final int TYPE) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                commonInterface.sendResponse(response, TYPE);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });
    }


    public static void callAPIPunchOut(String params) {

        ApiInterface api = RetrofitClients.createService(ApiInterface.class);

        Call<String> call = null;
            call = api.methodPunchStatusWFMS(params);
        callRetrofitPunchOut(call);


    }

    public static <T> void callRetrofitPunchOut(Call call) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.e("Response",response.toString());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("Re", "");

            }
        });
    }

}