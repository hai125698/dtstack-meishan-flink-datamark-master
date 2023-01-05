package com.ds.flink.meishan.until;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: timeUntil
 * @Description: 时间相关转换操作
 * @author: ds-longju
 * @Date: 2022-10-24 19:34
 * @Version 1.0
 **/
public class TimeUntil {
    /***
     *  日期转时间戳
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /***
     * 获取当前时间
     * @return date
     */
    public static String getCurrentStamp(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) ;
    }
}
