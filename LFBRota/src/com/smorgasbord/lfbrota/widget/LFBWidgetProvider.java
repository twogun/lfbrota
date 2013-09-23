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
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.Toast;

import com.smorgasbord.lfbrota.R;

/**
 * The LFB widget's AppWidgetProvider.
 */
public class LFBWidgetProvider extends AppWidgetProvider {
	public static String CLICK_ACTION = "com.example.android.weatherlistwidget.CLICK";
	public static String TODAY_ACTION = "com.example.android.weatherlistwidget.TODAY";
	public static String PREVWK_ACTION = "com.example.android.weatherlistwidget.PREVWEEK";
	public static String NEXTWK_ACTION = "com.example.android.weatherlistwidget.NEXTWEEK";
	public static String EXTRA_DAY_ID = "com.example.android.weatherlistwidget.day";

	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	private static LFBDataProviderObserver sDataObserver;

	private boolean mIsLargeLayout = true;

	private int todayClicks = 0;

	public LFBWidgetProvider() {
		// Start the worker thread
		Log.d("LFBRota", "Starting LFB Widget");
		sWorkerThread = new HandlerThread("LFBWidgetProvider-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());
	}

	// XXX: clear the worker queue if we are destroyed?

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

			CharSequence text = "Today Clicked ";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(ctx, text, duration);
			toast.show();

		} else if (action.equals(NEXTWK_ACTION)) {
			int x = prefs.getInt("weekOffset", 0) + 1;
			Editor edit = prefs.edit();
			edit.putInt("weekOffset", x);
			edit.commit();

			CharSequence text = "Next Week offset:  " + x;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(ctx, text, duration);
			toast.show();

		} else if (action.equals(PREVWK_ACTION)) {

			int x = prefs.getInt("weekOffset", 0) - 1;
			Editor edit = prefs.edit();
			edit.putInt("weekOffset", x);
			edit.commit();

			CharSequence text = "Prev Week offset:  " + x;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(ctx, text, duration);
			toast.show();

		}
		
		final Context context = ctx;
		final String[] selectionArgs= {prefs.getInt("weekOffset", 0) + "", };
        sWorkerQueue.removeMessages(0);
        sWorkerQueue.post(new Runnable() {
            @Override
            public void run() {
                final ContentResolver r = context.getContentResolver();
                final Cursor c = r.query(LFBDataProvider.CONTENT_URI, null, null, selectionArgs, null);
                final int count = c.getCount();

                // We disable the data changed observer temporarily since each of the updates
                // will trigger an onChange() in our data observer.
              //  r.unregisterContentObserver(sDataObserver);
//                for (int i = 0; i < count; ++i) {
//                    final Uri uri = ContentUris.withAppendedId(LFBDataProvider.CONTENT_URI, i);
//                    final ContentValues values = new ContentValues();
//                    
//                    r.update(uri, values, null, null);
//                }
               // r.registerContentObserver(LFBDataProvider.CONTENT_URI, true, sDataObserver);

                final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                final ComponentName cn = new ComponentName(context, LFBWidgetProvider.class);
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.dayList);
            }
        });

		super.onReceive(ctx, intent);
	}

	private RemoteViews buildLayout(Context context, int appWidgetId,
			boolean largeLayout) {
		RemoteViews rv;

		// Specify the service to provide data for the collection widget. Note
		// that we need to
		// embed the appWidgetId via the data otherwise it will be ignored.
		final Intent intent = new Intent(context, LFBWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		rv.setRemoteAdapter(appWidgetId, R.id.dayList, intent);

		// Bind the click intent for the refresh button on the widget
		final Intent refreshIntent = new Intent(context,
				LFBWidgetProvider.class);
		refreshIntent.setAction(LFBWidgetProvider.TODAY_ACTION);
		final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
				context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.todayBtn, refreshPendingIntent);

		final Intent nxtWeekIntent = new Intent(context,
				LFBWidgetProvider.class);
		nxtWeekIntent.setAction(LFBWidgetProvider.NEXTWK_ACTION);
		final PendingIntent nxtWeekPendingIntent = PendingIntent.getBroadcast(
				context, 0, nxtWeekIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.nextWeek, nxtWeekPendingIntent);

		final Intent prvWeekIntent = new Intent(context,
				LFBWidgetProvider.class);
		prvWeekIntent.setAction(LFBWidgetProvider.PREVWK_ACTION);
		final PendingIntent prvWeekPendingIntent = PendingIntent.getBroadcast(
				context, 0, prvWeekIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.prevWeek, prvWeekPendingIntent);

		return rv;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews layout = buildLayout(context, appWidgetIds[i],
					mIsLargeLayout);
			appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {

		int minWidth = newOptions
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
		int maxWidth = newOptions
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
		int minHeight = newOptions
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		int maxHeight = newOptions
				.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

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
	private AppWidgetManager mAppWidgetManager;
	private ComponentName mComponentName;

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