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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ScrobbleQuery {
    private static final String ARTIST_NAME = "artist";
    private static final String TRACK_NAME = "track";
    private static final String TIMESTAMP_NAME = "timestamp";

    @SerializedName(ARTIST_NAME)
    public String mArtist;

    @SerializedName(TRACK_NAME)
    public String mTrack;

    @SerializedName(TIMESTAMP_NAME)
    public long mTimestamp;

    public static final String Method = "track.scrobble";

    public ScrobbleQuery(String in) {
        String[] arr = in.split(",");
        try {
            this.mArtist = URLDecoder.decode(arr[0],"UTF-8");
            this.mTrack = URLDecoder.decode(arr[1],"UTF-8");
            this.mTimestamp = Long.parseLong(arr[2],16);
        } catch (UnsupportedEncodingException ignored) { }
    }

    public ScrobbleQuery(String artist, String track, long timestamp) {
        this.mArtist = artist;
        this.mTrack = track;
        this.mTimestamp = timestamp;
    }

    @Override
    public String toString(){
        try {
            return URLEncoder.encode(mArtist,"UTF-8")+','+URLEncoder.encode(mTrack,"UTF-8")+','+Long.toHexString(mTimestamp);
        } catch (UnsupportedEncodingException ignored) {
            return "";
        }
    }
}
