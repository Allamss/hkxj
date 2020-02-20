package cn.hkxj.platform.utils;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.Term;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
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
public class DateUtilsTest2 {
   @Autowired
   RedisTemplate<Object,Object> redisTemplate;
    @Autowired
   StringRedisTemplate  stringRedisTemplate;

//    private final static String term_start="2019-5-26" ;

    public final static String YYYY_MM_DD_PATTERN = "yyyyMMdd";

    public final static String PATTERN_WITHOUT_SPILT = "yyyyMMddHHmmSS";

    public final static String YYYY_MM_PATTERN = "yyyyMM";

    public final static String DEFAULT_PATTERN = "yyyy-MM-dd";

    public final static String HH_MM_SS_PATTERN = "hh:mm:ss";


    public  Integer getCurrentWeek() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        try {
            calendar.setTime(format.parse(getTerm_start()));
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
    public  Integer getCurrentDay() {
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

    public  String getTimeOfPattern(LocalDateTime localDateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    public  LocalDateTime string2LocalDateTime(String time, String pattern) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
    }


    /**
     * 格式化时间转换为标准Java时间
     * @return
     */
    public  Date localDateToDate(String time, String pattern) {

        SimpleDateFormat format =  new SimpleDateFormat(pattern);


        try {
            return format.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public  SchoolTime getCurrentSchoolTime() {
        SchoolTime schoolTime = new SchoolTime();
        schoolTime.setDay(getCurrentDay());
        schoolTime.setWeek(getCurrentWeek());
        schoolTime.setTerm(new Term(2019, 2020, 1));
        return schoolTime;
    }


    public  String dateToChinese(Date date){
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

    public  byte getDistinct() {
        return (byte) (getCurrentWeek() % 2 == 0 ? 2 : 1);
    }

    public  byte getContraryDistinct() {
        return (byte) (getCurrentWeek() % 2 == 0 ? 1 : 2);
    }

    public  void  setTerm_start(String term_start)  {
        stringRedisTemplate.opsForValue().set("term_start",term_start);
    }
    public  String getTerm_start()  {
        return stringRedisTemplate.opsForValue().get("term_start");
    }
    public void setSchoolTime(Term term) {
        Set set=new TreeSet();
        SchoolTime schoolTime=new SchoolTime();
        schoolTime.setTerm(term);
        schoolTime.setDay(getCurrentDay());
        schoolTime.setWeek(getCurrentWeek());
        set.add(schoolTime);
      redisTemplate.opsForValue().set("schoolTime",set);
    }
    public SchoolTime getSchoolTime()  {
        SchoolTime schoolTime=null;
        TreeSet set=(TreeSet)redisTemplate.opsForValue().get("schoolTime");
        Iterator iterator=set.iterator();
        while (iterator.hasNext()){
            schoolTime=(SchoolTime)iterator.next();
        }
return schoolTime;
    }
@Test
    public void demo(){
    Date date = localDateToDate("20191105102840", PATTERN_WITHOUT_SPILT);
    System.out.println(date);
    System.out.println(dateToChinese(date));
        String s="2019-5-26";
        setTerm_start(s);
        System.out.println(getTerm_start());

    Term term=new Term(2000,2001,1);
    setSchoolTime(term);
    System.out.println(getSchoolTime());

    }
}
