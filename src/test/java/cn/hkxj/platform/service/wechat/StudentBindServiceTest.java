package cn.hkxj.platform.service.wechat;

import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.pojo.vo.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentBindServiceTest {
    @Autowired
    StudentBindService studentBindService;
    @Autowired
    StudentDao studentDao;

    @Test
    public void studentLogin() {
        StudentVo studentVo = studentBindService.studentLogin("2016023344", "1");
        System.out.println(studentVo);
        assert studentVo != null;
    }

    @Test
    public void studentBind() {
        Student student = studentDao.selectStudentByAccount(2016023344);
        studentBindService.studentBind("2016023344", "o6393wvmheXId6z3pO9hPsZrI2VQ", "test");
        assert student != null;
    }

    @Test
    public void getStudentUserInfo() {
        Student student = studentDao.selectStudentByAccount(2015023299);
        StudentUser studentUserInfo = studentBindService.getStudentUserInfo(student.getAccount().toString(), student.getPassword());

        System.out.println(studentUserInfo);

    }

}