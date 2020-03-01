package cn.hkxj.platform.dao;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.ScheduleTask;
import cn.hkxj.platform.pojo.WechatOpenid;
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
@SpringBootTest(classes = PlatformApplication.class)
@Slf4j
public class WechatOpenIdDaoTest {
    @Autowired
    private WechatOpenIdDao wechatOpenIdDao;

    @Test
    public void selectBySubscribe() {
        ScheduleTask task = new ScheduleTask();
        task.setAppid("wx541fd36e6b400648").setIsSubscribe((byte)1);
        List<WechatOpenid> openidList = wechatOpenIdDao.selectBySubscribe(task);

        System.out.println(openidList.size());
    }
}