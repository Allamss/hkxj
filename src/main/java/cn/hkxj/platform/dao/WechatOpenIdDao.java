package cn.hkxj.platform.dao;


import cn.hkxj.platform.mapper.ext.WechatOpenIdExtMapper;
import cn.hkxj.platform.pojo.ScheduleTask;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.WechatOpenidExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WechatOpenIdDao {
    @Autowired
    private WechatOpenIdExtMapper wechatOpenIdExtMapper;

    public List<WechatOpenid> selectByPojo(WechatOpenid wechatOpenid) {
        WechatOpenidExample example = new WechatOpenidExample();
        WechatOpenidExample.Criteria criteria = example.createCriteria();

        if (wechatOpenid.getOpenid() != null) {
            criteria.andOpenidEqualTo(wechatOpenid.getOpenid());
        }
        if (wechatOpenid.getAppid() != null) {
            criteria.andAppidEqualTo(wechatOpenid.getAppid());
        }

        return wechatOpenIdExtMapper.selectByExample(example);
    }

    public WechatOpenid selectByUniqueKey(String appid, String openid) {
        WechatOpenid wechatOpenid = new WechatOpenid().setOpenid(openid).setAppid(appid);
        return selectByPojo(wechatOpenid).stream().findFirst().orElse(null);
    }


    public void insertSelective(WechatOpenid wechatOpenid){
        wechatOpenIdExtMapper.insertSelective(wechatOpenid);
    }

    public void updateByPrimaryKeySelective(WechatOpenid wechatOpenid){
        wechatOpenIdExtMapper.updateByPrimaryKeySelective(wechatOpenid);
    }

    public List<WechatOpenid> getOpenid(String openid) {
        WechatOpenidExample wechatOpenidExample = new WechatOpenidExample();
        wechatOpenidExample.createCriteria().andOpenidEqualTo(openid);
        return wechatOpenIdExtMapper.selectByExample(wechatOpenidExample);
    }


    public void openIdUnbind(String openid, String appid) {
        WechatOpenidExample example = new WechatOpenidExample();
        example.createCriteria()
                .andAppidEqualTo(appid)
                .andOpenidEqualTo(openid);

        wechatOpenIdExtMapper.updateByExampleSelective(new WechatOpenid().setIsBind(false), example);
    }


    public void openIdUnbindAllPlatform(int account) {
        WechatOpenidExample wechatOpenidExample = new WechatOpenidExample();
        wechatOpenidExample.createCriteria().andAccountEqualTo(account);
        wechatOpenIdExtMapper.updateByExampleSelective(new WechatOpenid().setIsBind(false), wechatOpenidExample);
    }


    public List<String> getAllOpenidsFromOneClass(int classId, String openid, String appid) {
        return wechatOpenIdExtMapper.getAllOpenidsFromOneClass(classId, openid);
    }

    public List<WechatOpenid> selectBySubscribe(ScheduleTask scheduleTask){
        return wechatOpenIdExtMapper.selectBySubscribe(scheduleTask);
    }


}
