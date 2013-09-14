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

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;

import com.smorgasbord.lfbrota.rota.DayRenderer;
import com.smorgasbord.lfbrota.rota.LFBRota;


/**
 * The AppWidgetProvider for our sample weather widget.
 */
public class LFBDataProvider extends ContentProvider {
    
	public static final Uri CONTENT_URI = Uri.parse("content://com.smorgasbord.lfbrota.widget.provider.LFBDataProvider");
    
	public static class Columns {
        public static final String DAY_OF_MONTH = "day_of_month";
        public static final String DAY_COLOUR = "day_colour";
        public static final String DAY_BORDER_COLOUR = "day_border_colour";
    }
	
	private DayRenderer renderer;
	
	
    @Override
    public boolean onCreate() {
        renderer = new DayRenderer(new LFBRota());
        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        assert(uri.getPathSegments().isEmpty());

        // In this sample, we only query without any parameters, so we can just return a cursor to the current week
        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.DAY_OF_MONTH, Columns.DAY_COLOUR, Columns.DAY_BORDER_COLOUR });
        
        DateTime now = new DateTime();
        DateTimeComparator dateOnlyInstance = DateTimeComparator.getDateOnlyInstance();
        int borderColour = Color.BLACK;
        
        for (int i = 0; i < 7; i++) {
        	DateTime day = now.withDayOfWeek(i+1);
        	if(dateOnlyInstance.compare(now, day) == 0) {
        		borderColour = Color.MAGENTA;
        	};
            c.addRow(new Object[]{ 
            		day.dayOfMonth().getAsShortText(),
            		renderer.getDayColour(day.toDate()),
            		borderColour});
        }
        
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.lfbrotawidget.date";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // This example code does not support inserting
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // This example code does not support deleting
        return 0;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        assert(uri.getPathSegments().size() == 1);

        // In this sample, we only update the content provider individually for each row with new
        // temperature values.
        final int index = Integer.parseInt(uri.getPathSegments().get(0));
        final MatrixCursor c = new MatrixCursor( new String[]{ Columns.DAY_OF_MONTH });
                
        // Notify any listeners that the data backing the content provider has changed, and return
        // the number of rows affected.
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

}