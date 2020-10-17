package com.utils.Session;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class SessionManager {
    private final SharedPreferences prf;
    private final SharedPreferences.Editor edit;
    Context context;

    public static SessionManager get(Context context) {
        return new SessionManager(context);
    }
    public SessionManager(Context context) {
        this.context = context;
        prf=context.getSharedPreferences(SessionKey.DB_RLM.name(), Context.MODE_PRIVATE);
        edit=prf.edit();
    }

    public void CreateSession(String result){
        edit.putBoolean(SessionKey.IsUserLogedIn.name(),true);
        edit.putString(SessionKey.UserProfile.name(),result);
        edit.commit();
    }
    public String getAllDetails(){
        return prf.getString(SessionKey.UserProfile.name(),"");
    }
    public String getValue(SessionKey key){
        JSONObject jsonObject = null;
        String result="";
        try {
            jsonObject=new JSONObject(prf.getString(SessionKey.UserProfile.name(),""));
            result=jsonObject.getString(key.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String getUserID(){
        JSONObject jsonObject = null;
        String result="";
        try {
            jsonObject=new JSONObject(prf.getString(SessionKey.UserProfile.name(),""));
            result=jsonObject.getString(SessionKey.id.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isUserLogin(){
        return prf.getBoolean(SessionKey.IsUserLogedIn.name(),false);
    }
    public void Logout(){
        edit.clear();
        edit.commit();
    }

    public void setLanguage(String lng){
        SharedPreferences lng_prf = context.getSharedPreferences("rlm_lng", Context.MODE_PRIVATE);
        SharedPreferences.Editor lng_edit = lng_prf.edit();
        lng_edit.putString(SessionKey.language.name(),lng);
        lng_edit.apply();
    }

    public String getSelectedLanguage(){
        SharedPreferences lng_prf = context.getSharedPreferences("rlm_lng", Context.MODE_PRIVATE);
        return lng_prf.getString(SessionKey.language.name(), Locale.getDefault().getDisplayLanguage());
    }

}

