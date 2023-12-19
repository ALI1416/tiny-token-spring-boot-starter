package com.demo.controller;

import cn.z.tinytoken.T4s;
import cn.z.tinytoken.entity.TokenInfo;
import cn.z.tinytoken.entity.TokenInfoExtra;
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
     * 获取参数<br>
     * http://localhost:8080/getProperties
     */
    @GetMapping("getProperties")
    public String getProperties() {
        return t4s.getHeader() + " " + t4s.getPrefix() + " " + t4s.getTimeout();
    }

    /**
     * 设置token(token使用Base62编码的雪花ID 过期时间使用默认值)<br>
     * http://localhost:8080/setToken?id=123
     */
    @GetMapping("setToken")
    public String setToken(long id) {
        return t4s.setToken(id);
    }

    /**
     * 设置token(token使用Base62编码的雪花ID)<br>
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
     * 设置拓展内容(当前Context)<br>
     * http://localhost:8080/setExtra?extra=asd
     */
    @GetMapping("setExtra")
    public boolean setExtra(String extra) {
        return t4s.setExtra(extra);
    }

    /**
     * 设置拓展内容<br>
     * http://localhost:8080/setExtra2?token=qwe&extra=asd
     */
    @GetMapping("setExtra2")
    public boolean setExtra(String token, String extra) {
        return t4s.setExtra(token, extra);
    }

    /**
     * 清除拓展内容(当前Context)<br>
     * http://localhost:8080/clearExtra
     */
    @GetMapping("clearExtra")
    public boolean clearExtra() {
        return t4s.clearExtra();
    }

    /**
     * 清除拓展内容<br>
     * http://localhost:8080/clearExtra2?token=qwe
     */
    @GetMapping("clearExtra2")
    public boolean clearExtra(String token) {
        return t4s.clearExtra(token);
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
     * 获取id(当前Context)<br>
     * http://localhost:8080/getId
     */
    @GetMapping("getId")
    public Long getId() {
        return t4s.getId();
    }

    /**
     * 获取id<br>
     * http://localhost:8080/getId2?token=qwe
     */
    @GetMapping("getId2")
    public Long getId(String token) {
        return t4s.getId(token);
    }

    /**
     * 获取拓展内容(当前Context)<br>
     * http://localhost:8080/getExtra
     */
    @GetMapping("getExtra")
    public String getExtra() {
        return t4s.getExtra();
    }

    /**
     * 获取拓展内容<br>
     * http://localhost:8080/getExtra?token=qwe
     */
    @GetMapping("getExtra2")
    public String getExtra(String token) {
        return t4s.getExtra(token);
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
    public TokenInfo getInfoByToken() {
        return t4s.getInfoByToken();
    }

    /**
     * 获取信息<br>
     * http://localhost:8080/getInfoByToken2?token=qwe
     */
    @GetMapping("getInfoByToken2")
    public TokenInfo getInfoByToken(String token) {
        return t4s.getInfoByToken(token);
    }

    /**
     * 获取信息列表<br>
     * http://localhost:8080/getInfoById?id=123
     */
    @GetMapping("getInfoById")
    public List<TokenInfo> getInfoById(long id) {
        return t4s.getInfoById(id);
    }

    /**
     * 获取所有信息列表<br>
     * http://localhost:8080/getInfo
     */
    @GetMapping("getInfo")
    public List<TokenInfo> getInfo() {
        return t4s.getInfo();
    }

    /**
     * 获取所有永不过期信息列表<br>
     * http://localhost:8080/getInfoPersist
     */
    @GetMapping("getInfoPersist")
    public List<TokenInfo> getInfoPersist() {
        return t4s.getInfoPersist();
    }

    /**
     * 获取拓展信息(当前Context)<br>
     * http://localhost:8080/getInfoExtraByToken
     */
    @GetMapping("getInfoExtraByToken")
    public TokenInfoExtra getInfoExtraByToken() {
        return t4s.getInfoExtraByToken();
    }

    /**
     * 获取拓展信息<br>
     * http://localhost:8080/getInfoExtraByToken2?token=qwe
     */
    @GetMapping("getInfoExtraByToken2")
    public TokenInfoExtra getInfoExtraByToken(String token) {
        return t4s.getInfoExtraByToken(token);
    }

    /**
     * 获取信息拓展列表<br>
     * http://localhost:8080/getInfoExtraById?id=123
     */
    @GetMapping("getInfoExtraById")
    public List<TokenInfoExtra> getInfoExtraById(long id) {
        return t4s.getInfoExtraById(id);
    }

    /**
     * 获取所有信息拓展列表<br>
     * http://localhost:8080/getInfoExtra
     */
    @GetMapping("getInfoExtra")
    public List<TokenInfoExtra> getInfoExtra() {
        return t4s.getInfoExtra();
    }

    /**
     * 获取所有永不过期信息拓展列表<br>
     * http://localhost:8080/getInfoExtraPersist
     */
    @GetMapping("getInfoExtraPersist")
    public List<TokenInfoExtra> getInfoExtraPersist() {
        return t4s.getInfoExtraPersist();
    }

}
