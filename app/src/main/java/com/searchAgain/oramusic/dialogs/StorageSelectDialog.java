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

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;


import com.searchAgain.oramusic.R;

import java.io.File;
import java.io.FileFilter;



public class StorageSelectDialog implements DialogInterface.OnClickListener {

    private final AlertDialog mDialog;
    private final File[] mStorages;
    private OnDirSelectListener mDirSelectListener;

    public StorageSelectDialog(final Context context) {
        mStorages = getAvailableStorages(context);
        String[] names = new String[mStorages.length];
        for (int i=0;i<mStorages.length;i++) {
            names[i] = mStorages[i].getName();
        }
        mDialog = new AlertDialog.Builder(context)
                .setItems(names, this)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(  R.string.menu_show_as_entry_default, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDirSelectListener.onDirSelected(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
                    }
                })
                .setCancelable(true)
                .setTitle(R.string.select_storage)
                .create();
    }

    public StorageSelectDialog setDirSelectListener(OnDirSelectListener dirSelectListener) {
        this.mDirSelectListener = dirSelectListener;
        return this;
    }

    public void show() {
        mDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int position) {
        File dir = mStorages[position];
        mDirSelectListener.onDirSelected(dir);
    }


    private static File[] getAvailableStorages(Context context) {
        File storageRoot = new File("/storage");
        return storageRoot.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.canRead();
            }
        });
    }

    public interface OnDirSelectListener {
        void onDirSelected(File dir);
    }
}