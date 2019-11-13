package com.ics.nrs.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Admin on 17-10-2015.
 */
public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String MyPREFERENCES = "MyPrefss";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_Comp_MOBILE = "comp_Mobile";
    public static final String KEY_STATE = "state";
    public static final String KEY_OP_BAL = "opening_bal";
    public static final String KEY_TYPE = "type";
    private static final String IS_SKIPPED = "IsSlipped";
    private static final String IS_SOLVED = "IsSolved";
    private static final String WAITER_NAME = "waiter_name";
    private static final String KEY_DISCOUNT = "discount";
    private static final String KEY_OTP = "discount";
    private static final String KEY_CompCODE = "ComplainCode";
    private static final String KEY_MODEL = "model_no";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_PINCODE = "pinCode";
    private static final String KEY_SERIAL = "serial";
    private static final String KEY_ADDRESS = "address";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
        editor = pref.edit();

    }


    public void waiterName(String strName) {
        editor.putString(WAITER_NAME, strName);
        editor.commit();
    }

    public void setOtp(String strOtp) {
        editor.putString(KEY_OTP, strOtp);
        editor.commit();
    }

    public void serverLogin(String strName, String strType, String strState, String strOPBal) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_TYPE, strType);
        editor.putString(KEY_STATE, strState);
        editor.putString(KEY_OP_BAL, strOPBal);
        editor.commit();
    }

    public void serverLoginForNRS(String strName, String strMob) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_MOBILE, strMob);
        editor.commit();
    }

    public void storeComplainData(String name, String mobile_no, String address, String pincode, String selected_product, String model_no, String serial_no) {
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_Comp_MOBILE, mobile_no);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_PRODUCT, selected_product);
        editor.putString(KEY_MODEL, model_no);
        editor.putString(KEY_SERIAL, serial_no);
//        editor.putBoolean(IS_SOLVED, comp_Done);
        editor.commit();
    }

    public void stroreCompCode(String strCompCode) {
        editor.putString(KEY_CompCODE, strCompCode);
        editor.commit();
    }

    public void dieselSubsidyLogin(String strMobile) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MOBILE, strMobile);
        editor.commit();
    }

    public void setDiscount(String disAmo) {
        editor.putString(KEY_DISCOUNT, disAmo);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.commit();
    }

    public void setSkipped(boolean isLoggedIn) {
        editor.putBoolean(IS_SKIPPED, isLoggedIn);
        editor.commit();
    }

    public void isComplainSolve(String strMob, boolean isSolved) {
        editor.putString(KEY_MOBILE, strMob);
        editor.putBoolean(IS_SOLVED, isSolved);
        editor.commit();
    }

    // Get Skipped State
    public boolean isSkipped() {
        return pref.getBoolean(IS_SKIPPED, false);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    // Clearing all data from Shared Preferences
    public void logoutUser() {
        editor.clear();
        editor.commit();

    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getMobile() {
        return pref.getString(KEY_MOBILE, null);
    }

    public String getState() {
        return pref.getString(KEY_STATE, null);
    }

    public String getOpBal() {
        return pref.getString(KEY_OP_BAL, null);
    }

    public String getType() {
        return pref.getString(KEY_TYPE, null);
    }

    public String getWaiterName() {
        return pref.getString(WAITER_NAME, null);
    }

    public String getDiscountAmo() {
        return pref.getString(KEY_DISCOUNT, null);
    }

    public String getUserMobileNumber() {
        return pref.getString(KEY_MOBILE, null);
    }

    public String getOtp() {
        return pref.getString(KEY_OTP, null);
    }

    public String getCompCode() {
        return pref.getString(KEY_CompCODE, null);
    }

    public String getModel() {
        return pref.getString(KEY_MODEL, null);
    }

    public String getSerial() {
        return pref.getString(KEY_SERIAL, null);
    }

    public String getProduct() {
        return pref.getString(KEY_PRODUCT, null);
    }

    public String getAddress() {
        return pref.getString(KEY_ADDRESS, null);
    }

    public String getPincode() {
        return pref.getString(KEY_PINCODE, null);
    }

    public String getCompMobNo() {
        return pref.getString(KEY_Comp_MOBILE, null);
    }

    public boolean isCompDone() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}