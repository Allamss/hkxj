package cn.hkxj.platform.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class WechatOpenid {
    private Integer id;

    private String openid;

    private Integer account;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean isBind;

    private String appid;


}