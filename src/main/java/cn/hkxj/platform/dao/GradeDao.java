package cn.hkxj.platform.dao;

import cn.hkxj.platform.mapper.GradeMapper;
import cn.hkxj.platform.pojo.Grade;
import cn.hkxj.platform.pojo.GradeExample;
import cn.hkxj.platform.pojo.SchoolTime;
import cn.hkxj.platform.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GradeDao {
    @Resource
    private GradeMapper gradeMapper;

    public int insertSelective(Grade grade) {
        return gradeMapper.insertSelective(grade);
    }

    public List<Grade> selectByPojo(Grade grade) {
        GradeExample gradeExample = new GradeExample();
        GradeExample.Criteria criteria = gradeExample.createCriteria();
        if(grade.getAccount() != null){
            criteria.andAccountEqualTo(grade.getAccount());
        }
        if(grade.getTermYear() != null){
            criteria.andTermYearEqualTo(grade.getTermYear());
        }
        if(grade.getTermOrder() != null){
            criteria.andTermOrderEqualTo(grade.getTermOrder());
        }

        return gradeMapper.selectByExample(gradeExample);
    }


    public List<Grade> getCurrentTermGradeByAccount(int account){
        SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();
        return selectByPojo(new Grade()
                .setTermYear(schoolTime.getTerm().getTermYear())
                .setTermOrder(schoolTime.getTerm().getOrder())
                .setAccount(account));
    }

    public void updateByPrimaryKeySelective(Grade grade){
        gradeMapper.updateByPrimaryKeySelective(grade);

    }
}
