package com.dreambig.supplymanagementapp.Locals;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Token {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Token(Application application){
        sharedPreferences = application.getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void updateToken(String token){
        editor.putString("TOKEN", token);
        editor.commit();
    }

    public String readToken(){
        return sharedPreferences.getString("TOKEN", "");
    }

    public void deleteToken(){
        editor.putString("TOKEN", null);
        editor.commit();
    }
}
