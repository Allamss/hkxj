package cn.hkxj.platform.utils;

import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.Term;
import cn.hkxj.platform.pojo.constant.RedisKeys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Yuki
 * @date 2018/11/5 23:22
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
@Component
public class DateUtils {

    static RedisTemplate redisTemplate;

    static StringRedisTemplate stringRedisTemplate;

//    private final static String term_start = "2019-08-26";

    public final static String YYYY_MM_DD_PATTERN = "yyyyMMdd";

    public final static String PATTERN_WITHOUT_SPILT = "yyyyMMddHHmmSS";

    public final static String YYYY_MM_PATTERN = "yyyyMM";

    public final static String DEFAULT_PATTERN = "yyyy-MM-dd";

    public final static String HH_MM_SS_PATTERN = "hh:mm:ss";


    @Autowired
    public  void setTemplate(RedisTemplate redisTemplate){
        DateUtils.redisTemplate = redisTemplate;
    }
    @Autowired
    public  void setTemplate(StringRedisTemplate stringRedisTemplate){
        DateUtils.stringRedisTemplate = stringRedisTemplate;
    }

    /*
     *  学期开始时间存入redis，可动态配置
     */
    public static   void  setTermStart(String termStart)  {
        stringRedisTemplate.opsForValue().set(RedisKeys.TERM_STARY.getName(),termStart);

    }

    public static    String getTermStart()  {
        return stringRedisTemplate.opsForValue().get(RedisKeys.TERM_STARY.getName());
    }


    public static Integer getCurrentWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(format.parse(getTermStart()));
        } catch (ParseException e) {
            log.error("parse string to date fail，error message{}", e.getMessage());
            throw new RuntimeException("parse string to date fail，error message" + e.getMessage());
        }
        long start = calendar.getTimeInMillis();
        long end = Calendar.getInstance().getTimeInMillis();
        return (int) Math.ceil(((end - start) / 1000 / 60 / 60 / 24 / 7)) + 1;
    }

    /**
     * 获取当天是周几
     *
     * @return 星期几
     */
    public static Integer getCurrentDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //星期天是1，星期一是2，星期二是3，星期三是4，星期四是5，星期五是6，星期六是7
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (currentDay < 0) {
            currentDay = 0;
        }
        return currentDay;
    }

    public static String getTimeOfPattern(LocalDateTime localDateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    public static LocalDateTime string2LocalDateTime(String time, String pattern) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
    }


    /**
     * 格式化时间转换为标准Java时间
     * @return
     */
    public static Date localDateToDate(String time, String pattern) {

        SimpleDateFormat format =  new SimpleDateFormat(pattern);


        try {
            return format.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    public static SchoolTime getCurrentSchoolTime() {
//        SchoolTime schoolTime = new SchoolTime();
//        schoolTime.setDay(getCurrentDay());
//        schoolTime.setWeek(getCurrentWeek());
//        schoolTime.setTerm(new Term(2019, 2020, 1));
//        return schoolTime;
//    }

    /*
     *  根据学期获取周
     */
    public static void setSchoolTime(Term term) {
        redisTemplate.opsForValue().set(RedisKeys.TERM.getName(),term);
    }

    public static SchoolTime getCurrentSchoolTime()  {
        SchoolTime schoolTime=new SchoolTime();
        Term term=(Term)redisTemplate.opsForValue().get(RedisKeys.TERM.getName());
        schoolTime.setTerm(term);
        schoolTime.setWeek(getCurrentWeek());
        schoolTime.setDay(getCurrentDay());
        return schoolTime;
    }

    public static String dateToChinese(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        StringBuffer buffer = new StringBuffer();
        buffer.append(cal.get(Calendar.YEAR)).append("年")
                .append(cal.get(Calendar.MONTH)+1).append("月")
                .append(cal.get(Calendar.DAY_OF_MONTH)).append("日")
                .append(cal.get(Calendar.HOUR_OF_DAY)).append("时")
                .append(cal.get(Calendar.MINUTE)).append("分");
        return buffer.toString();
    }

    public static byte getDistinct() {
        return (byte) (getCurrentWeek() % 2 == 0 ? 2 : 1);
    }

    public static byte getContraryDistinct() {
        return (byte) (getCurrentWeek() % 2 == 0 ? 1 : 2);
    }

    @Test
    public   void demo(){
        Date date = localDateToDate("20191105102840", PATTERN_WITHOUT_SPILT);
        System.out.println(date);
        System.out.println(dateToChinese(date));
        String s="2019-8-26";
        setTermStart(s);
        System.out.println(getTermStart());
        Term term=new Term(2000,2001,1);
        setSchoolTime(term);
        System.out.println(getCurrentSchoolTime());

    }
}
