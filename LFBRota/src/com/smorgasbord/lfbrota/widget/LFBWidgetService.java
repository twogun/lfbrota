/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smorgasbord.lfbrota.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.smorgasbord.lfbrota.R;

/**
 * This is the service that provides the factory to be bound to the collection service.
 */
public class LFBWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/**
 * This is the factory that will provide data to the collection widget.
 */
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider
        String day = "00";
        int colour = Color.GRAY;
        int isToday = 0;
        String dayOfWeek = "ss";
        
        
        if (mCursor.moveToPosition(position)) {
            final int dayColIndex = mCursor.getColumnIndex(LFBDataProvider.Columns.DAY_OF_MONTH);
            final int dayColourIndex = mCursor.getColumnIndex(LFBDataProvider.Columns.DAY_COLOUR);
            final int dayisToday = mCursor.getColumnIndex(LFBDataProvider.Columns.DAY_IS_TODAY);
            final int dow = mCursor.getColumnIndex(LFBDataProvider.Columns.DAY_OF_WEEK);
            day = mCursor.getString(dayColIndex);
            colour = mCursor.getInt(dayColourIndex);
            isToday = mCursor.getInt(dayisToday);
            dayOfWeek = mCursor.getString(dow);
        }

        final int itemId = R.layout.widget_daycell;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        //rv.setTextViewText(R.id.widget_day_gridcell, day);
        
        SpannableString s = new SpannableString(day); 
        if(isToday > 0) {
        	s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        	s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        	s.setSpan(new RelativeSizeSpan(1.3f), 0, s.length(), 0);
        	rv.setInt(R.id.widget_day_gridcell, "setTextColor", Color.BLACK);
        	rv.setInt(R.id.dayList, "setSelection", position);
        }
        
        rv.setTextViewText(R.id.widget_day_gridcell, s); 
        rv.setTextViewText(R.id.widget_daynumber, dayOfWeek);
        rv.setInt(R.id.widget_day_gridcell, "setBackgroundColor", colour);
        
        
        // Set the click intent so that we can handle it and show a toast message
        final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
        extras.putString(LFBWidgetProvider.EXTRA_DAY_ID, day);
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_daynumber, fillInIntent);

        return rv;
    }
    public RemoteViews getLoadingView() {
        // We aren't going to return a default loading view in this sample
        return null;
    }

    public int getViewTypeCount() {
        // Technically, we have two types of views (the dark and light background views)
        return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        
        SharedPreferences prefs = mContext.getSharedPreferences("lfbwidget", 0);
		int x = prefs.getInt("weekOffset", 0);
		String[] selectionArgs = {x + "", };
        
        mCursor = mContext.getContentResolver().query(LFBDataProvider.CONTENT_URI, null, null, selectionArgs, null);
    }
}
