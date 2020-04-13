package cn.hkxj.platform.pojo.dto;

import cn.hkxj.platform.pojo.Text;
import cn.hkxj.platform.pojo.TextReply;
import lombok.Data;

import java.util.List;

/**
 * @author Allams
 */
@Data
public class TextReplyDto {
    private Text text;
    private List<TextReply> replyList;
}
