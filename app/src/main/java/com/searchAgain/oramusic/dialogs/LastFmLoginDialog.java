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
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.searchAgain.oramusic.fragments.SettingsFragment;
import com.searchAgain.oramusic.lastfmapi.LastFmClient;
import com.searchAgain.oramusic.lastfmapi.callbacks.UserListener;
import com.searchAgain.oramusic.lastfmapi.models.UserLoginQuery;
import com.searchAgain.oramusic.utils.PreferencesUtility;
import com.searchAgain.oramusic.R;


public class LastFmLoginDialog extends DialogFragment {
    public static final String FRAGMENT_NAME = "LastFMLogin";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getActivity()).
                positiveText("Login").
                negativeText(getString(R.string.cancel)).
                title(getString(R.string.lastfm_login)).
                customView(R.layout.dialog_lastfm_login, false).
                onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String username = ((EditText) dialog.findViewById(R.id.lastfm_username)).getText().toString();
                        String password = ((EditText) dialog.findViewById(R.id.lastfm_password)).getText().toString();
                        if (username.length() == 0 || password.length() == 0) return;
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Logging in..");
                        progressDialog.show();
                        LastFmClient.getInstance(getActivity()).getUserLoginInfo(new UserLoginQuery(username, password), new UserListener() {

                            @Override
                            public void userSuccess() {
                                progressDialog.dismiss();
                                if (getTargetFragment() instanceof SettingsFragment) {
                                    ((SettingsFragment) getTargetFragment()).updateLastFM();
                                }
                            }

                            @Override
                            public void userInfoFailed() {
                                progressDialog.dismiss();
                                Toast.makeText(getTargetFragment().getActivity(), getString(R.string.lastfm_login_failture), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).build();
    }
}
