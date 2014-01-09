package com.smorgasbord.lfbrota.rota;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

public class DayRendererTest extends TestCase {
    
    private DayRenderer dayRenderer;
    
    @Test
    public void test1stDec13() {
        dayRenderer = new DayRenderer(new LFBRota());
        dayRenderer.isStartDay(new Date(2014, 12, 01));
    }

}
