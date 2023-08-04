package com.demo.controller;

import cn.z.tinytoken.T4s;
import cn.z.tinytoken.TinyToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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
     * 设置token，token使用UUID，过期时间使用默认值<br>
     * http://localhost:8080/setToken?id=123
     */
    @GetMapping("setToken")
    public String setToken(long id) {
        return t4s.setToken(id);
    }

    /**
     * 设置token，token使用UUID<br>
     * http://localhost:8080/setToken2?id=1234&timeout=100
     */
    @GetMapping("setToken2")
    public String setToken(long id, long timeout) {
        return t4s.setToken(id, timeout);
    }

    /**
     * 设置token，过期时间使用默认值<br>
     * http://localhost:8080/setToken3?token=qwe&id=12345
     */
    @GetMapping("setToken3")
    public String setToken(String token, long id) {
        t4s.setToken(token, id);
        return "ok";
    }

    /**
     * 设置token<br>
     * http://localhost:8080/setToken4?token=qwe&id=12346&timeout=100
     */
    @GetMapping("setToken4")
    public String setToken(String token, long id, long timeout) {
        t4s.setToken(token, id, timeout);
        return "ok";
    }

    /**
     * 设置token<br>
     * http://localhost:8080/setToken5
     */
    @GetMapping("setToken5")
    public String setToken() {
        t4s.setToken("", 0L, null, 0L);
        return "ok";
    }

    /**
     * 获取token，通过当前Context，不判断是否有效<br>
     * http://localhost:8080/getToken
     */
    @GetMapping("getToken")
    public String getToken() {
        return t4s.getToken();
    }

    /**
     * 获取token，通过当前Context，并判断是否有效<br>
     * http://localhost:8080/getTokenValid
     */
    @GetMapping("getTokenValid")
    public String getTokenValid() {
        return t4s.getTokenValid();
    }

    /**
     * 获取token列表，通过id<br>
     * http://localhost:8080/getToken2?id=123
     */
    @GetMapping("getToken2")
    public Set<String> getToken(long id) {
        return t4s.getToken(id);
    }

    /**
     * token是否存在<br>
     * http://localhost:8080/existByToken?token=qwe
     */
    @GetMapping("existByToken")
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
     * 删除，通过token<br>
     * http://localhost:8080/deleteByToken?token=qwe
     */
    @GetMapping("deleteByToken")
    public Boolean deleteByToken(String token) {
        return t4s.deleteByToken(token);
    }

    /**
     * 删除，通过id<br>
     * http://localhost:8080/deleteById?id=123
     */
    @GetMapping("deleteById")
    public Long deleteById(long id) {
        return t4s.deleteById(id);
    }

    /**
     * 设置过期时间，通过token<br>
     * http://localhost:8080/expire?token=qwe&timeout=100
     */
    @GetMapping("expire")
    public Boolean expire(String token, long timeout) {
        return t4s.expire(token, timeout);
    }

    /**
     * 设置永不过期，通过token<br>
     * http://localhost:8080/persist?token=qwe
     */
    @GetMapping("persist")
    public Boolean persist(String token) {
        return t4s.persist(token);
    }

    /**
     * 获取信息，通过token<br>
     * http://localhost:8080/getInfoByToken?token=qwe
     */
    @GetMapping("getInfoByToken")
    public TinyToken.Info<String, Long, Void> getInfoByToken(String token) {
        return t4s.getInfoByToken(token);
    }

    /**
     * 获取信息列表，通过id<br>
     * http://localhost:8080/getInfoById?id=123
     */
    @GetMapping("getInfoById")
    public List<TinyToken.Info<String, Long, Void>> getInfoById(long id) {
        return t4s.getInfoById(id);
    }

}
