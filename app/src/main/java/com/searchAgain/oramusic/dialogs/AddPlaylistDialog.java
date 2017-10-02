
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
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.searchAgain.oramusic.MusicPlayer;
import com.searchAgain.oramusic.dataloaders.PlaylistLoader;
import com.searchAgain.oramusic.models.Playlist;
import com.searchAgain.oramusic.models.Song;

import java.util.List;

public class AddPlaylistDialog extends DialogFragment {

    public static AddPlaylistDialog newInstance(Song song) {
        long[] songs = new long[1];
        songs[0] = song.id;
        return newInstance(songs);
    }

    public static AddPlaylistDialog newInstance(long[] songList) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putLongArray("songs", songList);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), false);
        CharSequence[] chars = new CharSequence[playlists.size() + 1];
        chars[0] = "Create new playlist";

        for (int i = 0; i < playlists.size(); i++) {
            chars[i + 1] = playlists.get(i).name;
        }
        return new MaterialDialog.Builder(getActivity()).title("Add to playlist").items(chars).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                long[] songs = getArguments().getLongArray("songs");
                if (which == 0) {
                    CreatePlaylistDialog.newInstance(songs).show(getActivity().getSupportFragmentManager(), "CREATE_PLAYLIST");
                    return;
                }

                MusicPlayer.addToPlaylist(getActivity(), songs, playlists.get(which - 1).id);
                dialog.dismiss();

            }
        }).build();
    }
}
