package com.sumscope.bab.quote.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shaoxu.wang on 2016/12/19.
 * 报价日期工具类
 */
public final class QuoteDateUtils {
    private QuoteDateUtils(){}
    /**
     * 判断两个日期是否是同一天
     * @param date1 日期1
     * @param date2 日期2
     * @return 同一天：true
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 设置某一日期的时间为23：59：59
     */
    public static Date getLatestTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 23, 59, 59);
    }

    public static Date getDateWithSpecifiedTime(Date date, int hour, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static Date getBigoningDateWithSpecifiedTime(Date date){
        return getDateWithSpecifiedTime(date, 0,0, 0);
    }
    /**
     * 获取某一日期时间为一日开始时间，既00：00：01
     */
    public static Date getBeginingTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 0, 0, 1);
    }

    public static Date getBeginingTimeByDate(Date date) {
        return getDateWithSpecifiedTime(date, 0, 0, 0);
    }
    /**
     * 设置日期为用于矩阵计算的时间，既18:00:00，该时间是工作日结束进行结转的时间
     */
    public static Date getCalculationTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 18, 0, 0);
    }

    /**
     * 设置日期为过期时间，既23:00:00
     */
    public static Date getExpiredTimeOfDate(Date date) {
        return getDateWithSpecifiedTime(date, 23, 0, 0);
    }

    /**
     * 获取昨日过期日期，时间为23：00：00
     */
    public static Date getYesterdayExpiredTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return getExpiredTimeOfDate(calendar.getTime());
    }

    /**
     *获取昨天日期
     */
    public static Date getYesterdayTime(){
        return getTime(Calendar.DATE,-1);
    }

    /**
     *获取上个月日期
     */
    public static Date getLastMonthTime(){
       return getTime(Calendar.MONTH,-1);
    }
    /**
     *获取七天
     */
    public static Date getLastWeekTime(){
        return getTime(Calendar.DATE,-7);
    }

    public static Date getTime(int i,int j){
        Calendar cal = Calendar.getInstance();
        cal.add( i,j);
        return cal.getTime();
    }

    public static Date parserSimpleDateFormatString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    /**
     * 计算两个日期的相差天数，本计算不考虑时间，既计算前将时间均设置为23：59：59；
     * @param firstDate 参数1
     * @param secondDate 参数2
     * @return 两个输入日期相差的天数 (参数2 - 参数1)
     */
    public static int calculateDiffDays(Date firstDate, Date secondDate) {
        //计算天数 = 到期日 - 交易日
        //计算两个日期区分时需要把时间部分的影响取消，否则使用24小时作为一天会有问题，比如2.1日22：00：00 - 2.2日 11：00：00
        //是两天，但是按24小时为一天计算时则不足一天
        Calendar calendar1 = getCalendarInstanceForDateWithSpecifiedTime(firstDate);
        Calendar calendar2 = getCalendarInstanceForDateWithSpecifiedTime(secondDate);

        long timeInMillis1 = calendar1.getTimeInMillis();
        long timeInMillis2 = calendar2.getTimeInMillis();
        long diffDays = (timeInMillis2 - timeInMillis1) / (24 * 60 * 60 * 1000);
        return Integer.valueOf(String.valueOf(diffDays));
    }

    private static Calendar getCalendarInstanceForDateWithSpecifiedTime(Date date) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.set(Calendar.HOUR, 23);
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        return calendar1;
    }

    public static Calendar getDateTime(int i){
        Calendar calendar = setYearCalendar(i);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

    private static Calendar setYearCalendar(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, i);
        return calendar;
    }

    public static Calendar getIsDateTime(int i){
        Calendar calendar = setYearCalendar(i);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar;
    }

    public static String getDateTimeToString(int i){
        Calendar calendar = getDateTime(i);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    public static String getDateTimeToStringForAnalysis(int i){
        Calendar calendar = getDateTime(i);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String months= month<10 ? "0"+month : month+"";
        String days= day<10 ? "0"+day : day+"";
        return year + "" + months + "" + days;
    }
    public static Calendar getCalendar(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar;
    }

    public static String getDateTimeToStringForAnalysis(Date date){
       SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     *
     *判断给定日期是否为月末的一天
     * @param date
     * @return true=是,false=不是
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }
    /**
     *
     *获取某一日的是星期几
     * @param date
     * @return 1 - 7
     */
    public static int getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }
}
