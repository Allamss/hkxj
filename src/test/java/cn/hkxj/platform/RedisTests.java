package cn.hkxj.platform;

import cn.hkxj.platform.pojo.constant.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * @author JR Chan
 * @date 2018/12/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTests {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void redisAdd() {
//        Set<String> accountSet = Optional
//                .ofNullable(redisTemplate.keys(RedisKeys.URP_LOGIN_COOKIE.getName()+"*")).orElse(Collections.emptySet())
//                .stream()
//                .map(key -> key.split(RedisKeys.URP_LOGIN_COOKIE.getName() + "_")[1])
//                .collect(Collectors.toSet());
        Set<String> keys = redisTemplate.keys(RedisKeys.URP_LOGIN_COOKIE.getName() + "*");
        System.out.println(keys.size());
        for (String s : keys) {
            System.out.println(s);
        }


    }


}
