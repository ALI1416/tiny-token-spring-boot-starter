package com.demo.controller;

import cn.z.tinytoken.T4s;
import cn.z.tinytoken.TinyToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 设置token(token使用UUID 过期时间使用默认值)<br>
     * http://localhost:8080/setToken?id=123
     */
    @GetMapping("setToken")
    public String setToken(long id) {
        return t4s.setToken(id);
    }

    /**
     * 设置token(token使用UUID)<br>
     * http://localhost:8080/setToken2?id=1234&timeout=100
     */
    @GetMapping("setToken2")
    public String setToken(long id, long timeout) {
        return t4s.setToken(id, timeout);
    }

    /**
     * 设置token(过期时间使用默认值)<br>
     * http://localhost:8080/setToken3?id=12345&token=qwe
     */
    @GetMapping("setToken3")
    public String setToken(long id, String token) {
        t4s.setToken(id, token);
        return "ok";
    }

    /**
     * 设置token<br>
     * http://localhost:8080/setToken4?id=12346&token=qwe&timeout=100
     */
    @GetMapping("setToken4")
    public String setToken(long id, String token, long timeout) {
        t4s.setToken(id, token, timeout);
        return "ok";
    }

    /**
     * 设置token<br>
     * http://localhost:8080/setToken5?id=123467&token=qwe&extra=asd&timeout=100
     */
    @GetMapping("setToken5")
    public String setToken(long id, String token, String extra, long timeout) {
        t4s.setToken(id, token, extra, timeout);
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
     * 获取token列表<br>
     * http://localhost:8080/getToken2?id=123
     */
    @GetMapping("getToken2")
    public List<String> getToken(long id) {
        return t4s.getToken(id);
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
     * id是否存在<br>
     * http://localhost:8080/existById?id=123
     */
    @GetMapping("existById")
    public boolean existById(long id) {
        return t4s.existById(id);
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
     * 删除<br>
     * http://localhost:8080/deleteById?id=123
     */
    @GetMapping("deleteById")
    public Long deleteById(long id) {
        return t4s.deleteById(id);
    }

    /**
     * 设置过期时间(当前Context)<br>
     * http://localhost:8080/expire?timeout=100
     */
    @GetMapping("expire")
    public Boolean expire(long timeout) {
        return t4s.expire(timeout);
    }

    /**
     * 设置过期时间<br>
     * http://localhost:8080/expire2?token=qwe&timeout=100
     */
    @GetMapping("expire2")
    public Boolean expire(String token, long timeout) {
        return t4s.expire(token, timeout);
    }

    /**
     * 设置永不过期(当前Context)<br>
     * http://localhost:8080/persist?token=qwe
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
     * http://localhost:8080/getInfoByToken?token=qwe
     */
    @GetMapping("getInfoByToken")
    public TinyToken.Info<Long, String> getInfoByToken() {
        return t4s.getInfoByToken();
    }

    /**
     * 获取信息<br>
     * http://localhost:8080/getInfoByToken2?token=qwe
     */
    @GetMapping("getInfoByToken2")
    public TinyToken.Info<Long, String> getInfoByToken(String token) {
        return t4s.getInfoByToken(token);
    }

    /**
     * 获取信息列表<br>
     * http://localhost:8080/getInfoById?id=123
     */
    @GetMapping("getInfoById")
    public List<TinyToken.Info<Long, String>> getInfoById(long id) {
        return t4s.getInfoById(id);
    }

    /**
     * 获取拓展信息(当前Context)<br>
     * http://localhost:8080/getInfoExtraByToken?token=qwe
     */
    @GetMapping("getInfoExtraByToken")
    public TinyToken.InfoExtra<Long, String, String> getInfoExtraByToken() {
        return t4s.getInfoExtraByToken();
    }

    /**
     * 获取拓展信息<br>
     * http://localhost:8080/getInfoExtraByToken2?token=qwe
     */
    @GetMapping("getInfoExtraByToken2")
    public TinyToken.InfoExtra<Long, String, String> getInfoExtraByToken(String token) {
        return t4s.getInfoExtraByToken(token);
    }

    /**
     * 获取信息拓展列表<br>
     * http://localhost:8080/getInfoExtraById?id=123
     */
    @GetMapping("getInfoExtraById")
    public List<TinyToken.InfoExtra<Long, String, String>> getInfoExtraById(long id) {
        return t4s.getInfoExtraById(id);
    }

}
