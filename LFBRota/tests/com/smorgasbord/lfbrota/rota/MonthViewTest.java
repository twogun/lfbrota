package com.smorgasbord.lfbrota.rota;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class MonthViewTest {

    private MonthView monthView;
    
    @Before
    public void setUp() throws Exception {
    }

    /**
     * There was an anomaly with Dec13 and Jan14 where a day was getting dropped from the Rota it seems that it
     * is to do with fetching the previous days
     * 
     */
    @Test
    public void testGetDatesDec2013() {
        
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(2013, 12, 01);
        
        monthView = new MonthView(calendar);
        List<DateTime> dates = monthView.getDates();
        
        Assert.assertTrue("Simple test case no Implementation", true);
    }

}
