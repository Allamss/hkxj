package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.dao.CourseTimeTableDao;
import cn.hkxj.platform.dao.StudentUserDao;
import cn.hkxj.platform.dao.UrpClassRoomDao;
import cn.hkxj.platform.pojo.CourseTimetable;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.pojo.UrpClassroom;
import cn.hkxj.platform.pojo.vo.CourseTimeTableVo;
import cn.hkxj.platform.spider.newmodel.coursetimetable.UrpCourseTimeTableForSpider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2019/9/2 16:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@WebAppConfiguration
@Slf4j
public class CourseTimeTableServiceTest {

    @Resource
    private CourseTimeTableService courseTimeTableService;
    @Resource
    private StudentUserDao studentUserDao;
    @Resource
    private CourseTimeTableDao courseTimeTableDao;
    @Resource
    private UrpClassRoomDao urpClassRoomDao;


    @Test
    public void getCourseTimeTableByStudent() {
        StudentUser student = studentUserDao.selectStudentByAccount(2017022846);
        for (CourseTimeTableVo courseTimeTableVo : courseTimeTableService.getCurrentTermCourseTimeTableByStudent(student)) {
            System.out.println(courseTimeTableVo);
        }

    }


    @Test
    public void fix() {
        for (CourseTimetable courseTimetable : courseTimeTableDao.selectByCourseTimetable(new CourseTimetable())) {
            List<UrpClassroom> classroomList = urpClassRoomDao.selectByClassroom(new UrpClassroom().setName(courseTimetable.getRoomName()));

            if (classroomList.size() == 0) {
                log.error("{} size 0", courseTimetable.getRoomName());
            } else if (classroomList.size() == 1) {
                if (!classroomList.get(0).getNumber().equals(courseTimetable.getRoomNumber())) {
                    log.error("{} number error", courseTimetable);
                }
            } else {
                log.error("{} size more than 1", classroomList);
            }
        }

    }



    @Test
    public void getCourseTimeTableByStudentFromSpider() {
        StudentUser student = studentUserDao.selectStudentByAccount(2017021881);
        for (CourseTimeTableVo vo : courseTimeTableService.getCourseTimeTableByStudentFromSpider(student)) {
            System.out.println(vo);
        }

    }

    @Test
    public void testUpdate() {
        for (CourseTimeTableVo vo : courseTimeTableService.getCurrentTermCourseTimeTableByStudent(2017023437)) {
            System.out.println(vo);
        }

        System.out.println("####################");

        for (CourseTimeTableVo vo : courseTimeTableService.updateCourseTimeTableByStudent(2017023437)) {
            System.out.println(vo);
        }


    }


    @Test
    public void fixErrorData() {
        // 按学生分好组，然后再进行抓取
        StudentUser student = studentUserDao.selectStudentByAccount(2017021881);
        UrpCourseTimeTableForSpider details = courseTimeTableService.getCourseTimeTableDetails(student);
        List<CourseTimetable> collect = details.adaptToList().stream().filter(CourseTimetable::isCurrentTerm).collect(Collectors.toList());

        System.out.println(collect);


    }

    @Test
    public void getCurrentTermCourseTimetableByClass() {
        // 按学生分好组，然后再进行抓取
        List<CourseTimetable> timetable = courseTimeTableService.getCurrentTermCourseTimetableByClass("2017040012");
        System.out.println(timetable.size());
    }


}