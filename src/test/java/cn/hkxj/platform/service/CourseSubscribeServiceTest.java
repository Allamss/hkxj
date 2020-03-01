package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.vo.CourseTimeTableVo;
import cn.hkxj.platform.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @author Yuki
 * @date 2019/5/9 22:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@WebAppConfiguration
public class CourseSubscribeServiceTest {

    @Resource
    private CourseSubscribeService courseSubscribeService;

    @Test
    public void currentWeekCourse() {
        WechatOpenid wechatOpenid = new WechatOpenid().setAccount(2019026628);
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();
        schoolTime.setDay(3);
        schoolTime.setSchoolWeek(8);
        CourseTimeTableVo table = courseSubscribeService.getTableBySection(wechatOpenid, 3, schoolTime);
        assert table != null;

    }

    @Test
    public void notInCurrentWeekCourse() {
        WechatOpenid wechatOpenid = new WechatOpenid().setAccount(2019026628);
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();
        schoolTime.setDay(3);
        schoolTime.setSchoolWeek(9);
        CourseTimeTableVo table = courseSubscribeService.getTableBySection(wechatOpenid, 3, schoolTime);
        System.out.println(table);
        assert table == null;

    }

    @Test
    public void getCourseTimeTables() {
        for (WechatOpenid wechatOpenid : courseSubscribeService.getSubscribeOpenid()) {
            System.out.println(wechatOpenid);
        }

    }
}