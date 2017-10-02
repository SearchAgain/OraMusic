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

package com.searchAgain.oramusic.dataloaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;

import com.searchAgain.oramusic.models.Song;
import com.searchAgain.oramusic.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

public class LastAddedLoader {

    private static Cursor mCursor;

    public static List<Song> getLastAddedSongs(Context context) {

        ArrayList<Song> mSongList = new ArrayList<>();
        mCursor = makeLastAddedCursor(context);

        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                long id = mCursor.getLong(0);
                String title = mCursor.getString(1);
                String artist = mCursor.getString(2);
                String album = mCursor.getString(3);
                int duration = mCursor.getInt(4);
                int trackNumber = mCursor.getInt(5);
                long artistId = mCursor.getInt(6);
                long albumId = mCursor.getLong(7);

                final Song song = new Song(id, albumId, artistId, title, artist, album, duration, trackNumber);

                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }

    public static final Cursor makeLastAddedCursor(final Context context) {
        //four weeks ago
        long fourWeeksAgo = (System.currentTimeMillis() / 1000) - (4 * 3600 * 24 * 7);
        long cutoff = PreferencesUtility.getInstance(context).getLastAddedCutoff();
        // use the most recent of the two timestamps
        if (cutoff < fourWeeksAgo) {
            cutoff = fourWeeksAgo;
        }

        final StringBuilder selection = new StringBuilder();
        selection.append(AudioColumns.IS_MUSIC + "=1");
        selection.append(" AND " + AudioColumns.TITLE + " != ''");
        selection.append(" AND " + MediaStore.Audio.Media.DATE_ADDED + ">");
        selection.append(cutoff);

        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id"}, selection.toString(), null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
    }
}
