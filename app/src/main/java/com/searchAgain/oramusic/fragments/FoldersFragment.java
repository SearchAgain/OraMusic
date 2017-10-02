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
package com.searchAgain.oramusic.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.appthemeengine.ATE;

import com.searchAgain.oramusic.adapters.FolderAdapter;
import com.searchAgain.oramusic.dialogs.StorageSelectDialog;
import com.searchAgain.oramusic.utils.PreferencesUtility;
import com.searchAgain.oramusic.widgets.DividerItemDecoration;
import com.searchAgain.oramusic.widgets.FastScroller;
import com.searchAgain.oramusic.R;

import java.io.File;


public class FoldersFragment extends Fragment implements StorageSelectDialog.OnDirSelectListener {

    private FolderAdapter mAdapter;
    private RecyclerView recyclerView;
    private FastScroller fastScroller;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_folders, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.folders);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        fastScroller = (FastScroller) rootView.findViewById(R.id.fastscroller);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getActivity() != null)
            new loadFolders().execute("");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean dark = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("dark_theme", false);
        if (dark) {
            ATE.apply(this, "dark_theme");
        } else {
            ATE.apply(this, "light_theme");
        }
        if (mAdapter != null) {
            mAdapter.applyTheme(dark);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setItemDecoration() {
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_folders, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_storages) {
            new StorageSelectDialog(getActivity())
                    .setDirSelectListener(this)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateTheme() {
        Context context = getActivity();
        if (context != null) {
            boolean dark = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false);
            mAdapter.applyTheme(dark);
        }
    }

    @Override
    public void onDirSelected(File dir) {
        mAdapter.updateDataSetAsync(dir);
    }

    private class loadFolders extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Activity activity = getActivity();
            if (activity != null) {
                mAdapter = new FolderAdapter(activity, new File(PreferencesUtility.getInstance(activity).getLastFolder()));
                updateTheme();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            recyclerView.setAdapter(mAdapter);
            //to add spacing between cards
            if (getActivity() != null) {
                setItemDecoration();
            }
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            fastScroller.setVisibility(View.VISIBLE);
            fastScroller.setRecyclerView(recyclerView);
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
