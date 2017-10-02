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
 package com.searchAgain.oramusic.nowplaying;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.searchAgain.oramusic.MusicPlayer;
import com.searchAgain.oramusic.R;
import com.searchAgain.oramusic.dataloaders.SongLoader;
import com.searchAgain.oramusic.models.Song;
import com.searchAgain.oramusic.utils.oramusicUtils;
import com.searchAgain.oramusic.widgets.CircleImageView;
import com.searchAgain.oramusic.widgets.MusicVisualizer_gradient;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;



public class oraMusic6 extends BaseNowplayingFragment {

    TextView nextSong;
    CircleImageView nextArt;
    private MusicVisualizer_gradient visualizer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_oramusic6, container, false);

        setMusicStateListener();
        setSongDetails(rootView);

        initGestures(rootView.findViewById(R.id.album_art));

        ((SeekBar) rootView.findViewById(R.id.song_progress)).getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        ((SeekBar) rootView.findViewById(R.id.song_progress)).getThumb().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

        nextSong = (TextView) rootView.findViewById(R.id.title_next);
        nextArt = (CircleImageView) rootView.findViewById(R.id.album_art_next);
        visualizer = (MusicVisualizer_gradient) rootView.findViewById(R.id.pVisualizer);
        visualizer.setEnabled(true);
        //// TODO: 9/23/2017 update visulizer view to get gradient grained visulizer of 40 % hight 

        rootView.findViewById(R.id.nextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.setQueuePosition(MusicPlayer.getQueuePosition() + 1);
            }
        });

        return rootView;
    }

    @Override
    public void updateShuffleState() {
        if (shuffle != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                    .setSizeDp(30);

            if (MusicPlayer.getShuffleMode() == 0) {
                builder.setColor(Color.WHITE);
            } else builder.setColor(accentColor);

            shuffle.setImageDrawable(builder.build());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleShuffle();
                    updateShuffleState();
                    updateRepeatState();
                }
            });
        }
    }

    @Override
    public void updateRepeatState() {
        if (repeat != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setIcon(MaterialDrawableBuilder.IconValue.REPEAT)
                    .setSizeDp(30);

            if (MusicPlayer.getRepeatMode() == 0) {
                builder.setColor(Color.WHITE);
            } else builder.setColor(accentColor);

            repeat.setImageDrawable(builder.build());
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleRepeat();
                    updateRepeatState();
                    updateShuffleState();
                }
            });
        }
    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        if (getActivity() != null) {
            long nextId = MusicPlayer.getNextAudioId();
            Song next = SongLoader.getSongForID(getActivity(), nextId);
            nextSong.setText(next.title);
            nextArt.setImageURI(oramusicUtils.getAlbumArtUri(next.albumId));
        }
    }
}
