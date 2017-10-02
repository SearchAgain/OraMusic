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

package com.searchAgain.oramusic.listeners;

/**
 * Listens for playback changes to send the the fragments bound to this activity
 */
public interface MusicStateListener {

    /**
     * Called when {@link com.searchAgain.oramusic.MusicService#REFRESH} is invoked
     */
    void restartLoader();

    /**
     * Called when {@link com.searchAgain.oramusic.MusicService#PLAYLIST_CHANGED} is invoked
     */
    void onPlaylistChanged();

    /**
     * Called when {@link com.searchAgain.oramusic.MusicService#META_CHANGED} is invoked
     */
    void onMetaChanged();

}
