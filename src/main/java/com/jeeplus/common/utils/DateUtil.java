/**
 * 
 */
package com.jeeplus.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * @Description: TODO
 * @author lzp
 * @date 2018年5月15日
 * @version V1.0
 *
 */
public class DateUtil {
	
	public static final String STANDARD_SIMPLE_FORMAT = "yyyy-MM-dd";
	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static void main(String[] args) throws ParseException {  
        // TODO Auto-generated method stub  
        SimpleDateFormat sdf=new SimpleDateFormat(STANDARD_FORMAT);  
        Date d1=sdf.parse("2012-09-08 10:10:10");  
        Date d2=sdf.parse("2012-09-15 00:00:00");  
         
        System.out.println(timesBetween(d1,d2));
        System.out.println(timesBetween("2012-09-08 10:10:10","2012-09-15 00:00:00"));
        
        System.out.println(DateUtil.dateToStr(new Date(),"yyyy-MM-dd"));
        System.out.println(DateUtil.simpleStrToDate("2010-01-01"));
        System.out.println(DateUtil.strToDate("2010-01-01 11:11:11"));
        
    }  
	
	/**
	 * 计算两个日期之间相差的时间
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static Map timesBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_FORMAT);
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		
		long minus = time2 - time1;
		Map map = new ConcurrentHashMap<>();
		long second = minus / 1000;
		long minute = second / 60;
		long hour = minute / 60;
		long days = hour / 24;
		map.put("second", second);	//距离秒
		map.put("minute", minute);	//距离分钟
		map.put("hour", hour);		//距离小时
		map.put("days", days);		//距离天数
		
		return map;
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static Map timesBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		
		
		long minus = time2 - time1;
		Map map = new ConcurrentHashMap<>();
		long second = minus / 1000;
		long minute = second / 60;
		long hour = minute / 60;
		long days = hour / 24;
		map.put("second", second);	//距离秒
		map.put("minute", minute);	//距离分钟
		map.put("hour", hour);		//距离小时
		map.put("days", days);		//距离天数
		
		return map;
	}
	
    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date,String formatStr){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    
    public static Date simpleStrToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_SIMPLE_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    
    public static String dateToSimpleStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_SIMPLE_FORMAT);
    }

 
}
