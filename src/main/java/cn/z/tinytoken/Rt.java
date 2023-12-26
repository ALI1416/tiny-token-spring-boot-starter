package cn.z.tinytoken;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis模板类</h1>
 *
 * <p>
 * createDate 2023/08/01 16:20:58
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class Rt {

    /**
     * Redis模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造函数(自动注入)
     *
     * @param factory RedisConnectionFactory
     */
    public Rt(RedisConnectionFactory factory) {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 使用String序列化
        redisTemplate.setDefaultSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.afterPropertiesSet();
    }

    /**
     * 删除key(del)
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个key(del)
     *
     * @param keys 键
     * @return 成功个数
     */
    public Long deleteMulti(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 模糊查询，一次扫描1000条(scan)
     *
     * @param match 匹配模式<br>
     *              * : 匹配0+个任意字符<br>
     *              ? : 匹配1个任意字符<br>
     *              [abc] : 匹配1个指定字符(括号内字符abc)<br>
     *              [^abc] : 不匹配1个指定字符(括号内字符abc)<br>
     *              [A-z] : 匹配1个指定字符(括号内字符A-z)<br>
     *              \ : 转义(字符*?[]^-\等)
     * @return 键列表
     */
    public List<String> scan(String match) {
        List<String> list = new ArrayList<>();
        try (Cursor<String> cursor = (Cursor<String>) redisTemplate.executeWithStickyConnection(
                connection -> new ConvertingCursor<>(
                        connection.scan(ScanOptions.scanOptions().match(match).count(1000).build()),
                        redisTemplate.getKeySerializer()::deserialize)
        )) {
            if (cursor != null) {
                while (cursor.hasNext()) {
                    list.add(cursor.next());
                }
            }
        }
        return list;
    }

    /**
     * 指定超时时间(expire)
     *
     * @param key     键
     * @param timeout 超时时间(秒，<=0删除)
     * @return 是否成功
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 指定为持久数据(persist)
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 获取超时时间(ttl)
     *
     * @param key 键
     * @return 超时时间(秒 ， - 1不过期 ， - 2不存在)
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 放入(set)
     *
     * @param <T>   数据类型
     * @param key   键(已存在会被覆盖)
     * @param value 值
     * @since 1.2.0
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 放入，并设置超时时间(setEX)
     *
     * @param <T>     数据类型
     * @param key     键(已存在会被覆盖)
     * @param value   值
     * @param timeout 超时时间(秒，必须>0)
     */
    public <T> void set(String key, T value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取(get)
     *
     * @param key 键(不存在返回null)
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取多个(mGet)
     *
     * @param keys 多个键(不存在返回null)
     * @return 值列表
     */
    public List<Object> getMulti(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

}
