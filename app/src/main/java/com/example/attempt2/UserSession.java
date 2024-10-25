package com.example.attempt2;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static UserSession instance;
    private String userID;
    private static final String PREFS_NAME = "UserSessionPrefs";
    private static final String KEY_USER_ID = "userID";
    private SharedPreferences sharedPreferences;

    // Private constructor to prevent instantiation
    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString(KEY_USER_ID, null);  // Load saved userID
    }

    // Synchronized method to get the instance
    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        // Save the userID in SharedPreferences
        sharedPreferences.edit().putString(KEY_USER_ID, userID).apply();
    }

    public void clearSession() {
        userID = null;
        // Remove userID from SharedPreferences
        sharedPreferences.edit().remove(KEY_USER_ID).apply();
    }
}
