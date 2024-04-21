package com.yamo.cdcommoncore.utils;

import cn.hutool.core.util.StrUtil;
import com.yamo.cdcommoncore.constants.enums.TimeType;
import com.yamo.cdcommoncore.domain.pojo.TimeBucket;
import com.yamo.cdcommoncore.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 日期转换工具
 *
 * @Description:
 * @author: liuzhimin
 * @date: 2019年5月8日 下午7:36:34
 * @version: 1.0
 */
public class DateUtils {
    private DateUtils() {
        // do nothings
    }

    public static final String YEAR_MOUTH_PATTERN = "yyyy-MM";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String YEAR_TIME_PATTERN = "yyyy";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_PATTERN_S = "yyyyMMdd";

    public static final String DATE_TIME_PATTERN_S = "yyyyMMddHHmmss";

    public static final String DATE_TIME_PATTERN_V2 = "yyyy-MM-dd HH:mm";

    public static final String DATE_TIME = "HH:mm:ss";

    public static final String DATE_TIME_V2 = "HHmmss";

    public static final String DATE_PATTERN_CHINESE = "yyyy年MM月dd日";

    public static final String DATE_TIME_V2_CHINESE = "MM月dd日HH时mm分";

    public static List<String> getDaysStrByTime(String start, String end) {
        Date startTime = DateUtils.parse(start, DATE_TIME_PATTERN);
        Date endTime = DateUtils.parse(end, DATE_TIME_PATTERN);
        Calendar cstart = Calendar.getInstance();
        cstart.setTime(startTime);
        List<String> dateList = new ArrayList<>();
        while (endTime.after(cstart.getTime())) {
            cstart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(format(cstart.getTime(), DATE_PATTERN));
        }
        return dateList;
    }

    public static List<Date> getDaysByTime(Date start, Date end) {
        Calendar cstart = Calendar.getInstance();
        cstart.setTime(start);
        List<Date> dateList = new ArrayList<>();
        dateList.add(start);
        while (end.after(cstart.getTime())) {
            cstart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cstart.getTime());
        }
        return dateList;
    }

    public static TimeBucket getTimeBucket(TimeType timeType) {
        LocalDateTime now = LocalDateTime.now();
        TimeBucket timeBucket = new TimeBucket();
        String startTimeStr = null;
        String endTimeStr = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        switch (timeType) {
            case TODAY:
                LocalDateTime dayStart = now.with(LocalTime.MIN);
                LocalDateTime dayEnd = now.with(LocalTime.MAX);
                startTimeStr = dayStart.format(formatter);
                endTimeStr = dayEnd.format(formatter);
                break;
            case CURRENT_WEEK:
                int dayOfWeek = now.getDayOfWeek().getValue();
                LocalDateTime weekStart = now.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
                LocalDateTime weekEnd = now.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
                startTimeStr = weekStart.format(formatter);
                endTimeStr = weekEnd.format(formatter);
                break;
            case CURRENT_MONTH:
                LocalDateTime monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                LocalDateTime monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                startTimeStr = monthStart.format(formatter);
                endTimeStr = monthEnd.format(formatter);
                break;
            case CURRENT_SEASON:
                startTimeStr = format(getCurrentQuarterStartTime(), DATE_TIME_PATTERN);
                endTimeStr = format(getCurrentQuarterEndTime(), DATE_TIME_PATTERN);
                break;
            case CURRENT_YEAR:
                LocalDateTime yearStart = now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                LocalDateTime yearEnd = now.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
                startTimeStr = yearStart.format(formatter);
                endTimeStr = yearEnd.format(formatter);
                break;
            case CURRENT_N_DAYS:
                LocalDateTime fiveStart = now.plusDays(-1 * (timeType.val - 1)).with(LocalTime.MIN);
                LocalDateTime nowEnd = now.with(LocalTime.MAX);
                startTimeStr = fiveStart.format(formatter);
                endTimeStr = nowEnd.format(formatter);
                break;
            case _12_HOUR:
                LocalDateTime _12hourDate = now.minusHours(12);
                startTimeStr = _12hourDate.format(formatter);
                endTimeStr = now.format(formatter);
                break;
            case _24_HOUR:
                LocalDateTime _24hourDate = now.minusHours(24);
                startTimeStr = _24hourDate.format(formatter);
                endTimeStr = now.format(formatter);
                break;
            case _48_HOUR:
                LocalDateTime _48hourDate = now.minusHours(48);
                startTimeStr = _48hourDate.format(formatter);
                endTimeStr = now.format(formatter);
                break;
            case LAST_MONTH:
                LocalDateTime lastMonthStart = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                LocalDateTime lastMonthEnd = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                startTimeStr = lastMonthStart.format(formatter);
                endTimeStr = lastMonthEnd.format(formatter);
                break;
            case MONTH_OF_LAST_YEAR:
                LocalDateTime lastYearStart = now.minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                LocalDateTime lastYearEnd = now.minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                startTimeStr = lastYearStart.format(formatter);
                endTimeStr = lastYearEnd.format(formatter);
                break;
        }
        timeBucket.setStartTime(startTimeStr);
        timeBucket.setEndTime(endTimeStr);
        return timeBucket;
    }

    /**
     * 获取上一个月的时间
     * @param dateStr
     * @return
     */
    public static String getLastMouth(String dateStr){
        Date date=parse(dateStr,DATE_TIME_PATTERN);
        if(date==null){
            return null;
        }
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,-1);
        Date date1=c.getTime();
        return format(date1,DATE_TIME_PATTERN);
    }

    /**
     * 获取上一年的时间
     * @param dateStr
     * @return
     */
    public static String getLastYear(String dateStr){
        Date date=parse(dateStr,DATE_TIME_PATTERN);
        if(date==null){
            return null;
        }
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR,-1);
        Date date1=c.getTime();
        return format(date1,DATE_TIME_PATTERN);
    }

    /**
     * 获取当前月的天数
     *
     * @return
     */
    public static int getCurrentMonthDays() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        return c.get(Calendar.DATE);
    }


    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = parse(format(c.getTime(), DATE_PATTERN) + " 00:00:00", DATE_TIME_PATTERN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 判断是否为周末
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return week != 6;
    }

    public static Date parse(String str, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(str, new ParsePosition(0));
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime parseOfLocalDateTime(String str, String pattern) {
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date date, String pattern) {
        try {
            return org.apache.commons.lang3.time.DateFormatUtils.format(date, pattern);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return the current date
     *
     * @return － DATE<br>
     * @author lizhihong
     */
    public static Date getCurrentDate() {
        return toDate(LocalDateTime.now());
    }

    /**
     * 获取自定义格式的当前系统日期
     *
     * @return
     */
    public static Date getCurrentDate(String parsePattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(parsePattern);
        return parse(df.format(LocalDateTime.now()), parsePattern);
    }

    /**
     * 获取自定义格式的当前系统日期
     *
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return
     */
    public static Date getCurrentDate(int dayOfMonth) {
        LocalDate date = LocalDate.now();
        LocalDate localDate = LocalDate.of(date.getYear(), date.getMonth(), dayOfMonth);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(TimeZone.getTimeZone("GMT+8:00").toZoneId()).toInstant());
    }

    /**
     * 获取最近n天的日期
     *
     * @return
     */
    public static List<String> getCurrentDays(int days, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        List<String> list = new ArrayList<>(days);
        for (int i = -1 * (days - 1); i <= 0; i++) {
            list.add(dtf.format(now.plusDays(i)));
        }
        return list;
    }

    /**
     * 获取7天的日期
     *
     * @return
     */
    public static List<String> getDateScopeWithWeek(String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        List<String> list = new ArrayList<>(7);
        list.add(dtf.format(now.plusDays(-6)));
        list.add(dtf.format(now.plusDays(-5)));
        list.add(dtf.format(now.plusDays(-4)));
        list.add(dtf.format(now.plusDays(-3)));
        list.add(dtf.format(now.plusDays(-2)));
        list.add(dtf.format(now.plusDays(-1)));
        list.add(dtf.format(now));
        return list;
    }

    /**
     * 获取本月的所有天数
     *
     * @param pattern 日期格式
     * @return 天数集合
     */
    public static List<String> getDayForMonth(String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now().plusMonths(0);
        localDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        LocalDate endLocalDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        dateScopeList.add(dtf.format(localDate));
        int max = 32;
        for (int i = 1; i < max; i++) {
            dateScopeList.add(dtf.format(localDate.plusDays(i)));
            if (localDate.plusDays(i).isEqual(endLocalDate)) {
                break;
            }
        }
        return dateScopeList;
    }

    public static List<String> getNearly30Day(String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        int maxDay = 31;
        for (int i = 1; i < maxDay; i++) {
            dateScopeList.add(dtf.format(localDate.plusDays(-i)));
        }
        Collections.reverse(dateScopeList);
        return dateScopeList;
    }

    /**
     * 获取前六个月的时间集合
     *
     * @param pattern 日期格式
     * @return 前六个月的时间集合
     */
    public static List<String> getFirstSixMonth(String pattern) {
        return getFirstMonth(6, pattern);
    }

    public static List<String> getFirstMonth(int n, String pattern) {
        List<String> dateScopeList = new ArrayList<>();
        LocalDate localDate = LocalDate.now().plusMonths(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        dateScopeList.add(dtf.format(localDate));
        for (int i = 1; i < n; i++) {
            dateScopeList.add(dtf.format(localDate.plusMonths(-i)));
        }
        Collections.reverse(dateScopeList);
        return dateScopeList;
    }

    /**
     * <Description> 获取前xxx分钟字符串时间 <br>
     *
     * @param currentDate 时间字符串
     * @param minute      分钟 <br>
     * @return java.lang.String <br>
     * @author Lizhihong <br>
     * @createDate 2019-12-16 10:55 <br>
     **/
    public static String getFirstSixMinute(String currentDate, int minute) {
        if (StringUtils.isBlank(currentDate)) {
            return currentDate;
        }
        String newStrDate = currentDate;
        long num = 1000 * 60 * minute;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            long time = sdf.parse(newStrDate).getTime();
            long newTime = time - num;
            Date date = new Date(newTime);
            newStrDate = format(date, DATE_TIME_PATTERN);
        } catch (Exception e) {
            //
        }
        return newStrDate;
    }

    /**
     * 获取n小时后的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date getHourAfter(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hours);
        return calendar.getTime();
    }

    /**
     * 获取n小时前的时间
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date getHourBefore(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        return calendar.getTime();
    }

    /**
     * 获取n天前的时间
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDaysBefore(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - days);
        return calendar.getTime();
    }

    /**
     * 获取n分钟后的时间
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date getMinuteAfter(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
        return calendar.getTime();
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        return format.format(new Date());
    }


    /**
     * 获取相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            return day2 - day1;
        }

    }


    public static String getWeekOfDay(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            throw new BizException("日期不能为空");
        }
        String[] weeks = {"00", "01", "02", "03", "04", "05", "06"};
        Date date = parse(dateStr, DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekIndex < 0) {
            weekIndex = 0;
        }
        return weeks[weekIndex];
    }

    public static void main(String[] args) {
        System.out.println(getLastYear("2023-09-12 00:00:00"));
    }

    public static int getLastDayOfMonthInOneYear(int year,int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        return lastDay.getDayOfMonth();
    }

    /**
     * 判断一个日期是否在一个时间范围值内
     * */
    public static boolean betweenAnd(String startTimeStr, String endTimeStr, Date jqTime) {
        Date startTime = DateUtils.parse(startTimeStr, DateUtils.DATE_TIME_PATTERN);
        Date endTime = DateUtils.parse(endTimeStr, DateUtils.DATE_TIME_PATTERN);
        if (endTime.after(jqTime)&&startTime.before(jqTime))
            return true;
        return false;
    }
}
