package cn.hkxj.platform.mapper.ext;

import cn.hkxj.platform.mapper.WechatOpenidMapper;
import cn.hkxj.platform.pojo.ScheduleTask;
import cn.hkxj.platform.pojo.WechatOpenid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JR Chan
 */
@Mapper
@Repository
public interface WechatOpenIdExtMapper extends WechatOpenidMapper {

    void insertBatch(List<WechatOpenid> wechatOpenidList);

    List<WechatOpenid> selectBySubscribe(ScheduleTask task);
}
