package cn.hkxj.platform.mapper;

import cn.hkxj.platform.pojo.Text;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Allams
 * 微博mapper接口
 */
@Mapper
@Repository
public interface TextMapper {
    Text selectById(Integer id);
    int insertText(Text text);
    int deleteById(Integer id);
    int updateText(Text text);
}
