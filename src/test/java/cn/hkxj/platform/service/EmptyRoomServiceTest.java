package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.constant.Building;
import cn.hkxj.platform.pojo.timetable.RoomTimeTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author junrong.chen
 * @date 2019/6/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@WebAppConfiguration
public class EmptyRoomServiceTest {
    @Resource
    private EmptyRoomService emptyRoomService;

    @Test
    public void getRoomTimeTableByTime() throws IOException {
        List<RoomTimeTable> roomTimeTableByTime = emptyRoomService.getRoomTimeTableByTime(12, 3, 3, Building.SCIENCE, 3);

    }
}