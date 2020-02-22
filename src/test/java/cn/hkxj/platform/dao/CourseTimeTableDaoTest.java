package cn.hkxj.platform.dao;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.CourseTimetable;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JR Chan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@Slf4j
public class CourseTimeTableDaoTest {
    @Resource
    private CourseTimeTableDao courseTimeTableDao;

    @Test
    public void selectByPrimaryKey() {
    }

    @Test
    public void selectBatch() {
        ArrayList<CourseTimetable> list1 = Lists.newArrayList();
        list1.add(courseTimeTableDao.selectByPrimaryKey(1));
        list1.add(courseTimeTableDao.selectByPrimaryKey(3));

        List<CourseTimetable> list = courseTimeTableDao.selectBatch(list1);
        System.out.println(list.size());
        System.out.println(list);
    }
}