package com.checkcode.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

    public static String FORMATTER_STYLE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMATTER_STYLE);
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 获取去年现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getLastYearStringDate() {
        Calendar c = Calendar.getInstance();
        //过去一年
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date dateTime = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMATTER_STYLE);
        String dateString = formatter.format(dateTime);
        return dateString;
    }

    /**
     * 解析时间
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMATTER_STYLE);
        return formatter.parse(date);
    }

}
