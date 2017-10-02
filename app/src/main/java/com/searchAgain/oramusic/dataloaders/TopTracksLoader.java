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
import android.provider.BaseColumns;

import com.searchAgain.oramusic.provider.RecentStore;
import com.searchAgain.oramusic.provider.SongPlayCount;

import java.util.ArrayList;

public class TopTracksLoader extends SongLoader {

    public static final int NUMBER_OF_SONGS = 99;
    protected static QueryType mQueryType;
    private static Context mContext;

    public TopTracksLoader(final Context context, QueryType type) {
        mContext = context;
        mQueryType = type;
    }

    public static Cursor getCursor() {
        SortedCursor retCursor = null;
        if (mQueryType == QueryType.TopTracks) {
            retCursor = makeTopTracksCursor(mContext);
        } else if (mQueryType == QueryType.RecentSongs) {
            retCursor = makeRecentTracksCursor(mContext);
        }

        if (retCursor != null) {
            ArrayList<Long> missingIds = retCursor.getMissingIds();
            if (missingIds != null && missingIds.size() > 0) {
                for (long id : missingIds) {
                    if (mQueryType == QueryType.TopTracks) {
                        SongPlayCount.getInstance(mContext).removeItem(id);
                    } else if (mQueryType == QueryType.RecentSongs) {
                        RecentStore.getInstance(mContext).removeItem(id);
                    }
                }
            }
        }

        return retCursor;
    }

    public static final SortedCursor makeTopTracksCursor(final Context context) {

        Cursor songs = SongPlayCount.getInstance(context).getTopPlayedResults(NUMBER_OF_SONGS);

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(SongPlayCount.SongPlayCountColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
                songs = null;
            }
        }
    }

    public static final SortedCursor makeRecentTracksCursor(final Context context) {

        Cursor songs = RecentStore.getInstance(context).queryRecentIds(null);

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(SongPlayCount.SongPlayCountColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
                songs = null;
            }
        }
    }

    public static final SortedCursor makeSortedCursor(final Context context, final Cursor cursor,
                                                      final int idColumn) {
        if (cursor != null && cursor.moveToFirst()) {

            StringBuilder selection = new StringBuilder();
            selection.append(BaseColumns._ID);
            selection.append(" IN (");

            long[] order = new long[cursor.getCount()];

            long id = cursor.getLong(idColumn);
            selection.append(id);
            order[cursor.getPosition()] = id;

            while (cursor.moveToNext()) {
                selection.append(",");

                id = cursor.getLong(idColumn);
                order[cursor.getPosition()] = id;
                selection.append(String.valueOf(id));
            }

            selection.append(")");

            Cursor songCursor = makeSongCursor(context, selection.toString(), null);
            if (songCursor != null) {
                return new SortedCursor(songCursor, order, BaseColumns._ID, null);
            }
        }

        return null;
    }


    public enum QueryType {
        TopTracks,
        RecentSongs,
    }
}
