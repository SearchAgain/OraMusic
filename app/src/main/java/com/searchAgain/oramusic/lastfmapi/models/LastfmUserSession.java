/*
 * Copyright (C) 2017 Akshay Nikhare
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package com.searchAgain.oramusic.lastfmapi.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;
import com.searchAgain.oramusic.lastfmapi.LastFmClient;

public class LastfmUserSession {
    private static final String USERNAME = "name";
    private static final String TOKEN = "key";
    private static LastfmUserSession session;


    public static LastfmUserSession getSession(Context context) {
        if (session != null) return session;
        SharedPreferences preferences = context.getSharedPreferences(LastFmClient.PREFERENCES_NAME, Context.MODE_PRIVATE);
        session = new LastfmUserSession();
        session.mToken = preferences.getString(TOKEN, null);
        session.mUsername = preferences.getString(USERNAME, null);
        return session;
    }

    public boolean isLogedin(){
        return session.mToken != null && session.mUsername != null;
    }

    public void update(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LastFmClient.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (this.mToken == null || this.mUsername == null) {
            editor.clear();
        } else {
            editor.putString(TOKEN, this.mToken);
            editor.putString(USERNAME, this.mUsername);
        }
        editor.apply();
    }

    @SerializedName(USERNAME)
    public String mUsername;

    @SerializedName(TOKEN)
    public String mToken;
}
