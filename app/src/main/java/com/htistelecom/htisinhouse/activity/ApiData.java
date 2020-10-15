package com.htistelecom.htisinhouse.activity;

import android.content.Context;

import com.htistelecom.htisinhouse.retrofit.MyInterface;
import com.htistelecom.htisinhouse.retrofit.RetrofitAPI;
import com.htistelecom.htisinhouse.utilities.Utilities;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.CLAIM_SUMMARY_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.COMP_OFF_LEAVE_TYPE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.DEPARTMENT_LIST_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.LEAVE_TYPE_WFMS;
import static com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.SELECT_INDUSTRY_TAP_LOCATION_WFMS;

public class ApiData {

    public static void getData(String params, int Type, MyInterface myInterface, Context context) {

        if (Utilities.isNetConnected(context)) {
            if (Type == LEAVE_TYPE_WFMS) {

            } else if (Type == CLAIM_SUMMARY_WFMS) {

            }
            else if(Type==COMP_OFF_LEAVE_TYPE_WFMS)
            {

            }


            else {
                Utilities.showDialog(context);

            }
            RetrofitAPI.callAPI(params, Type, myInterface);
        } else {
            Utilities.showToast(context, "Internet is not connected");
        }

    }

    public static void getGetData(int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
            if (Type == DEPARTMENT_LIST_WFMS) {

            } else if (Type == SELECT_INDUSTRY_TAP_LOCATION_WFMS) {

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
    public static void forTaskImage(MultipartBody.Part fileToUpload, String mData, int Type, MyInterface myInterface, Context context) {
        if (Utilities.isNetConnected(context)) {
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

    public static void getDataPunchOut(String params) {


            RetrofitAPI.callAPIPunchOut(params);


    }

}
