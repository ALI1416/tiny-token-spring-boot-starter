package cn.z.tinytoken;

import java.util.List;
import java.util.Set;

/**
 * <h1>轻量级权限认证接口</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @param <K> token的类型<br>
 *            推荐String
 * @param <V> id的类型<br>
 *            推荐Long、Integer、String
 * @param <E> 拓展内容的类型
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public interface TinyToken<K, V, E> {

    /**
     * 设置token，token使用UUID，过期时间使用默认值
     *
     * @param id id
     * @return token
     */
    K setToken(V id);

    /**
     * 设置token，token使用UUID
     *
     * @param id      id
     * @param timeout 过期时间(秒)
     * @return token
     */
    K setToken(V id, int timeout);

    /**
     * 设置token，过期时间使用默认值
     *
     * @param token token
     * @param id    id
     */
    void setToken(K token, V id);

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param timeout 过期时间(秒)
     */
    void setToken(K token, V id, int timeout);

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)
     */
    void setToken(K token, V id, E extra, long timeout);

    /**
     * 获取token，通过当前Context，不判断是否有效
     *
     * @return token(不存在返回null)
     */
    K getToken();

    /**
     * 获取token，通过当前Context，并判断是否有效
     *
     * @return token(不存在或无效返回null)
     */
    K getTokenValid();

    /**
     * 获取token列表，通过id
     *
     * @param id id(不存在返回[])
     * @return token列表
     */
    Set<K> getToken(V id);

    /**
     * 获取id，通过当前Context
     *
     * @return id(不存在返回null)
     */
    V getId();

    /**
     * 获取id，通过token
     *
     * @param token token
     * @return id(不存在返回null)
     */
    V getId(K token);

    /**
     * 获取过期时间，通过当前Context
     *
     * @return 过期时间(秒)(不存在返回null ， 不过期返回 - 1)
     */
    Long getTimeoutByToken();

    /**
     * 获取过期时间，通过token
     *
     * @param token token
     * @return 过期时间(秒)(不存在返回null ， 不过期返回 - 1)
     */
    Long getTimeoutByToken(K token);

    /**
     * 获取过期时间，通过token
     *
     * @param id id
     * @return 过期时间(秒)列表(不存在返回[] ， 不过期返回 - 1)
     */
    List<Long> getTimeoutById(V id);

    /**
     * token是否存在
     *
     * @param token token
     */
    boolean existByToken(K token);

    /**
     * id是否存在
     *
     * @param id id
     */
    boolean existById(V id);

    /**
     * 删除，通过token
     *
     * @param token token
     */
    void deleteByToken(K token);

    /**
     * 删除，通过id
     *
     * @param id id
     */
    void deleteById(V id);

    /**
     * 获取信息，通过token
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    Info<K, V, E> getInfoByToken(K token);

    /**
     * 获取信息列表，通过id
     *
     * @param id id
     * @return 信息列表(不存在返回[])
     */
    List<Info<K, V, E>> getInfoById(V id);

    /**
     * 信息
     *
     * @param <K> token的类型<br>
     *            推荐String
     * @param <V> id的类型<br>
     *            推荐Long、Integer、String
     * @param <E> 拓展内容的类型
     */
    class Info<K, V, E> {

        /**
         * token
         */
        private final K token;
        /**
         * id
         */
        private final V id;
        /**
         * 拓展内容
         */
        private final E extra;
        /**
         * 过期时间(秒)<br>
         * -1:不过期
         */
        private final long timeout;

        private Info() {
            this.token = null;
            this.id = null;
            this.extra = null;
            this.timeout = -1;
        }

        public Info(K token, V id, long timeout) {
            this.token = token;
            this.id = id;
            this.extra = null;
            this.timeout = timeout;
        }

        public Info(K token, V id, E extra, long timeout) {
            this.token = token;
            this.id = id;
            this.extra = extra;
            this.timeout = timeout;
        }

        public K getToken() {
            return token;
        }

        public V getId() {
            return id;
        }

        public E getExtra() {
            return extra;
        }

        public long getTimeout() {
            return timeout;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "token=" + token +
                    ", id=" + id +
                    ", extra=" + extra +
                    ", timeout=" + timeout +
                    '}';
        }

    }

}
