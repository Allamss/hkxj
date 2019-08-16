package cn.hkxj.platform.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UrpGrade {
    private Integer id;

    private Integer examId;

    private Integer account;

    private Integer score;

    private Double gradePoint;

    private String levelName;

    private String levelPoint;

    private Integer rank;

    private String unpassedReasonCode;

    private String unpassedReasonExplain;

    private String remark;

    private String replaceCourseNumber;

    private String retakeCourseMark;

    private String retakecourseModeCode;

    private String retakeCourseModeExplain;

    private String standardPoint;

    private Date gmtCreate;

}