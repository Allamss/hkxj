package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.wechat.Openid;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@WebAppConfiguration
public class OpenIdServiceTest {
    @Resource
    private OpenIdService openIdService;
    @Resource
    private StudentDao studentDao;

    @Test
    public void unBind(){
        openIdService.openIdUnbind("o6393wqXpaxROMjiy8RAgPLqWFF8", "wx2212ea680ca5c05d");
    }


    @Test
    public void openIdUnbindAllPlatform(){
        for (Student student : studentDao.selectAllStudent()) {
            if(!student.getIsCorrect()){
                System.out.println(student.getAccount());
                try {
                    openIdService.openIdUnbindAllPlatform(student.getAccount());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void openidIsBind(){
        boolean a = openIdService.openidIsBind("o6393ws2oaJFXBZwViWSTJd6ABkU", "wx2212ea680ca5c05d");
        System.out.println(a);

    }

    @Test
    public void getOpenid(){
        openIdService.getOpenid("o6393ws2oaJFXBZwViWSTJd6ABkU", "wx2212ea680ca5c05d");
    }

    @Test
    public void openIdUnbind(){
        openIdService.openIdUnbind("o6393wvmheXId6z3pO9hPsZrI2VQ", "wx2212ea680ca5c05d");
    }

    @Test
    public void getStudentByOpenId(){
        Student studentByOpenId = openIdService.getStudentByOpenId("o6393wvmheXId6z3pO9hPsZrI2VQ", "wx2212ea680ca5c05d");
        System.out.println(studentByOpenId);

    }

}