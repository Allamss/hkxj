package cn.hkxj.platform.task;

import cn.hkxj.platform.dao.WechatOpenIdDao;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author JR Chan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CourseSubscriptionTaskTest {

    @Autowired
    private CourseSubscriptionTask courseSubscriptionTask;
    @Autowired
    private WechatOpenIdDao wechatOpenIdDao;
    @Test
    public void processOpenid() {

        List<WechatOpenid> list = wechatOpenIdDao.selectByPojo(new WechatOpenid().setOpenid("oCxRO1G9N755dOY5dwcT5l3IlS3Y"));
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();

        schoolTime.setDay(3);
        schoolTime.setSchoolWeek(8);
        WechatOpenid openid = list.get(0);
        openid.setAccount(2019026628);
        courseSubscriptionTask.processOpenid(openid, 3, schoolTime);
    }
}