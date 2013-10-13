package com.smorgasbord.lfbrota.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.smorgasbord.lfbrota.R;

public class HelloWorldWidget extends AppWidgetProvider {

	@SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss EEEEEEEEE\nd MMM yyy");

	public static String CLICK_ACTION = "com.example.android.HelloWorldWidget.CLICK";
	
	
	public HelloWorldWidget() {
		Log.d(":LFBRota","Hello World widget Constructor called");
		

	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		String now = formatter.format(new Date());
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_hw_layout);
		
		for (int i = 0; i < appWidgetIds.length; ++i) {
		    final Intent intent = new Intent(context, HelloWorldWidget.class);
		    intent.setAction(HelloWorldWidget.CLICK_ACTION);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.button, pendingIntent);
        }
		
		
		rv.setTextViewText(R.id.text, now);
		appWidgetManager.updateAppWidget(appWidgetIds, rv);
		
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HWWidget", "On receive");
        String now = formatter.format(new Date());
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_hw_layout);
        rv.setTextViewText(R.id.text, now);
        
        AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(context);
        appWidgetMan.updateAppWidget(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0), rv);
        super.onReceive(context, intent);
    }
	
}
