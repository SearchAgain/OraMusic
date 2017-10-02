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
package com.searchAgain.oramusic.widgets.desktop;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.searchAgain.oramusic.MusicService;
import com.searchAgain.oramusic.R;
import com.searchAgain.oramusic.utils.NavigationUtils;
import com.searchAgain.oramusic.utils.oramusicUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SmallWidget extends BaseWidget {

    @Override
    int getLayoutRes() {
        return R.layout.widget_small;
    }

    @Override
    void onViewsUpdate(Context context, RemoteViews remoteViews, ComponentName serviceName, Bundle extras) {
        remoteViews.setOnClickPendingIntent(R.id.image_next, PendingIntent.getService(
                context,
                REQUEST_NEXT,
                new Intent(context, MusicService.class)
                        .setAction(MusicService.NEXT_ACTION)
                        .setComponent(serviceName),
                0
        ));
        remoteViews.setOnClickPendingIntent(R.id.image_playpause, PendingIntent.getService(
                context,
                REQUEST_PLAYPAUSE,
                new Intent(context, MusicService.class)
                        .setAction(MusicService.TOGGLEPAUSE_ACTION)
                        .setComponent(serviceName),
                0
        ));
        if (extras != null) {
            String t = extras.getString("track");
            if (t != null) {
                remoteViews.setTextViewText(R.id.textView_title, t);
            }
            t = extras.getString("artist");
            if (t != null) {
                remoteViews.setTextViewText(R.id.textView_subtitle, t);
            }
            remoteViews.setImageViewResource(R.id.image_playpause,
                    extras.getBoolean("playing") ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_white_36dp);
            long albumId = extras.getLong("albumid");
            if (albumId != -1) {
                Bitmap artwork = ImageLoader.getInstance().loadImageSync(oramusicUtils.getAlbumArtUri(albumId).toString());
                if (artwork != null) {
                    remoteViews.setImageViewBitmap(R.id.imageView_cover, artwork);
                } else {
                    remoteViews.setImageViewResource(R.id.imageView_cover, R.drawable.ic_empty_music2);
                }
            }
        }
        remoteViews.setOnClickPendingIntent(R.id.textView_title, PendingIntent.getActivity(
                context,
                0,
                NavigationUtils.getNowPlayingIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT
        ));
    }
}
