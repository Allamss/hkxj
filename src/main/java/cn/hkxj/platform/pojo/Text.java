package cn.hkxj.platform.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author Allams
 * 微博实体类
 */
@Data
@Accessors(chain = true)
public class Text {
    private Integer id;
    private Integer studentId;
    @Length(min = 1, max = 255, message = "正文内容长度在1到255之间")
    private String text;
    private byte isAnonymous;
    private Integer thumbsUp;
    private Date gmtCreate;
    private Date gmtModified;
}
