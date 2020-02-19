package cn.hkxj.platform.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Yuki
 * @date 2019/9/1 14:48
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolTime implements Comparable {

    private int day;

    private int week;

    private Term term;
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

