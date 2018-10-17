package com.twjitm.transaction.utils;import java.text.SimpleDateFormat;import java.util.Calendar;import java.util.Date;/** * @author twjitm - [Created on 2018-08-27 11:14] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public class TimeUtil {    public static long SECOND = 1000;    public static long MINUTE = 60 * SECOND;    public static long FIVE_MINUTE = 5 * MINUTE;    public static long ONE_HOUR = 60 * MINUTE;    public static long SIX_HOUR = 6 * ONE_HOUR;    public static long ONE_DAY = 24 * ONE_HOUR;    public static int SECOND_SECOND = 1;    public static int MINUTE_SECOND = (int) (MINUTE / SECOND);    public static int SIX_HOUR_SECOND = (int) (SIX_HOUR / SECOND);    public static long getSeconds(){        long now = System.currentTimeMillis();        return now / SECOND;    }    public static String getDateString(Date d){        Calendar c = Calendar.getInstance();        c.setTime(d);        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        return format.format(c.getTime());    }}