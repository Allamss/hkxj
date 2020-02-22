package cn.hkxj.platform.mapper.ext;

import cn.hkxj.platform.mapper.ClassCourseTimetableMapper;
import cn.hkxj.platform.pojo.ClassCourseTimetable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JR Chan
 */
@Mapper
@Repository
public interface ClassCourseTimeTableExtMapper extends ClassCourseTimetableMapper {

    void insertBatch(List<ClassCourseTimetable> list);
}
