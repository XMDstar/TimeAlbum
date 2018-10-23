package com.zcc.albumlibrary.album.utils;

import java.util.Calendar;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 日期格式化工具类 同年 eg:10月01日 星期一   不同年 eg:2017年10月13日 星期五
 */
public class DateUtil {

    static String YEAR_CHAR = "年";
    static String MONTH_CHAR = "月";
    static String DAY_CHAR = "日";
    static String[] weekDays = {" 星期日", " 星期一", " 星期二", " 星期三", " 星期四", " 星期五", " 星期六"};
    static String NOW_DAY_CHAR = "今天";

    public static String formatDate(long time) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowDay = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTimeInMillis(time);
        StringBuilder sb = new StringBuilder();
        if (nowDay == cal.get(Calendar.DAY_OF_YEAR)) {
            sb.append(NOW_DAY_CHAR);
        } else {
            if (nowYear != cal.get(Calendar.YEAR)) {
                sb.append(cal.get(Calendar.YEAR));
                sb.append(YEAR_CHAR);
            }
            sb.append(cal.get(Calendar.MONTH) + 1);
            sb.append(MONTH_CHAR);
            sb.append(cal.get(Calendar.DATE));
            sb.append(DAY_CHAR);
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        sb.append(weekDays[w]);
        return sb.toString();
    }
}