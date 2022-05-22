package com.example.pdpchatapp.cacheMemoryService;

import android.content.Context;
import android.content.SharedPreferences;


public class MySharedPreference {
    private static MySharedPreference mySharedPreference = new MySharedPreference();
    private static SharedPreferences sharedPreference;
    private static SharedPreferences.Editor editor;

    public static MySharedPreference getInstance(Context context, String fileName) {
        if (sharedPreference == null) {
            sharedPreference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return mySharedPreference;
    }

    public String userListGeT() {
        return sharedPreference.getString("firebase", "");
    }

    public void userListSeT(String userListString) {
        editor = sharedPreference.edit();
        editor.putString("firebase", userListString);
        editor.commit();
    }

    public void booleanValuesSeT(boolean booleanValues) {
        editor = sharedPreference.edit();
        editor.putBoolean("booleanValues", booleanValues);
        editor.commit();
    }

    public Boolean booleanValuesGeT() {
        return sharedPreference.getBoolean("booleanValues", true);
    }

    public void clearCache() {
        editor.clear();
    }

}
