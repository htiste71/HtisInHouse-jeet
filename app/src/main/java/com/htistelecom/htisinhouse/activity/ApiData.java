package com.htistelecom.htisinhouse.activity;

import android.content.Context;

import com.htistelecom.htisinhouse.retrofit.MyInterface;
import com.htistelecom.htisinhouse.retrofit.RetrofitAPI;
import com.htistelecom.htisinhouse.utilities.Utilities;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.BRANCH_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUMMARY_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.COMP_OFF_LEAVE_TYPE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.COUNTRY_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.DEPARTMENT_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.LEAVE_TYPE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.MARKETING_POSITION_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROJECT_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.SELECT_INDUSTRY_TAP_LOCATION_WFMS;

public class ApiData {

    public static void getData(String params, int Type, MyInterface myInterface, Context context) {

        if (Utilities.isNetConnected(context)) {
            if (Type == LEAVE_TYPE_WFMS) {

            } else if (Type == CLAIM_SUMMARY_WFMS) {

            } else if (Type == COMP_OFF_LEAVE_TYPE_WFMS) {

            } else {
                Utilities.showDialog(context);

            }
            RetrofitAPI.callAPI(params, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void officeData(String params, int Type, MyInterface myInterface, Context context,boolean isShow) {
        if (Utilities.isNetConnected(context)) {
            if (isShow) {
                Utilities.showDialog(context);

            }  else {

            }
            RetrofitAPI.callAPI(params, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }
    }


    public static void travelData(String params, int Type, MyInterface myInterface, Context context,boolean isShow) {
        if (Utilities.isNetConnected(context)) {
            if (isShow) {
                Utilities.showDialog(context);

            }  else {

            }
            RetrofitAPI.callAPI(params, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }
    }


    public static void getDataNoAuth(String params, int Type, MyInterface myInterface, Context context) {

        if (Utilities.isNetConnected(context)) {
            Utilities.showDialog(context);

            RetrofitAPI.callAPIWithoutAuth(params, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }


    public static void getGetData(int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
            if (Type == DEPARTMENT_LIST_WFMS) {

            } else if (Type == SELECT_INDUSTRY_TAP_LOCATION_WFMS) {

            } else if (Type == BRANCH_LIST_WFMS) {

            } else
                Utilities.showDialog(context);

            RetrofitAPI.callGetAPI(Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void getGetDataSiteAdd(int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
            if (Type == COUNTRY_LIST_WFMS) {

            } else
                Utilities.showDialog(context);

            RetrofitAPI.callGetAPI(Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }


    public static void forImageData(MultipartBody.Part fileToUpload, String mData, int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
            Utilities.showDialog(context);
            RetrofitAPI.imageUpload(fileToUpload, mData, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void forTaskImage(MultipartBody.Part fileToUpload, String mData, int Type, MyInterface myInterface, Context context, boolean isShow) {
        if (Utilities.isNetConnected(context)) {
            if (isShow)
                Utilities.showDialog(context);
            RetrofitAPI.imageUpload(fileToUpload, mData, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void forImageDataArray(MultipartBody.Part[] fileToUpload, RequestBody mData, int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
            Utilities.showDialog(context);
            RetrofitAPI.imageArrayUpload(fileToUpload, mData, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void getMarketingData(int TYPE, MyInterface myInterface,Context context) {

        if (TYPE == MARKETING_POSITION_WFMS) {
            Utilities.showDialog(context);
        }


        RetrofitAPI.callGetAPI(TYPE, myInterface);


    }

}
