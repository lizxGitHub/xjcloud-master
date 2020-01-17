package gov.pbc.xjcloud.provider.contract.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
    private static final Logger log                 = LoggerFactory.getLogger(DateUtils.class);
    public static final String  YYYY_MM_DD          = "yyyy-MM-dd";
    public static final String  YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String  YYYYMMDD            = "yyyyMMdd";
    public static final String  YYMMDD              = "yyMMdd";
    public static final String  YYMMDDHHMMSS        = "yyMMddHHmmss";
    public static final String  YYYYMMDDHHMMSS      = "yyyyMMddHHmmss";
    public static final String  YYMMDDHHMMSSSSS     = "yyMMddHHmmssSSS";

    public static int betweenDays(Date sdate, Date edate) {
        Calendar beginDate = getCalendar(sdate);
        Calendar endDate = getCalendar(edate);
        return betweenDays(beginDate, endDate);
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        calendar.set(year, month, day);
        return calendar;
    }

    public static Date addDateMonth(Date date, int monthNum) {
        if (monthNum <= 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, monthNum);
        return calendar.getTime();
    }

    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0L;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginDate = format.parse(beginDateStr);
            Date endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / 86400000L;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

    public static int betweenDays(Calendar beginDate, Calendar endDate) {
        if (beginDate.get(1) == endDate.get(1)) {
            return endDate.get(6) - beginDate.get(6);
        }
        if (beginDate.getTimeInMillis() < endDate.getTimeInMillis()) {
            int days = beginDate.getActualMaximum(6) - beginDate.get(6) + endDate.get(6);
            for (int i = beginDate.get(1) + 1; i < endDate.get(1); i++) {
                Calendar c = Calendar.getInstance();
                c.set(1, i);
                days += c.getActualMaximum(6);
            }
            return days;
        }
        int days = endDate.getActualMaximum(6) - endDate.get(6) + beginDate.get(6);
        for (int i = endDate.get(1) + 1; i < beginDate.get(1); i++) {
            Calendar c = Calendar.getInstance();
            c.set(1, i);
            days += c.getActualMaximum(6);
        }
        return days;
    }

    public static DateFormat getDateFormat(String dateFormat) {
        return new SimpleDateFormat(dateFormat);
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getcurrentTime() {
        String current = dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
        return current;
    }

    public static String getlastMonTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(2, 1);
        String current = dateToString(new Date(cal.getTime().getTime()), "yyyy-MM-dd HH:mm:ss");
        return current;
    }

    public static long getlastMothTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(2, 1);
        return cal.getTime().getTime();
    }

    @SuppressWarnings("deprecation")
    public static Date newDate(Date srcDate, int differdays) {
        if (srcDate == null) {
            return new Date();
        }
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        return date;
    }

    public static String dateToString(Date date, String dateFormat) {
        try {
            if (date == null) {
                return null;
            }
            return getDateFormat(dateFormat).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        try {
            if (date == null) {
                return "";
            }
            return getDateFormat("yyyy-MM-dd").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String dateToString(Date srcDate, int differdays, String dateFormat) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        try {
            return getDateFormat(dateFormat).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String getWeek(Date srcDate, int differdays) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        return getDateFormat("w").format(date);
    }

    public static Date stringToDate(String dateString, String dateFormat) {
        try {
            if (StringUtils.isEmpty(dateString)) {
                return null;
            }
            return getDateFormat(dateFormat).parse(dateString);
        } catch (Exception e) {
            log.error("日期转换错误", e);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static Date getLastDayOfMonth(Date srcDate, int differdays) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        int lastDay = cDay1.getActualMaximum(5);
        Date lastDate = cDay1.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstDayOfMonth(Date srcDate, int differdays) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        int lastDay = cDay1.getActualMinimum(5);
        Date lastDate = cDay1.getTime();
        lastDate.setDate(lastDay);
        return lastDate;
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstDayOfWeek(Date srcDate, int differdays) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int d = 0;
        if (calendar.get(7) == 1) {
            d = -6;
        } else {
            d = 2 - calendar.get(7);
        }
        calendar.add(7, d);
        Date firstDateOfWeek = calendar.getTime();
        return firstDateOfWeek;
    }

    @SuppressWarnings("deprecation")
    public static Date getLastDayOfWeek(Date srcDate, int differdays) {
        Date date = new Date(srcDate.getTime());
        date.setDate(date.getDate() + differdays);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int d = 0;
        if (calendar.get(7) == 1) {
            d = -6;
        } else {
            d = 2 - calendar.get(7);
        }
        calendar.add(7, d);

        calendar.add(7, 6);
        Date lastDateOfWeek = calendar.getTime();
        return lastDateOfWeek;
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstMonthOfYear(Date srcDate, int differmonths) {
        Date date = new Date(srcDate.getTime());
        date.setMonth(date.getMonth() + differmonths);
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        date.setDate(cDay1.getActualMinimum(5));
        date.setMonth(cDay1.getActualMinimum(2));
        return getFirstSecondOfOneDay(date);
    }

    @SuppressWarnings("deprecation")
    public static Date getLastMonthOfYear(Date srcDate, int differmonths) {
        Date date = new Date(srcDate.getTime());
        date.setMonth(date.getMonth() + differmonths);
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(date);
        date.setDate(cDay1.getActualMaximum(5));
        date.setMonth(cDay1.getActualMaximum(2));
        return getLastSecondOfOneDay(date);
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstSecondOfOneDay(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar c = Calendar.getInstance();
        date.setHours(c.getActualMinimum(11));
        date.setMinutes(c.getActualMinimum(12));
        date.setSeconds(c.getActualMinimum(13));
        return date;
    }

    @SuppressWarnings("deprecation")
    public static Date getFirstSecondOfHour(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar c = Calendar.getInstance();
        date.setMinutes(c.getActualMinimum(12));
        date.setSeconds(c.getActualMinimum(13));
        return date;
    }

    @SuppressWarnings("deprecation")
    public static Date getLastSecondOfHour(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar c = Calendar.getInstance();
        date.setMinutes(c.getActualMaximum(12));
        date.setSeconds(c.getActualMaximum(13));
        return date;
    }

    @SuppressWarnings("deprecation")
    public static Date getLastSecondOfOneDay(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar c = Calendar.getInstance();
        date.setHours(c.getActualMaximum(11));
        date.setMinutes(c.getActualMaximum(12));
        date.setSeconds(c.getActualMaximum(13));
        return date;
    }

    public static Date newDate(String timeZone) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone), Locale.CHINESE);
        Calendar day = Calendar.getInstance();
        day.set(1, cal.get(1));
        day.set(2, cal.get(2));
        day.set(5, cal.get(5));
        day.set(11, cal.get(11));
        day.set(12, cal.get(12));
        day.set(13, cal.get(13));
        return day.getTime();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws ParseException {
        System.out.println(getFirstDayOfWeek(new Date(), 0).toLocaleString());
        System.out.println(getFirstDayOfWeek(new Date(), 0).toLocaleString());
    }

    public static Date addDays(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, day);
        return calendar.getTime();
    }

    public static String getDate(String formatDate) {
        return getDateFormat(formatDate).format(new Date());
    }

    /**   
     * @Description:  获得该时间是星期几
     * @param date
     * @return  
     * @return: int    
     * @author: Alex.wen  
     * @date:   2019年2月22日 下午3:49:46
     */
    public static int getDateWeekInt(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**   
     * @Description: 得到几天后的时间 
     * @param d
     * @param day
     * @return  
     * @return: Date    
     * @author: Alex.wen  
     * @date:   2019年3月9日 下午4:57:02
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);//+后 -前
        return now.getTime();
    }

    public static Date getFirstDayOfLastMonth(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
        return calendar.getTime();
    }

    public static Date getLastDayOfLastMonth(Date srcDate) {
        Date date = new Date(srcDate.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    public static Long getSecondValue(Date date) {
        return date.getTime() / 1000;
    }
}
