package cn.hkxj.platform.controller;

import cn.hkxj.platform.pojo.GradeSearchResult;
import cn.hkxj.platform.pojo.WebResponse;
import cn.hkxj.platform.pojo.vo.GradeVo;
import cn.hkxj.platform.service.NewGradeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author junrong.chen
 */
@Slf4j
@RestController
public class GradeController {
    @Resource
    private NewGradeSearchService newGradeSearchService;

    @RequestMapping(value = "/nowGrade", method = RequestMethod.POST)
    public WebResponse getNowGrade(@RequestParam("account") String account, @RequestParam("password") String password){
        GradeSearchResult currentGrade = newGradeSearchService.getCurrentGrade(account, password);
        return WebResponse.success(currentGrade.getData());
    }

    @RequestMapping(value = "/nowGradeV2", method = RequestMethod.POST)
    public WebResponse getNowGradeV2(@RequestParam("account") String account,
                                     @RequestParam("password") String password){
        List<GradeVo> result = newGradeSearchService.getCurrentTermGrade(account, password);
        return WebResponse.success(result);
    }
}
