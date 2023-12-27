package com.demo.controller;

import cn.z.tinytoken.T4s;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
     * 用户名和密码是否正确<br>
     * http://localhost:8080/isCorrect?username=root&password=admin<br>
     * http://localhost:8080/isCorrect?username=root&password=admin1
     */
    @GetMapping("isCorrect")
    public boolean isCorrect(String username, String password) {
        return t4s.isCorrect(username, password);
    }

    /**
     * 设置token(token使用16位随机字符串 过期时间使用默认值)<br>
     * http://localhost:8080/setToken
     */
    @GetMapping("setToken")
    public String setToken() {
        return t4s.setToken();
    }

    /**
     * 设置token(token使用16位随机字符串)<br>
     * http://localhost:8080/setToken2?timeout=100
     */
    @GetMapping("setToken2")
    public String setToken(long timeout) {
        return t4s.setToken(timeout);
    }

    /**
     * 设置token<br>
     * http://localhost:8080/setToken3?token=qwe&timeout=100
     */
    @GetMapping("setToken3")
    public String setToken(String token, long timeout) {
        t4s.setToken(token, timeout);
        return "ok";
    }

    /**
     * 获取token(当前Context 不判断是否有效)<br>
     * http://localhost:8080/getToken
     */
    @GetMapping("getToken")
    public String getToken() {
        return t4s.getToken();
    }

    /**
     * 获取token(当前Context 判断是否有效)<br>
     * http://localhost:8080/getTokenValid
     */
    @GetMapping("getTokenValid")
    public String getTokenValid() {
        return t4s.getTokenValid();
    }

    /**
     * token是否存在(当前Context)<br>
     * http://localhost:8080/existByToken
     */
    @GetMapping("existByToken")
    public boolean existByToken() {
        return t4s.existByToken();
    }

    /**
     * token是否存在<br>
     * http://localhost:8080/existByToken2?token=qwe
     */
    @GetMapping("existByToken2")
    public boolean existByToken(String token) {
        return t4s.existByToken(token);
    }

    /**
     * 删除(当前Context)<br>
     * http://localhost:8080/deleteByToken
     */
    @GetMapping("deleteByToken")
    public Boolean deleteByToken() {
        return t4s.deleteByToken();
    }

    /**
     * 删除<br>
     * http://localhost:8080/deleteByToken2?token=qwe
     */
    @GetMapping("deleteByToken2")
    public Boolean deleteByToken(String token) {
        return t4s.deleteByToken(token);
    }

    /**
     * 设置过期时间(当前Context 过期时间使用默认值)<br>
     * http://localhost:8080/expire
     */
    @GetMapping("expire")
    public Boolean expire() {
        return t4s.expire();
    }

    /**
     * 设置过期时间(当前Context)<br>
     * http://localhost:8080/expire2?timeout=100
     */
    @GetMapping("expire2")
    public Boolean expire(long timeout) {
        return t4s.expire(timeout);
    }

    /**
     * 设置过期时间(过期时间使用默认值)<br>
     * http://localhost:8080/expire3?token=qwe
     */
    @GetMapping("expire3")
    public Boolean expire(String token) {
        return t4s.expire(token);
    }

    /**
     * 设置过期时间<br>
     * http://localhost:8080/expire4?token=qwe&timeout=100
     */
    @GetMapping("expire4")
    public Boolean expire(String token, long timeout) {
        return t4s.expire(token, timeout);
    }

    /**
     * 设置永不过期(当前Context)<br>
     * http://localhost:8080/persist
     */
    @GetMapping("persist")
    public Boolean persist() {
        return t4s.persist();
    }

    /**
     * 设置永不过期<br>
     * http://localhost:8080/persist2?token=qwe
     */
    @GetMapping("persist2")
    public Boolean persist(String token) {
        return t4s.persist(token);
    }

    /**
     * 获取信息(当前Context)<br>
     * http://localhost:8080/getInfoByToken
     */
    @GetMapping("getInfoByToken")
    public Map.Entry<String, Long> getInfoByToken() {
        return t4s.getInfoByToken();
    }

    /**
     * 获取信息<br>
     * http://localhost:8080/getInfoByToken2?token=qwe
     */
    @GetMapping("getInfoByToken2")
    public Map.Entry<String, Long> getInfoByToken(String token) {
        return t4s.getInfoByToken(token);
    }

    /**
     * 获取token列表<br>
     * http://localhost:8080/getTokenList
     */
    @GetMapping("getTokenList")
    public List<String> getTokenList() {
        return t4s.getTokenList();
    }

    /**
     * 获取所有信息列表<br>
     * http://localhost:8080/getInfo
     */
    @GetMapping("getInfo")
    public List<Map.Entry<String, Long>> getInfo() {
        return t4s.getInfo();
    }

    /**
     * 获取所有永不过期信息列表<br>
     * http://localhost:8080/getInfoPersist
     */
    @GetMapping("getInfoPersist")
    public List<String> getInfoPersist() {
        return t4s.getInfoPersist();
    }

}
