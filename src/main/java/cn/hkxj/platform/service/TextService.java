package cn.hkxj.platform.service;

import cn.hkxj.platform.exceptions.TextNotFoundException;
import cn.hkxj.platform.mapper.TextMapper;
import cn.hkxj.platform.pojo.Text;
import cn.hkxj.platform.pojo.dto.TextReplyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Allams
 * 微博服务层
 */
@Slf4j
@Service
public class TextService {

    @Resource
    TextMapper textMapper;

    //@Resource
    //TextReplyMapper textReplyMapper;

    public int insertText(Text text) {
        return textMapper.insertText(text);
    }

    public void deleteById(Integer id) throws TextNotFoundException{
        //textReplyMapper.deleteByTextId(id);
        if(textMapper.deleteById(id) <= 0) {
            log.info("妹找到这个id为"+id+"的微博，删除失败");
            throw new TextNotFoundException();
        }
    }

    public TextReplyDto selectWithReplyById(Integer id) {
        TextReplyDto dto = new TextReplyDto();
        dto.setText(textMapper.selectById(id));
        //dto.setReplyList();
        return dto;
    }

    public Text selectById(Integer id) throws TextNotFoundException{
        Text text = textMapper.selectById(id);
        if(text == null) {
            log.info("妹找到这个id为"+id+"的微博，查询失败");
            throw new TextNotFoundException();
        }
        return text;
    }

    public void updateText(Text text) {
        if(textMapper.updateText(text) <= 0) {
            log.info("妹找到这个id为"+text.getId()+"的微博，更新失败");
            throw new TextNotFoundException();
        }
    }
}
