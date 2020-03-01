package cn.hkxj.platform.task;

import cn.hkxj.platform.MDCThreadPool;
import cn.hkxj.platform.builder.TemplateBuilder;
import cn.hkxj.platform.config.wechat.WechatTemplateProperties;
import cn.hkxj.platform.dao.StudentUserDao;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.constant.MiniProgram;
import cn.hkxj.platform.pojo.vo.CourseTimeTableVo;
import cn.hkxj.platform.service.CourseSubscribeService;
import cn.hkxj.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuki
 * @date 2018/11/6 20:36
 */
@Slf4j
@Service
public class CourseSubscriptionTask extends BaseSubscriptionTask {

    private static ExecutorService courseSubscriptionSendPool = new MDCThreadPool(5, 5,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), r -> new Thread(r, "courseSendThread"));


    @Value("${domain}")
    private String domain;
    @Resource
    private CourseSubscribeService courseSubscribeService;
    @Resource
    private TemplateBuilder templateBuilder;
    @Resource
    private WechatTemplateProperties wechatTemplateProperties;
    @Value("${scheduled.sendCourse}")
    private String updateSwitch;
    @Resource
    private StudentUserDao studentUserDao;

    @Scheduled(cron = "0 0 8 * * ?")
//这个cron表达式的意思是星期一到星期五的早上8点执行一次
    void sendCourseRemindMsgForFirstSection() {
        execute(CourseSubscribeService.FIRST_SECTION);
    }

    @Scheduled(cron = "0 50 9 * * ?")
//这个cron表达式的意思是星期一到星期五的早上9点50分执行一次
    void sendCourseRemindMsgForSecondSection() {
        execute(CourseSubscribeService.SECOND_SECTION);
    }

    @Scheduled(cron = "0 0 13 * * ?")
//这个cron表达式的意思是星期一到星期五的下午13点执行一次
    void sendCourseRemindMsgForThirdSection() {
        execute(CourseSubscribeService.THIRD_SECTION);
    }

    @Scheduled(cron = "0 50 14 * * ?")
//这个cron表达式的意思是星期一到星期五的下午14点50分执行一次
    void sendCourseRemindMsgForFourthSection() {
        execute(CourseSubscribeService.FOURTH_SECTION);
    }

    @Scheduled(cron = "0 0 18 * * ?")
//这个cron表达式的意思是星期一到星期五的晚上6点执行一次
    void sendCourseRemindMsgForFifthSection() {
        execute(CourseSubscribeService.FIFTH_SECTION);
    }

    public void execute(int section) {
        log.info("send course message task switch {}", updateSwitch);

        if (!BooleanUtils.toBoolean(updateSwitch)) {
            return;
        }

        List<WechatOpenid> openidList = courseSubscribeService.getSubscribeOpenid();
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();
        for (WechatOpenid openid : openidList) {
            courseSubscriptionSendPool.submit(() ->{
                processOpenid(openid, section, schoolTime);
            });

        }

    }

    private void sendMessage(WechatOpenid openid, List<WxMpTemplateData> templateData) {
        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
        miniProgram.setAppid(MiniProgram.APP_ID);
        miniProgram.setPagePath(MiniProgram.COURSE_PATH.getValue());
        String url = domain + "/platform/show/timetable";
        //构建一个课程推送的模板消息
        WxMpTemplateMessage templateMessage =
                templateBuilder.build(openid.getOpenid(), templateData, wechatTemplateProperties.getPlusCourseTemplateId(), miniProgram, url);
        sendTemplateMessage(templateMessage, openid, "course");
    }

    void processOpenid(WechatOpenid openid, int section, SchoolTime schoolTime){
        StudentUser studentUser = studentUserDao.selectStudentByAccount(openid.getAccount());
        if (studentUser == null){
            return;
        }
        CourseTimeTableVo table = courseSubscribeService.getTableBySection(studentUser, section, schoolTime);
        if (table != null) {
            List<WxMpTemplateData> templateData = templateBuilder.assemblyTemplateContentForCourse(studentUser, table, schoolTime);
            sendMessage(openid, templateData);
        }
    }


}
