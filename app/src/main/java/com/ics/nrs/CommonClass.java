package com.ics.nrs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

class CommonClass {
    Activity activity;
    public SharedPreferences settings;

    public CommonClass(Activity activity){
        this.activity = activity;
       // settings = activity.getSharedPreferences(ConstValue.PREF_NAME, 0);
    }
    public void setSession(String key,String value){
        settings.edit().putString(key,value).commit();
    }

 /*   public String getSession(String key){
        return settings.getString(key,"");
    }
    public boolean is_user_login(){
        String key = getSession(ConstValue.COMMON_KEY);
        if (key==null || key.equalsIgnoreCase("")){
            return  false;
        }else {
            return  true;
        }
    }*/

    public  void open_screen(int position){
        Intent intent = null;
        switch (position)
        {
            case 0:
                intent = new Intent(activity, ComplaintList.class);
                break;
            case 1:
                intent = new Intent(activity, Login.class);
                break;
          /*  case 2:
                intent = new Intent(activity, ExamActivity.class);
                break;
            case 3:
                intent = new Intent(activity, BookActivity.class);
                break;
            case 4:
                intent = new Intent(activity, Quiz_subjectActivity.class);
                break;
            case 5:
                intent = new Intent(activity, GrowthActivity.class);
                break;*/

        }
        if (intent!=null){
            activity.startActivity(intent);
        }
    }
}
