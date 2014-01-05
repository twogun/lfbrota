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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;

import com.smorgasbord.lfbrota.R;

/**
 * The LFB widget's AppWidgetProvider.
 */
public class LFBWidgetProvider extends AppWidgetProvider {
    
    public static final String WIDGETTAG = "LFBWidget";
    
	public static String CLICK_ACTION = "com.example.android.weatherlistwidget.CLICK";
	public static String TODAY_ACTION = "com.example.android.weatherlistwidget.TODAY";
	public static String PREVWK_ACTION = "com.example.android.weatherlistwidget.PREVWEEK";
	public static String NEXTWK_ACTION = "com.example.android.weatherlistwidget.NEXTWEEK";
	public static String EXTRA_DAY_ID = "com.example.android.weatherlistwidget.day";

	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	private static LFBDataProviderObserver sDataObserver;

	private boolean mIsLargeLayout = true;

	private final int todayClicks = 0;

	public LFBWidgetProvider() {
		// Start the worker thread
		Log.d(WIDGETTAG, "Starting LFB Widget");
		sWorkerThread = new HandlerThread("LFBWidgetProvider-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());
	}

	
	@Override
	public void onEnabled(Context context) {
		// Register for external updates to the data to trigger an update of the
		// widget. When using
		// content providers, the data is often updated via a background
		// service, or in response to
		// user interaction in the main app. To ensure that the widget always
		// reflects the current
		// state of the data, we must listen for changes and update ourselves
		// accordingly.
		final ContentResolver r = context.getContentResolver();
		if (sDataObserver == null) {
			final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
			final ComponentName cn = new ComponentName(context,
					LFBWidgetProvider.class);
			sDataObserver = new LFBDataProviderObserver(mgr, cn, sWorkerQueue);
			r.registerContentObserver(LFBDataProvider.CONTENT_URI, true,
					sDataObserver);
		}
	}

	@Override
	public void onReceive(Context ctx, Intent intent) {
		final String action = intent.getAction();
		SharedPreferences prefs = ctx.getSharedPreferences("lfbwidget", 0);
		
		if (action.equals(TODAY_ACTION)) {
			Editor edit = prefs.edit();
			edit.putInt("weekOffset", 0);
			edit.commit();

		} else if (action.equals(NEXTWK_ACTION)) {
			int x = prefs.getInt("weekOffset", 0) + 1;
			Editor edit = prefs.edit();
			edit.putInt("weekOffset", x);
			edit.commit();

		} else if (action.equals(PREVWK_ACTION)) {
			int x = prefs.getInt("weekOffset", 0) - 1;
			Editor edit = prefs.edit();
			edit.putInt("weekOffset", x);
			edit.commit();
		}
		
		final Context context = ctx;
		final String[] selectionArgs= {prefs.getInt("weekOffset", 0) + "", };
        sWorkerQueue.removeMessages(0);
        sWorkerQueue.post(new Runnable() {
            @Override
            public void run() {
                final ContentResolver r = context.getContentResolver();
                final Cursor c = r.query(LFBDataProvider.CONTENT_URI, null, null, selectionArgs, null);
                int count = c.getCount();
                final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                final ComponentName cn = new ComponentName(context, LFBWidgetProvider.class);
                int[] appWidgetIds = mgr.getAppWidgetIds(cn);
                //RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_hw_layout);
                //rv.setTextViewText(R.id.text, now);
               // mgr.notifyAppWidgetViewDataChanged(appWidgetId, viewId)
                mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dayList);
            }
        });

		super.onReceive(ctx, intent);
	}

	private RemoteViews buildLayout(Context context, int appWidgetId,
			boolean largeLayout) {
		
	    final Intent intent = new Intent(context, LFBWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
	    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rv.setRemoteAdapter(appWidgetId, R.id.dayList, intent);
		
        rv.setTextViewText(R.id.monthLabel, "grrrr");
        
		final Intent refreshIntent = new Intent(context, LFBWidgetProvider.class);
		refreshIntent.setAction(LFBWidgetProvider.TODAY_ACTION);
		final PendingIntent refreshPendingIntent = 
		        PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.todayBtn, refreshPendingIntent);

		final Intent nxtWeekIntent = new Intent(context, LFBWidgetProvider.class);
		nxtWeekIntent.setAction(LFBWidgetProvider.NEXTWK_ACTION);
		final PendingIntent nxtWeekPendingIntent = 
		        PendingIntent.getBroadcast(context, 0, nxtWeekIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.nextWeek, nxtWeekPendingIntent);

		final Intent prvWeekIntent = new Intent(context,LFBWidgetProvider.class);
		prvWeekIntent.setAction(LFBWidgetProvider.PREVWK_ACTION);
		final PendingIntent prvWeekPendingIntent = 
		        PendingIntent.getBroadcast(context, 0, prvWeekIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.prevWeek, prvWeekPendingIntent);

		return rv;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews layout = buildLayout(context, appWidgetIds[i], mIsLargeLayout);
			appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

		int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
		int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
		int minHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

		RemoteViews layout;
		if (minHeight < 100) {
			mIsLargeLayout = false;
		} else {
			mIsLargeLayout = true;
		}
		layout = buildLayout(context, appWidgetId, mIsLargeLayout);
		appWidgetManager.updateAppWidget(appWidgetId, layout);
	}

}

/**
 * Our data observer just notifies an update for all weather widgets when it
 * detects a change.
 */
class LFBDataProviderObserver extends ContentObserver {
	private final AppWidgetManager mAppWidgetManager;
	private final ComponentName mComponentName;

	LFBDataProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
		super(h);
		mAppWidgetManager = mgr;
		mComponentName = cn;
	}

	@Override
	public void onChange(boolean selfChange) {
		// The data has changed, so notify the widget that the collection view
		// needs to be updated.
		// In response, the factory's onDataSetChanged() will be called which
		// will requery the
		// cursor for the new data.
		mAppWidgetManager
				.notifyAppWidgetViewDataChanged(
						mAppWidgetManager.getAppWidgetIds(mComponentName),
						R.id.dayList);
	}
}