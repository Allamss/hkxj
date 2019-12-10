package cn.hkxj.platform.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Grade {
    @EqualsAndHashCode.Exclude
    private Integer id;

    @EqualsAndHashCode.Exclude
    private Integer examId = 0;

    private Integer account;

    private Double score;

    private Double credit;

    private Double gradePoint;

    private String levelName;

    private String levelPoint;

    @EqualsAndHashCode.Exclude
    private Integer rank;

    private String courseName;

    private String courseNumber;

    private String courseOrder;

    private String coursePropertyCode;

    private String coursePropertyName;

    private String examTypeCode;

    private String examTypeName;

    private Integer studyHour;

    private String operateTime;

    private String operator;

    private String examTime;

    private String unpassedReasonCode;

    private String unpassedReasonExplain;

    private String remark;

    private String replaceCourseNumber;

    private String retakeCourseMark;

    private String retakecourseModeCode;

    private String retakeCourseModeExplain;

    private String standardPoint;

    private String termYear;

    private Integer termOrder;

    @EqualsAndHashCode.Exclude
    private Date gmtCreate;
    @EqualsAndHashCode.Exclude
    private Date gmtModify;

    private boolean update = false;
}