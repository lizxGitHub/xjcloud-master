package gov.pbc.xjcloud.provider.contract.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 *
 * @author paungmiao@163.com
 * @date
 */
public class TimeUtil {
    public static String formatDate(Date date) {
        DateTime dateTime = new DateTime(date);
        String formatStr = "yyyy-MM-dd HH:mm:ss";
        return dateTime.toString(formatStr);
    }
    public static Date parse(String strDate) {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = DateTime.parse(strDate, dateTimeFormat);
        return dateTime.toDate();
    }

    public static Date parse(String strDate, String format) {
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(format);
        DateTime dateTime = DateTime.parse(strDate, dateTimeFormat);
        return dateTime.toDate();
    }
}
