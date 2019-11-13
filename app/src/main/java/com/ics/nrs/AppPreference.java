package com.ics.nrs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AppPreference {

    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES  = "NRS";
    public static final String DEVICE_TOKEN = "token";
    public static final String LOGOUT = "logout";
    public static final String COMPLAINT_ID = "complaintId";
    public static final String PROFILE = "profile";
    public static final String EMAIL = "email";
    public static final String MOBILE_NO = "mobileNo";
    public static final String ADDRESS = "address";
    public static final String STATUS = "status";
    public static final String IS_LOGIN = "isLogin";
    public static final String CUST_NAME = "cust_name";
    public static final String CUST_MOBILENO = "cust_mobileno";
    public static final String CUST_OTP = "cust_otp";
    public static final String EMP_NAME = "emp_name";
    public static final String MOB = "mob";
    public static final String EMPOTP = "empotp";
    public static final String EMPMOB = "empmob";



    Context context;
    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static final String KEY_ID = "user_id";

    public static final String KEY_MOBILE = "user_phone";

    public static final String KEY_IMAGE = "user_image";

    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_NAME = "user_fullname";

    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_PASSWORD = "password";



    public AppPreference(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(MyPREFERENCES,0);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(MyPREFERENCES,0);
        editor2 = prefs2.edit();
    }


    public void createCustLogin(String name
            , String mobile,String otp) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(CUST_NAME, name);
        editor.putString(CUST_MOBILENO, mobile);
        editor.putString(CUST_OTP,otp);
        editor.commit();
    }


    public void customercheckLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, UserLoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
        else {
            Intent loginsucces = new Intent(context, MainActivity.class);
            context.startActivity(loginsucces);

        }
    }

    public void custlogoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, UserLoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }


    public void createLoginSession(String id, String email, String name
            , String mobile,String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, Login.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
        else {
            Intent loginsucces = new Intent(context, ComplaintList.class);
            context.startActivity(loginsucces);

        }
    }


    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }


    public void cleardatetime(){
        editor2.clear();
        editor2.commit();
    }


    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }


    public static String getEmpName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(EMP_NAME, "");
    }
    public static void setEmpName(Context context, String ename) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMP_NAME, ename);
        editor.commit();
    }


    public static String getCustName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(CUST_NAME, "");
    }
    public static void setCustName(Context context, String cname) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CUST_NAME, cname);
        editor.commit();
    }

    public static String getCustMobileno(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(CUST_MOBILENO, "");
    }
    public static void setCustMobileno(Context context, String cmobile) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CUST_MOBILENO, cmobile);
        editor.commit();
    }


    public static String getCustOtp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(CUST_OTP, "");
    }
    public static void setCustOtp(Context context, String otp) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CUST_OTP, otp);
        editor.commit();
    }


    public static String getStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(STATUS, "");
    }
    public static void setStatus(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STATUS, value);
        editor.commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(EMAIL, "");
    }
    public static void setEmail(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, value);
        editor.commit();
    }

    public static String getMobileNo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(MOBILE_NO, "");
    }
    public static void setMobileNo(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NO, value);
        editor.commit();
    }

    public static String getAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(ADDRESS, "");
    }
    public static void setAddress(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ADDRESS, value);
        editor.commit();
    }

    public static String getProfile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(PROFILE, "");
    }
    public static void setProfile(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROFILE, value);
        editor.commit();
    }


    public static String getDeviceToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(DEVICE_TOKEN, "");
    }
    public static void setDeviceToken(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_TOKEN, value);
        editor.commit();
    }

    public static String getLogout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(LOGOUT, "");
    }
    public static void setLogout(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGOUT, value);
        editor.commit();
    }

    public static String getComplaintId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(COMPLAINT_ID, "");
    }
    public static void setComplaintId(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COMPLAINT_ID, value);
        editor.commit();
    }

    public static String getMob(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(MOB, "");
    }
    public static void setMob(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOB, value);
        editor.commit();
    }

    public static String getEmpotp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(EMPOTP, "");
    }
    public static void setEmpotp(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMPOTP, value);
        editor.commit();
    }

    public static String getEmpmob(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(EMPMOB, "");
    }
    public static void setEmpmob(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMPMOB, value);
        editor.commit();
    }

}
