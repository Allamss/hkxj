package cn.hkxj.platform.dao;


import cn.hkxj.platform.config.wechat.WechatOpenidProperties;
import cn.hkxj.platform.mapper.MiniProgramOpenidMapper;
import cn.hkxj.platform.mapper.StudentMapper;
import cn.hkxj.platform.mapper.WechatOpenidMapper;
import cn.hkxj.platform.pojo.*;

import cn.hkxj.platform.mapper.MiniProgramOpenidMapper;
import cn.hkxj.platform.mapper.WechatOpenidMapper;
import cn.hkxj.platform.pojo.MiniProgramOpenid;
import cn.hkxj.platform.pojo.MiniProgramOpenidExample;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.WechatOpenidExample;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class WechatOpenIdDao {



    @Resource
    private WechatOpenidMapper wechatOpenidMapper;

    @Resource
    private WechatOpenidProperties wechatOpenidProperties;
    @Resource
    private StudentDao studentDao;
    @Resource
    private StudentMapper studentMapper;


    public List<WechatOpenid> selectByPojo( WechatOpenid wechatOpenid){
        WechatOpenidExample example=new WechatOpenidExample();
        WechatOpenidExample.Criteria criteria = example.createCriteria();

        if(wechatOpenid.getOpenid() != null){
            criteria.andOpenidEqualTo(wechatOpenid.getOpenid());
        }

        return wechatOpenidMapper.selectByExample(example);
    }


    public boolean openidIsBind(String openid) {
        List<WechatOpenid> wechatOpenids = wechatOpenidMapper.selectByExample(new WechatOpenidExample());
        for(WechatOpenid wechatOpenid:wechatOpenids){
            if(wechatOpenid.getOpenid().equals(openid)){
                return wechatOpenid.getIsBind();
            }
        }
        return false;
    }


    public List<WechatOpenid> getOpenid(String openid) {
        WechatOpenidExample wechatOpenidExample=new WechatOpenidExample();
        wechatOpenidExample.createCriteria().andOpenidEqualTo(openid);
        return wechatOpenidMapper.selectByExample(wechatOpenidExample);
    }


    public Student getStudentByOpenId(String openid, String appid) {
        if( wechatOpenidProperties.getAppId().equals(appid))  {
            List<WechatOpenid> openidList = selectByPojo(new WechatOpenid().setOpenid(openid));
            if (openidList.size() == 1) {
                return studentDao.selectStudentByAccount(openidList.get(0).getAccount());
            }
            return null;
        }
          List<WechatOpenid> openidList = getOpenid(openid);
        if (openidList.size() == 0) {
            throw new IllegalArgumentException("user not bind openid: " + openid + " appid: " + appid);
        }
        Integer account = openidList.get(0).getAccount();
        return studentMapper.selectByAccount(account);

    }


    public void openIdUnbind(String openid, String appid) {
        List<WechatOpenid> wechatOpenids = wechatOpenidMapper.selectByExample(new WechatOpenidExample());
        for (WechatOpenid wechatOpenid:wechatOpenids){
            if(wechatOpenid.getOpenid().equals(openid)){
                wechatOpenid.setIsBind(false);
                wechatOpenidMapper.updateByPrimaryKey(wechatOpenid);
            }
        }
    }


    public void openIdUnbindAllPlatform(int account) {
        WechatOpenidExample wechatOpenidExample=new WechatOpenidExample();
        wechatOpenidExample.createCriteria().andAccountEqualTo(account);
        wechatOpenidMapper.updateByExampleSelective(new WechatOpenid().setIsBind(false), wechatOpenidExample);
    }


    public List<String> getAllOpenidsFromOneClass(int classId, String openid, String appid) {
        return wechatOpenidMapper.getAllOpenidsFromOneClass(classId, openid);
    }






}
