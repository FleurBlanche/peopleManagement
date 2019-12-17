package com.hjq.demo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hjq.demo.common.entity.TimeRange;

public class TimeRangeUtil {
    // 预约时间为从早上8点到晚上10点，以半小时为时间间隔
    public static int ONE_DAY_TIME_RANGE_SIZE = 28;

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static String convertDateNumber2String(int dateNumber) {
        String HH = String.valueOf(dateNumber / 2);
        if (HH.length() == 1) {
            HH = "0" + HH;
        }
        String mm = dateNumber % 2 == 0 ? "00" : "30";
        return HH + ":" + mm;
    }

    public static String convertIndex2String(int index) {
        return convertDateNumber2String(convertIndex2DateNumber(index));
    }

    public static String convertTimeRange2Time(TimeRange timeRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(convertDateNumber2String(timeRange.getStart()));
        sb.append(" ~ ");
        sb.append(convertDateNumber2String(timeRange.getEnd()));
        return sb.toString();
    }

    // 向后取整
    public static int convertTime2Index(Date date) {
        String time = sdf.format(date);
        String[] splitter = time.split(":");
        int dateNumber = Integer.valueOf(splitter[0]) * 2 + (Integer.valueOf(splitter[1]) + 30) / 30;
        return convertDateNumber2Index(dateNumber);
    }

    public static int convertDateNumber2Index(int dateNumber) {
        return dateNumber - 16;
    }

    public static int convertIndex2DateNumber(int index) {
        return index + 16;
    }
}
