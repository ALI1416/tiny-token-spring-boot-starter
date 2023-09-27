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
        request.addHeader("tinytoken", tokenValue);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = t4s.getToken();
        log.info(token);
        assert tokenValue.equals(token);
    }

    /**
     * Base62编码雪花ID
     */
    @Test
    void test02Base62Id() {
        long id = Id.next();
        String encode = Base62.encode(id);
        long decode = Base62.decode(encode);
        log.info("{} {}", id, encode);
        log.info("解析Base62编码的雪花ID字符串 {}", T4s.parseBase62SnowId(encode));
        assert id == decode;
    }

}
