package com.group5.miniproject2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveLoginSession(int userId, String username, String fullName) {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USERNAME, username);
        editor.putString(Constants.KEY_FULL_NAME, fullName);
        editor.apply();
    }

    public void logout() {
        editor.clear().apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(Constants.KEY_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(Constants.KEY_USERNAME, "");
    }

    public String getFullName() {
        return prefs.getString(Constants.KEY_FULL_NAME, "");
    }
}