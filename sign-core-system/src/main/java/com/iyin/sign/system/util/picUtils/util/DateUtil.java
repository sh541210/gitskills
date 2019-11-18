//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.iyin.sign.system.util.picUtils.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String TIME_FORMAT = "HH:mm:ss";
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DateUtil() {
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toDateString(Date date) {
        return toString(date, DATE_FORMAT);
    }

    public static String toTimeString(Date date) {
        return toString(date, TIME_FORMAT);
    }

    public static String toDateTimeString(Date date) {
        return toString(date, DATE_TIME_FORMAT);
    }

    public static Date toDate(String str) {
        return toDate(str, (Date)null);
    }

    public static Date toDate(String str, String format) {
        return toDate(str, format, (Date)null);
    }

    public static Date toDate(String str, Date def) {
        int n = str.length();
        String format;
        if(n == 4) {
            format = "yyyy";
        } else if(n == 7) {
            format = "yyyy-MM";
        } else if(n == 13) {
            format = "yyyy-MM-dd HH";
        } else if(n == 16) {
            format = "yyyy-MM-dd HH:mm";
        } else if(n == 19) {
            format = "yyyy-MM-dd HH:mm:ss";
        } else if(n == 5) {
            format = "HH:mm";
        } else if(n == 9) {
            format = "HH:mm:ss";
        } else {
            format = "yyyy-MM-dd";
        }

        return toDate(str, format, def);
    }

    public static Date toDate(String str, String format, Date def) {
        try {
            return (new SimpleDateFormat(format)).parse(str);
        } catch (Exception var4) {
            return def != null?def:new Date();
        }
    }

    public static String getDateString() {
        return toDateString(new Date());
    }

    public static String getTimeString() {
        return toTimeString(new Date());
    }

    public static String getDateTimeString() {
        return toDateTimeString(new Date());
    }

    public static long sub(Date d1, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long n1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long n2 = cal.getTimeInMillis();
        return n1 - n2;
    }

    public static long sub(String str1, String str2) {
        return sub(toDate(str1), toDate(str2));
    }

    public static String toGMT(Date date) {
        SimpleDateFormat gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        gmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return gmt.format(date);
    }
}
