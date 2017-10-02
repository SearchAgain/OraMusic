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
package com.searchAgain.oramusic.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.searchAgain.oramusic.MusicPlayer;
import com.searchAgain.oramusic.fragments.PlaylistFragment;
import com.searchAgain.oramusic.models.Song;

public class CreatePlaylistDialog extends DialogFragment {

    public static CreatePlaylistDialog newInstance() {
        return newInstance((Song) null);
    }

    public static CreatePlaylistDialog newInstance(Song song) {
        long[] songs;
        if (song == null) {
            songs = new long[0];
        } else {
            songs = new long[1];
            songs[0] = song.id;
        }
        return newInstance(songs);
    }

    public static CreatePlaylistDialog newInstance(long[] songList) {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putLongArray("songs", songList);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity()).positiveText("Create").negativeText("Cancel").input("Enter playlist name", "", false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                long[] songs = getArguments().getLongArray("songs");
                long playistId = MusicPlayer.createPlaylist(getActivity(), input.toString());

                if (playistId != -1) {
                    if (songs != null && songs.length != 0)
                        MusicPlayer.addToPlaylist(getActivity(), songs, playistId);
                    else
                        Toast.makeText(getActivity(), "Created playlist", Toast.LENGTH_SHORT).show();
                    if (getParentFragment() instanceof PlaylistFragment) {
                        ((PlaylistFragment) getParentFragment()).updatePlaylists(playistId);
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to create playlist", Toast.LENGTH_SHORT).show();
                }

            }
        }).build();
    }
}
