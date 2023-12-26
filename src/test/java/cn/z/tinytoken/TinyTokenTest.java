package cn.z.tinytoken;

import cn.z.id.Id;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;

/**
 * <h1>轻量级权限认证测试</h1>
 *
 * <p>
 * createDate 2023/07/24 11:54:46
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootApplication
@SpringBootTest
@Slf4j
class TinyTokenTest {

    private final T4s t4s;

    @Autowired
    TinyTokenTest(T4s t4s) {
        this.t4s = t4s;
    }

    /**
     * 直接调用
     */
    @Test
    void test00Normal() {
        String tokenValue = "1234";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("token", tokenValue);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = t4s.getToken();
        log.info(token);
        assert tokenValue.equals(token);
    }

    /**
     * 解析16位随机字符串
     */
    @Test
    void test02Decode() {
        long time = Id.next();
        String encode = T4s.encode(time);
        log.info("time {} -> {}", time, encode);
        long[] decode = T4s.decode(encode);
        log.info("时间戳 {} 机器码 {} 序列号 {}", new Timestamp(decode[0]), decode[1], decode[2]);
        long format = Id.format(decode[1], decode[0], decode[2]);
        assert time == format;
    }

}
