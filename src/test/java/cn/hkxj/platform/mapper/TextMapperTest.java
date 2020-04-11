package cn.hkxj.platform.mapper;

import cn.hkxj.platform.PlatformApplication;
import cn.hkxj.platform.pojo.Text;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Allams
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TextMapperTest {
    @Autowired
    TextMapper textMapper;
    @Test
    public void testInsertText() {
        Text text = new Text();
        text.setText("这是一个测试");
        text.setStudentId(1);
        text.setIsAnonymous((byte) 0);
        textMapper.insertText(text);
    }
}
