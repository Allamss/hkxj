package cn.hkxj.platform.service;

import cn.hkxj.platform.config.wechat.MiniProgramProperties;
import cn.hkxj.platform.config.wechat.WechatMpPlusProperties;
import cn.hkxj.platform.config.wechat.WechatOpenidProperties;
import cn.hkxj.platform.dao.MiniProgramOpenIdDao;
import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.dao.WechatOpenIdDao;
import cn.hkxj.platform.mapper.OpenidMapper;
import cn.hkxj.platform.mapper.OpenidPlusMapper;
import cn.hkxj.platform.mapper.StudentMapper;
import cn.hkxj.platform.mapper.WechatOpenidMapper;
import cn.hkxj.platform.pojo.MiniProgramOpenid;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.WechatOpenidExample;
import cn.hkxj.platform.pojo.wechat.Openid;
import cn.hkxj.platform.pojo.example.OpenidExample;
import cn.hkxj.platform.pojo.Student;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author junrong.chen
 * @date 2018/10/22
 */
@Service("openIdService")
public class OpenIdService {
    @Resource
    private OpenidMapper openidMapper;//原先对应的表示openid表，现在已经将数据挪到wechatopenid表里面
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private OpenidPlusMapper openidPlusMapper;//原先对应的表示openid_plus表，现在已经将数据挪到wechatopenid表里面
    @Resource
    private WechatMpPlusProperties wechatMpPlusProperties;
    @Resource
    private MiniProgramProperties miniProgramProperties;
    @Resource
    private MiniProgramOpenIdDao miniProgramOpenIdDao;//原先对应的表示miniProgramOpenId表，现在已经将数据挪到wechatopenid表里面
    @Resource
    private StudentDao studentDao;
    @Resource
    private WechatOpenidMapper wechatOpenidMapper;
    @Resource
    private WechatOpenidProperties wechatOpenidProperties;
    @Resource
    private WechatOpenIdDao wechatOpenIdDao;

    public boolean openidIsExist(String openid, String appid) {
        return getOpenid(openid, appid).size() == 1;
    }

    /**
     * 已修改并在OpenIdServiceTest里面进行了单元测试
     */
    public boolean openidIsBind(String openid, String appid) {
       /*
       if (isPlus(appid)) {
            return openidPlusMapper.isOpenidBind(openid) == 1;
        }
        return openidMapper.isOpenidBind(openid) == 1;
        */
// =============================================================上面是原先的代码，下面的改过的代码

       return wechatOpenIdDao.openidIsBind(openid);

    }


    //wechatOpenidExample
    //源码的泛型是：、Openid
    /**
     * 已修改并在OpenIdServiceTest里面进行了单元测试
     */
    public List<WechatOpenid> getOpenid(String openid, String appid) {
       /*
        OpenidExample openidExample = new OpenidExample();
        openidExample.createCriteria().andOpenidEqualTo(openid);
        if (isPlus(appid)) {
            return openidPlusMapper.selectByExample(openidExample);
        }
        return openidMapper.selectByExample(openidExample);
     */
//=======================================下面是改过的代码替代上面的源码，若要恢复记得把方法返回的集合泛型换成Openid

       return wechatOpenIdDao.getOpenid(openid);

    }

    /**
     * 已修改并在OpenIdServiceTest里面进行了单元测试
     */
    public Student getStudentByOpenId(String openid, String appid) {
       /*
       if (miniProgramProperties.getAppId().equals(appid)) {
            List<MiniProgramOpenid> openidList = miniProgramOpenIdDao.selectByPojo(new MiniProgramOpenid().setOpenid(openid));//源码恢复记得解开
            if (openidList.size() == 1) {
                return studentDao.selectStudentByAccount(openidList.get(0).getAccount());
            }
            return null;
        }
        List<Openid> openidList = getOpenid(openid, appid);//源码恢复记得解开
             if (openidList.size() == 0) {
             throw new IllegalArgumentException("user not bind openid: " + openid + " appid: " + appid);

        }
             Integer account = openidList.get(0).getAccount();
             return studentMapper.selectByAccount(account);

             */
  //====================================================上面是源码，下面是修改过的代码
        return  wechatOpenIdDao.getStudentByOpenId(openid,appid);

    }

    /**
     * 已修改并在OpenIdServiceTest里面进行了单元测试
     */
    public void openIdUnbind(String openid, String appid) {
       /*
       if (isPlus(appid)) {
            openidPlusMapper.openidUnbind(openid);
        } else {
            openidMapper.openidUnbind(openid);
        }
        */
 //==========================================================上面是源代码，下面是改过的代码
        wechatOpenIdDao.openIdUnbind(openid,appid);

    }


    /**
     * 对于密码错误的账号全平台解绑
     * @param account
     */
    public void openIdUnbindAllPlatform(int account) {
       /*
        OpenidExample openidExample = new OpenidExample();

        openidExample.createCriteria().andAccountEqualTo(account);

        openidPlusMapper.updateByExampleSelective(new Openid().setIsBind(false), openidExample);

        openidMapper.updateByExampleSelective(new Openid().setIsBind(false), openidExample);
        */

 //============================================================上面是源代码，下面是改过的代码
        wechatOpenIdDao.openIdUnbindAllPlatform(account);

    }



    /**
     * 已修改并在OpenIdServiceTest里面进行了单元测试
     */
    public List<String> getAllOpenidsFromOneClass(int classId, String openid, String appid) {
       /*
       if (isPlus(appid)) {
            return openidPlusMapper.getAllOpenidsFromOneClass(classId, openid);
        } else {
            return openidMapper.getAllOpenidsFromOneClass(classId, openid);
        }
        */
  //===============================================上面是源代码，下面是改过的代码
        return wechatOpenIdDao.getAllOpenidsFromOneClass(classId,openid,appid);
    }



    private boolean isPlus(String appid) {
        return Objects.equals(wechatMpPlusProperties.getAppId(), appid);
    }
}
