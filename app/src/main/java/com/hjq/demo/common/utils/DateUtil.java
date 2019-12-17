package com.hjq.demo.common.utils;

import com.haibin.calendarview.Calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getCurrentDate() {
        return convertDate2String(new Date());
    }

    public static String convertDate2String(Date date) {
        return sdf.format(date);
    }

    public static String convertCalendar2String(Calendar calendar) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        sb.append(month < 10 ? "0" + month : month).append("-");
        sb.append(day < 10 ? "0" + day : day);
        return sb.toString();
    }
}
