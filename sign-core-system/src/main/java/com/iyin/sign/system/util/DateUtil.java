package com.iyin.sign.system.util;

import com.iyin.sign.system.model.code.BaseResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author: Koala
 * @Date: 14-8-23 下午10:56
 * @Version: 1.0
 */
public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * @return
     * @Description 获取当前中国时区的TIMESTAMP日期
     */
    public static Timestamp getSysTimestamp() {
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");//获取中国时区
        TimeZone.setDefault(zone);//设置时区
        return new Timestamp((new Date()).getTime());
    }

    /***
     * @author Barley
     * @detail 获取 服务器日期
     */
    public static String getSysDate() {
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");//获取中国时区
        TimeZone.setDefault(zone);//设置时区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 格式化日期，默认的格式为 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formateDate(Date date) {
        return formatDate(date, DateFormatType.DATE);
    }

    /**
     * 格式化日期，默认的格式为 yyyyMM
     *
     * @param date
     * @return
     */
    public static String formateYYYYMMDate(Date date) {
        return formatDate(date, DateFormatType.DATA_Y);
    }


    /**
     * 格式化指定格式的日期
     *
     * @param date 时间
     * @param type 日期格式,例： yyyyMMddHHmmss
     * @return String    格式后的字符串日期
     */
    public static String formatDate(Date date, DateFormatType type) {
        SimpleDateFormat format = new SimpleDateFormat(type.getValue());
        return format.format(date);
    }

    /**
     * 格式long类型日期为 Date
     *
     * @param time long类型日期
     * @return Date
     */
    public static Date formatDate(long time) {
        return new Date(time);
    }

    /**
     * 将字符串用指定的formatType解析成日期
     *
     * @param time
     * @param type
     * @return
     */
    public static Date parseDate(String time, DateFormatType type) {
        SimpleDateFormat format = new SimpleDateFormat(type.getValue());
        try {
            if (StringUtils.isNotEmpty(time)) {
                return format.parse(time);
            } else {
                return null;
            }
        } catch (ParseException e) {
            logger.error("解析日期出错：" + e.getMessage());
            return null;
        }
    }

    public static String getTheDayMinTimeToDateString(String date) {
        return "to_date('" + date + " 00:00:00','yyyy-MM-dd HH24:mi:ss')";
    }

    public static String getTheDayMaxTimeToDateString(String date) {
        return "to_date('" + date + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
    }

    /**
     * 方法用途: 获取应用服务器的当前时间<br>
     * 实现步骤: <br>
     *
     * @return
     */
    public static Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 方法用途:获取的前几个小时 eg:Date 2013-10-12 10:22:00  Then return: 2013-10-12 8:22:00 <br>
     * 实现步骤: <br>
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date getBeforeHour(Date date, int hour) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - hour);

        return c.getTime();
    }

    public static int getHour(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 方法用途: 得到几小时后的时间<br>
     * 实现步骤: <br>
     *
     * @param date 时间
     * @param hour 小时
     * @return 几小时后的时间
     */
    public static Date getAfterHour(Date date, int hour) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + hour);

        return c.getTime();

    }

    public static Date getAfterDay(Date date, int day) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + day);

        return c.getTime();
    }

    /**
     * 方法用途:获取的前几个小时 eg:Date 2013-10-12 10:22:00  Then return: 2013-10-11 10:22:00 <br>
     * 实现步骤: <br>
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getBeforeDay(Date date, int day) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - day);

        return c.getTime();
    }

    /**
     * 方法用途: 获取的前几个月 eg:Date 2013-10-12 10:22:00  Then return: 2013-08-12 10:22:00<br>
     * 实现步骤: <br>
     *
     * @param date
     * @param month
     * @return
     */
    public static Date getBeforeMonth(Date date, int month) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - month);

        return c.getTime();
    }

    /**
     * 方法用途: 获取的前几个后
     * 实现步骤: <br>
     *
     * @param date
     * @param month
     * @return
     */
    public static Date getAfterMonth(Date date, int month) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + month);

        return c.getTime();
    }

    /**
     * 方法用途: 获取的几年前
     * 实现步骤: <br>
     *
     * @param date
     * @param year
     * @return
     */
    public static Date getBeforeYear(Date date, int year) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - year);

        return c.getTime();
    }

    /**
     * 方法用途: 获取的几年后
     * 实现步骤: <br>
     *
     * @param date
     * @param year
     * @return
     */
    public static Date getAfterYear(Date date, int year) {
        if (date == null) {
            date = getCurrentTime();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + year);

        return c.getTime();
    }


    public static String getQueryMonthSql(String tableColumn, String queryMonth) {
        return " and TO_CHAR(" + tableColumn + ", '" + DateFormatType.MONTH.getValue() + "') ='" + queryMonth + "'";
    }

    public static Date getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date d = null;
        try {
            d = sdf.parse(sdf.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date parse(String date) throws ParseException {
        return new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
    }

    public static Date parses(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException(BaseResponseCode.DATE_PARSE_ERROR);
        }
        return date1;
    }

    public static void main(String[] args) {
        System.out.println(formatDate(new Date(),DateFormatType.DATETIMESTR));
        System.out.println("---------------" + getSysTimestamp());
        System.out.println("===============" + formateYYYYMMDate(new Date()));
        Date date = new Date();
        System.out.println("现在 = " + formateDate(date));
        System.out.println("两年后 = " + formateDate(getAfterYear(date, 2)));

        System.out.println("现在：" + date);
        System.out.println("两年后：" + getAfterYear(date, 2));
    }
}
