package cn.hkxj.platform.controller;

import cn.hkxj.platform.exceptions.TextNotFoundException;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.Text;
import cn.hkxj.platform.pojo.WebResponse;
import cn.hkxj.platform.service.TextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Allams
 */
@Controller
@Slf4j
@RequestMapping("/text")
public class TextController {

    @Resource
    TextService textService;
    //@Resource
    //TextReplyService textReplyService;

    @PostMapping(value = "/send")
    public WebResponse insertText(@Valid Text text, BindingResult bindingResult, HttpSession session) {
        //获取错误信息
        if(bindingResult.hasErrors()){
            List<ObjectError> verificationErrors = bindingResult.getAllErrors();
            //就只有一个校验信息，所以校验错误有的话直接把第一个传出去就行了
            return WebResponse.fail(400, verificationErrors.get(0).getDefaultMessage());
        }
        Student student = (Student)session.getAttribute("student");
        text.setStudentId(student.getId());
        textService.insertText(text);
        return WebResponse.successWithMessage("发博成功^ ^_");
    }

    @GetMapping(value = "/delete/{id}")
    public WebResponse deleteText(@PathVariable("id") Integer id, HttpSession session) {
        try {
            if(session.getAttribute("student") != textService.selectById(id)) {
                return WebResponse.fail(400, "你都不是发博人，可憋删了");
            }
            textService.deleteById(id);
        }catch (TextNotFoundException exception) {
            return WebResponse.fail(500, exception.getMessage());
        }
        return WebResponse.successWithMessage("微博删除成功");
    }

    @PostMapping(value = "/update/{id}")
    public WebResponse updateText(@PathVariable("id") Integer id, @Valid Text text, BindingResult bindingResult, HttpSession session) {
        try {
            if(session.getAttribute("student") != textService.selectById(id).getStudentId()) {
                return WebResponse.fail(400, "你都不是发博人，可憋删了");
            }
            textService.updateText(text);
        }catch (TextNotFoundException exception) {
            return WebResponse.fail(500, exception.getMessage());
        }
        return WebResponse.successWithMessage("更新成功");
    }

    @GetMapping(value = "/get/{id}")
    public WebResponse getTextById(@PathVariable("id") Integer id) {
        return WebResponse.success(textService.selectById(id));
    }



}
