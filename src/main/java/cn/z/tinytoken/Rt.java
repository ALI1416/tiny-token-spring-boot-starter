package cn.z.tinytoken;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.util.*;
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
@Component
public class Rt {

    private static final TimeUnit SECONDS = TimeUnit.SECONDS;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造函数
     *
     * @param factory RedisConnectionFactory
     */
    public Rt(RedisConnectionFactory factory) {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key使用String序列化
        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value使用FastJson序列化
        RedisSerializer<Object> fastJsonRedisSerializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(Object object) throws SerializationException {
                if (object == null) {
                    return new byte[0];
                }
                // 写类名
                return JSON.toJSONBytes(object, "yyyy-MM-dd HH:mm:ss", JSONWriter.Feature.WriteClassName);
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null || bytes.length == 0) {
                    return null;
                }
                // 类型自动探测
                return JSON.parseObject(bytes, Object.class, JSONReader.Feature.SupportAutoType);
            }
        };
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
    }

    /**
     * key是否存在(exists)
     *
     * @param key 键
     * @return 是否存在
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 集合中存在的key的数量(exists)
     *
     * @param keys 键(重复会计算多次)
     * @return 数量
     */
    public Long existsCount(Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
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
    public Set<String> scan(String match) {
        Set<String> keys = new HashSet<>();
        Cursor<String> cursor = (Cursor<String>) redisTemplate.executeWithStickyConnection( //
                connection -> new ConvertingCursor<>( //
                        connection.scan(ScanOptions.scanOptions().match(match).count(1000).build()), //
                        redisTemplate.getKeySerializer()::deserialize));
        if (cursor == null) {
            return keys;
        }
        while (cursor.hasNext()) {
            keys.add(cursor.next());
        }
        return keys;
    }

    /**
     * 指定超时时间(expire)
     *
     * @param key     键
     * @param timeout 超时时间(秒，<=0删除)
     * @return 是否成功
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, SECONDS);
    }

    /**
     * 指定失效日期(expire)
     *
     * @param key  键
     * @param date 失效日期
     * @return 是否成功
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
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
     * @param <T>   指定数据类型
     * @param key   键(已存在会被覆盖)
     * @param value 值
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 放入，并设置超时时间(setEX)
     *
     * @param <T>     指定数据类型
     * @param key     键(已存在会被覆盖)
     * @param value   值
     * @param timeout 超时时间(秒，必须>0)
     */
    public <T> void set(String key, T value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, SECONDS);
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
