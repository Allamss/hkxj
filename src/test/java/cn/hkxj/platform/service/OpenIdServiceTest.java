package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.StudentUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

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
        boolean a = openIdService.openidIsBind("oCxRO1G9N755dOY5dwcT5l3IlS3Y", "wx541fd36e6b400648");
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
        StudentUser studentByOpenId = openIdService.getStudentByOpenId("o6393wvmheXId6z3pO9hPsZrI2VQ", "wx2212ea680ca5c05d");
        System.out.println(studentByOpenId);

    }

}