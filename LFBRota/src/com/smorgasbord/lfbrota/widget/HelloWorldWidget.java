package com.smorgasbord.lfbrota.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.smorgasbord.lfbrota.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

public class HelloWorldWidget extends AppWidgetProvider {

	private SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE\nd MMM yyy");


	public HelloWorldWidget() {
		Log.d(":LFBRota","Hello World widget Constructor called");
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
	String now = formatter.format(new Date());
	
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_hw_layout);
		updateViews.setTextViewText(R.id.text, now);
		appWidgetManager.updateAppWidget(appWidgetIds, updateViews);
		
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	
	
}
