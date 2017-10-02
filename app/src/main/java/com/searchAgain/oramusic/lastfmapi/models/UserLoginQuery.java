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

import com.google.gson.annotations.SerializedName;
import com.searchAgain.oramusic.lastfmapi.LastFmClient;


public class UserLoginQuery {
    private static final String USERNAME_NAME = "username";
    private static final String PASSWORD_NAME = "password";

    @SerializedName(USERNAME_NAME)
    public String mUsername;

    @SerializedName(PASSWORD_NAME)
    public String mPassword;

    public static final String Method = "auth.getMobileSession";

    public UserLoginQuery(String username, String password) {
        this.mUsername = username;
        this.mPassword = password;
    }

    public String getSignature() {
        return "api_key" + LastFmClient.API_KEY  + "method" + Method + "password" + mPassword + "username" + mUsername + LastFmClient.API_SECRET;
    }
}
