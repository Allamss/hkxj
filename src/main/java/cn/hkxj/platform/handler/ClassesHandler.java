package cn.hkxj.platform.handler;

import cn.hkxj.platform.mapper.ClassesMapper;
import cn.hkxj.platform.pojo.Classes;
import cn.hkxj.platform.utils.ApplicationUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Yuki
 * @date 2018/12/4 22:14
 */
public class ClassesHandler extends BaseTypeHandler<Classes> {

    private ClassesMapper classesMapper;

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Classes classes, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, classes.getId());
    }

    @Override
    public Classes getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int id = resultSet.getInt(s);
        if(resultSet.wasNull()){
            return null;
        } else {
            checkMapper();
            return classesMapper.selectByPrimaryKey(id);
        }
    }

    @Override
    public Classes getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值
            checkMapper();
            return classesMapper.selectByPrimaryKey(id);
        }
    }

    @Override
    public Classes getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int id = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值
            checkMapper();
            return classesMapper.selectByPrimaryKey(id);
        }
    }

    private void checkMapper(){
        if(Objects.isNull(classesMapper)){
            classesMapper = ApplicationUtil.getBean("classesMapper");
        }
    }
}
