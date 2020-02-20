package cn.hkxj.platform.service;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.mapper.OpenidMapper;
import cn.hkxj.platform.mapper.OpenidPlusMapper;
import cn.hkxj.platform.mapper.WechatOpenidMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Yuki
 * @date 2019/3/19 22:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatformApplication.class)
@WebAppConfiguration
public class ExcelServiceTest {

   /* @Resource
    private ExcelService excelService;*/
    @Resource
    private OpenidPlusMapper openidPlusMapper;
    @Resource
    private WechatOpenidMapper wechatOpenidMapper;


    /*@Test
    public void isDistinct(){
        System.out.println(excelService.lastIndexOfNum("14-16单周上"));;
    }*/

    @Test
    public void readExcel() {
//        System.out.println(excelService.parseBuilding("科401（高层）"));
//        System.out.println(excelService.parseBuilding("科N302"));
//        System.out.println(excelService.parseBuilding("图书馆S4004"));
//        System.out.println(excelService.parseBuilding("N10播放室"));
//        List<ExcelResult> excelResults = excelService.readExcel();
//        excelService.insertDb(excelResults);
        System.out.println(openidPlusMapper.getAllOpenidsFromOneClass(316, "oWPfu0noOKkUHpTkmP8ExbUgfqUY"));
        System.out.println(wechatOpenidMapper.getAllOpenidsFromOneClass(316, "oWPfu0noOKkUHpTkmP8ExbUgfqUY"));//后添加的测试代码
    }

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        Set<Integer> set1 = new HashSet<>();
        set1.add(2);
        set1.add(3);
        set.addAll(set1);
        Set<Integer> set2 = new HashSet<>();
        set2.add(4);
        set2.add(5);
        set.addAll(set2);
        System.out.println(set);
    }
}