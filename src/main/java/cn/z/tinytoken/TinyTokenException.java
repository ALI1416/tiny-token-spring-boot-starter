package cn.z.tinytoken;

/**
 * <h1>TinyToken异常类</h1>
 *
 * <p>
 * createDate 2023/08/01 17:40:03
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class TinyTokenException extends RuntimeException {

    /**
     * TinyToken异常
     */
    public TinyTokenException() {
        super();
    }

    /**
     * TinyToken异常
     *
     * @param message 信息
     */
    public TinyTokenException(String message) {
        super(message);
    }

}
