package cn.hkxj.platform.controller;

import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.dao.StudentUserDao;
import cn.hkxj.platform.service.wechat.StudentBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class TestController {

    @Resource
    private StudentDao studentDao;
    @Resource
    private StudentUserDao studentUserDao;
    @Resource
    private StudentBindService studentBindService;

    @RequestMapping("tests")
    public String test(String echostr){
        return echostr;
    }

}
