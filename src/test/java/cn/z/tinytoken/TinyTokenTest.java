package cn.z.tinytoken;

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
        String username = "root";
        String password = "admin";
        String encode = T4s.encode(username + ":" + password);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", encode);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String auth = t4s.getAuth();
        log.info("encode {} auth {}", encode, auth);
        assert t4s.isCorrect(auth);
    }

    /**
     * base64
     */
    @Test
    void testBase64() {
        String username = "123";
        String password = "456";
        String s = username + ":" + password;
        String encode = T4s.encode(s);
        String decode = T4s.decode(encode);
        log.info("s {} encode {} decode {}", s, encode, decode);
        assert s.equals(decode);
    }

}
