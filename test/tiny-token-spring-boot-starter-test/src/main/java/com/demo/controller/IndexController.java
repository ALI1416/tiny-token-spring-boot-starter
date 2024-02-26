package com.demo.controller;

import cn.z.tinytoken.T4s;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>首页</h1>
 *
 * <p>
 * createDate 2023/08/04 17:51:17
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@AllArgsConstructor
public class IndexController {

    private final T4s t4s;

    /**
     * 获取Authorization(当前Context)<br>
     * http://localhost:8080/getAuth
     */
    @GetMapping("getAuth")
    public String getAuth() {
        return t4s.getAuth();
    }

    /**
     * 获取Authorization<br>
     * http://localhost:8080/getAuth2
     */
    @GetMapping("getAuth2")
    public String getAuth(HttpServletRequest request) {
        return t4s.getAuth(request);
    }

    /**
     * Authorization是否正确(当前Context)<br>
     * http://localhost:8080/isCorrect
     */
    @GetMapping("isCorrect")
    public boolean isCorrect() {
        return t4s.isCorrect();
    }

    /**
     * Authorization是否正确<br>
     * http://localhost:8080/isCorrect2
     */
    @GetMapping("isCorrect2")
    public boolean isCorrect2(HttpServletRequest request) {
        return t4s.isCorrect(request);
    }

    /**
     * Authorization是否正确<br>
     * http://localhost:8080/isCorrect3?auth=cm9vdDphZG1pbg==
     */
    @GetMapping("isCorrect3")
    public boolean isCorrect2(String auth) {
        return t4s.isCorrect(auth);
    }

}
