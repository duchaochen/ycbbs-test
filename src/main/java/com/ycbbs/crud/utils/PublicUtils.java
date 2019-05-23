package com.ycbbs.crud.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 共用类
 */
public class PublicUtils {
    /**
     * 获取时间
     * @param format:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateTime(String format,Date date) {
        /**
         * 设置日期格式
         */
        SimpleDateFormat df = new SimpleDateFormat(format);
        /**
         * 为获取当前系统时间
         */
        String time = df.format(date == null ? new Date() : date);
        return time;
    }
}
