package cn.hkxj.platform.dao;

import cn.hkxj.platform.mapper.StudentUserMapper;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.pojo.StudentUserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentUserDao {
    @Resource
    private StudentUserMapper studentUserMapper;

    public StudentUser selectStudentByAccount(int account){
        StudentUserExample studentUserExample = new StudentUserExample();
        studentUserExample.createCriteria().andAccountEqualTo(account);
        return studentUserMapper.selectByExample(studentUserExample).stream().findFirst().orElse(null);
    }

    public List<StudentUser> selectAllStudent(){
        StudentUserExample studentUserExample = new StudentUserExample();
        return studentUserMapper.selectByExample(studentUserExample);
    }

    public void insertStudentSelective(StudentUser studentUser){
        studentUserMapper.insertSelective(studentUser);
    }

    public boolean isStudentAccountExists(int account){
        StudentUserExample studentUserExample = new StudentUserExample();
        studentUserExample.createCriteria().andAccountEqualTo(account);
        List<StudentUser> studentUsers = studentUserMapper.selectByExample(studentUserExample);
        return studentUsers.size() > 0;
    }

}
