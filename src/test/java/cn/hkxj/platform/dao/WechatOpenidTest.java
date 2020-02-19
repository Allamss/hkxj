package cn.hkxj.platform.dao;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.mapper.MiniProgramOpenidMapper;
import cn.hkxj.platform.mapper.OpenidMapper;
import cn.hkxj.platform.mapper.OpenidPlusMapper;
import cn.hkxj.platform.mapper.WechatOpenidMapper;
import cn.hkxj.platform.pojo.MiniProgramOpenid;
import cn.hkxj.platform.pojo.MiniProgramOpenidExample;
import cn.hkxj.platform.pojo.WechatOpenid;
import cn.hkxj.platform.pojo.WechatOpenidExample;
import cn.hkxj.platform.pojo.example.OpenidExample;
import cn.hkxj.platform.pojo.wechat.Openid;
import cn.hkxj.platform.service.NewUrpSpiderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
public class WechatOpenidTest {
    @Autowired
    private OpenidMapper openidMapper;

    @Autowired
    private WechatOpenidMapper wechatOpenidMapper;

    @Autowired
    private OpenidPlusMapper openidPlusMapper;

    @Autowired
    private MiniProgramOpenidMapper miniProgramOpenidMapper;
    
    //测试从oppenid表中拿出数据插入到WeChatopenid表
    //openid表中第5864条数据和上面的某一条数据openid重复
    //已插入的数据学号2018025144   未插入的数据学号2018025838
    //运行结束插入5637条数据，源数据5638条，但是有一条APPID重复为插入成功
    //此次运行的最后一条数据id为5641
    @Test
    public void insertWechat_openidFromopenid(){
        WechatOpenid wechatOpenid=new WechatOpenid();
        List<Openid> openids = openidMapper.selectByExample(new OpenidExample());
        System.out.println("openid总数据："+openids.size());
       for (int i=5526;i<=openids.size();i++){
           wechatOpenid.setAccount(openids.get(i).getAccount());
           wechatOpenid.setGmtCreate(openids.get(i).getGmtCreate());
           wechatOpenid.setGmtModified(openids.get(i).getGmtModified());
           wechatOpenid.setIsBind(openids.get(i).getIsBind());
           wechatOpenid.setOpenid(openids.get(i).getOpenid());
           wechatOpenid.setAppid("wx2212ea680ca5c05d");
           wechatOpenidMapper.insert(wechatOpenid);
           //wechatOpenidMapper.insertSelective(wechatOpenid);
       }
        System.out.println("数据插入完成");

    }

    //1987条数据
    //又遇到一个APPID重复的值：oDUpSs8B8-vr4TPruTy2XPSyPg3U/////oDUpSs8B8-vr4TPruTy2XPSyPg3U
    //wechat_openid:oDUpSs8B8-vr4TPruTy2XPSyPg3U的数据id=1083
    //Openid_plus里面的数据学号是：2014025838
    //两张表插入的最后一条数据id=7630
    @Test//测试从Openid_plus表中拿出数据插入到WeChatopenid表
    public void insertWechat_openidFromOpenid_plus(){
        WechatOpenid wechatOpenid=new WechatOpenid();
        List<Openid> openids = openidPlusMapper.selectByExample(new OpenidExample());
        System.out.println(openids.size());
        for (int i=5;i<=openids.size();i++){
            if(openids.get(i).getAccount()==null){
                continue;
            }
            wechatOpenid.setAccount(openids.get(i).getAccount());
            wechatOpenid.setGmtCreate(openids.get(i).getGmtCreate());
            wechatOpenid.setGmtModified(openids.get(i).getGmtModified());
            wechatOpenid.setIsBind(openids.get(i).getIsBind());
            wechatOpenid.setOpenid(openids.get(i).getOpenid());
            wechatOpenid.setAppid("wx541fd36e6b400648");
            wechatOpenidMapper.insert(wechatOpenid);
           // wechatOpenidMapper.insertSelective(wechatOpenid);
        }
        System.out.println("数据插入完成");

    }

    //MiniProgramOpenid表杨新华同学负责导入数据，所以线面的代码没有运行测试过
    @Test//测试从MiniProgramOpenid表中拿出数据插入到WeChatopenid表
    public void insertWechat_openidFromMiniProgramOpenid(){
        WechatOpenid wechatOpenid=new WechatOpenid();
        List<MiniProgramOpenid> miniProgramOpenids = miniProgramOpenidMapper.selectByExample(new MiniProgramOpenidExample());
        System.out.println(miniProgramOpenids.size());
        for (int i=127;i<=miniProgramOpenids.size();i++){
            if(miniProgramOpenids.get(i).getAccount()==null){
                continue;
            }
            wechatOpenid.setAccount(miniProgramOpenids.get(i).getAccount());
            wechatOpenid.setGmtCreate(miniProgramOpenids.get(i).getGmtCreate());
            wechatOpenid.setGmtModified(miniProgramOpenids.get(i).getGmtModified());
            wechatOpenid.setIsBind(miniProgramOpenids.get(i).getIsBind());
            wechatOpenid.setOpenid(miniProgramOpenids.get(i).getOpenid());
            wechatOpenid.setAppid("wx05f7264e83fa40e9");
            wechatOpenidMapper.insert(wechatOpenid);
        }
        System.out.println("数据插入完成");


    }

    @Test
    public void getobjectwithtime(){
       /* Openid openid = openidMapper.selectByPrimaryKey(208);
        System.out.println(openid.getGmtCreate());
        Openid openid1 = openidPlusMapper.selectByPrimaryKey(1);
        System.out.println(openid1.getGmtCreate());*/
        List<Openid> openids = openidMapper.selectByExample(new OpenidExample());
        System.out.println("openid总数据："+openids.size());
        System.out.println(openids.get(0).getGmtCreate());


    }



    @Test//测试一下continue的使用
    public void test1() {
        for( int i=0;i<10;i++){
            if(i==5){
                continue;
            }
            System.out.println(i);
        }

    }

}
