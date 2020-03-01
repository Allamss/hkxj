package cn.hkxj.platform.service;

import cn.hkxj.platform.config.wechat.WechatMpPlusProperties;
import cn.hkxj.platform.dao.StudentUserDao;
import cn.hkxj.platform.dao.WechatOpenIdDao;
import cn.hkxj.platform.pojo.ScheduleTask;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.constant.SubscribeScene;
import cn.hkxj.platform.pojo.vo.CourseTimeTableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yuki
 * @date 2018/12/5 19:26
 */
@Slf4j
@Service
public class CourseSubscribeService {

    /**
     * 第一节
     */
    public final static int FIRST_SECTION = 1;
    /**
     * 第二节
     */
    public final static int SECOND_SECTION = 3;
    /**
     * 第三节
     */
    public final static int THIRD_SECTION = 5;
    /**
     * 第四节
     */
    public final static int FOURTH_SECTION = 7;
    /**
     * 第五节
     */
    public final static int FIFTH_SECTION = 9;

    @Resource
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Resource
    private WechatOpenIdDao wechatOpenIdDao;
    @Resource
    private CourseTimeTableService courseTimeTableService;
    @Resource
    private StudentUserDao studentUserDao;


    /**
     * 生成一个openid和学生实体映射的map
     */
    public List<WechatOpenid> getSubscribeOpenid() {
        ScheduleTask task = new ScheduleTask();
        ScheduleTask scene = task.setAppid(wechatMpPlusProperties.getAppId())
                .setScene(Integer.valueOf(SubscribeScene.COURSE_PUSH.getScene()));

        return wechatOpenIdDao.selectBySubscribe(scene);
    }



    public CourseTimeTableVo getTableBySection(WechatOpenid wechatOpenid, int section, SchoolTime schoolTime){
        StudentUser user = studentUserDao.selectStudentByAccount(wechatOpenid.getAccount());
        return getTableBySection(user, section, schoolTime);
    }

    public CourseTimeTableVo getTableBySection(StudentUser studentUser, int section, SchoolTime schoolTime){
        List<CourseTimeTableVo> tableVoList = courseTimeTableService.getCurrentTermCourseTimeTableByStudent(studentUser);

        List<CourseTimeTableVo> list = tableVoList.stream()
                .filter(x -> section == x.getClassOrder())
                .filter(x -> schoolTime.getDay() == x.getClassDay())
                .filter(x -> x.getClassInSchoolWeek().charAt(schoolTime.getSchoolWeek() - 1) == '1')
                .collect(Collectors.toList());

        if (list.size() > 1){
            log.info("more than one course {}", list);
        }

        return list.stream().findFirst().orElse(null);
    }
}
