package cn.hkxj.platform.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Allams
 */
@Data
@Accessors(chain = true)
public class TextReply {
    private Integer id;
    private Integer textId;
    private Integer studentId;
    private String replyText;
    private boolean isAnonymous;
    private Integer thumbsUp;
    private Date gmtCreate;
    private Date gmtModified;
}
