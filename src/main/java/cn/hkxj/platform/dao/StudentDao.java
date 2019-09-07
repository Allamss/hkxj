package cn.hkxj.platform.dao;

import cn.hkxj.platform.mapper.StudentMapper;
import cn.hkxj.platform.pojo.Classes;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.example.StudentExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentDao {
    @Resource
    private StudentMapper studentMapper;

    public Student selectStudentByAccount(int account){
        return studentMapper.selectByAccount(account);
    }

    public void insertStudent(Student student){
        studentMapper.insert(student);
    }

    public void updatePassword(String account, String password){
        Student student = new Student();
        student.setAccount(Integer.parseInt(account));
        student.setPassword(password);


        StudentExample studentExample = new StudentExample();
        studentExample.createCriteria()
                .andAccountEqualTo(Integer.parseInt(account));
        studentMapper.updateByExampleSelective(student, studentExample);
    }

    public List<Student> selectStudentByClassId(int classId){
        StudentExample studentExample = new StudentExample();
        Classes classes = new Classes();

        classes.setId(classId);
        studentExample.createCriteria().andClassesEqualTo(classes);

        return studentMapper.selectByExample(studentExample);
    }
}
