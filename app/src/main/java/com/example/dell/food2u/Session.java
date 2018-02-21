package com.example.dell.food2u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by dell on 19-Feb-18.
 */


public class Session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context ctx;

    int PRIVATE_MODE = 0;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String MOB_NO = "mobile";

    public Session(Context ctx){
        this.ctx = ctx;
        preferences = ctx.getSharedPreferences("myapp",PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String mobile){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(MOB_NO,mobile);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            ctx.startActivity(new Intent(ctx,Login.class));
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        user.put(MOB_NO,preferences.getString(MOB_NO,null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        ctx.startActivity(new Intent(ctx,Login.class));
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN,false);
    }

}
