package cn.z.tinytoken;

import java.util.List;

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
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param timeout 过期时间(秒)<br>
     *                -1:不过期
     */
    void setToken(K token, V id, int timeout);

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)<br>
     *                -1:不过期
     */
    void setToken(K token, V id, E extra, long timeout);

    /**
     * 获取token
     *
     * @return token
     */
    K getToken();

    /**
     * 获取id对应的token列表
     *
     * @param id id
     * @return token列表
     */
    List<K> getToken(V id);

    /**
     * 获取id
     *
     * @return id
     */
    V getId();

    /**
     * 获取token对应的id
     *
     * @param token token
     * @return id
     */
    V getId(K token);

    /**
     * 删除，通过token
     *
     * @param token token
     */
    void deleteForToken(K token);

    /**
     * 删除，通过id
     *
     * @param id id
     */
    void deleteForId(V id);

    /**
     * 获取token对应的信息
     *
     * @param token token
     * @return 信息
     */
    Info<K, V, E> getInfoForToken(K token);

    /**
     * 获取id对应的信息列表
     *
     * @param id id
     * @return 信息列表
     */
    List<Info<K, V, E>> getInfoForId(V id);

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
