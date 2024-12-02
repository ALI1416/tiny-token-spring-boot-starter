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
     * @param message 详细信息
     */
    public TinyTokenException(String message) {
        super(message);
    }

    /**
     * TinyToken异常
     *
     * @param message 详细信息
     * @param cause   原因
     */
    public TinyTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * TinyToken异常
     *
     * @param cause 原因
     */
    public TinyTokenException(Throwable cause) {
        super(cause);
    }

    /**
     * TinyToken异常
     *
     * @param message            详细信息
     * @param cause              原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否为可写的
     */
    protected TinyTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
