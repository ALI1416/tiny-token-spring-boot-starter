package cn.z.tinytoken;

import java.nio.charset.StandardCharsets;

/**
 * <h1>Base62</h1>
 *
 * <p>
 * createDate 2020/11/14 21:20:10
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.1.0
 **/
public class Base62 {

    /**
     * 字母表
     */
    public static final byte[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8);
    /**
     * 反向映射表
     */
    public static final byte[] LOOKUP = lookup();

    private Base62() {
    }

    /**
     * 编码long型数字
     *
     * @param number long型数字
     * @return Base62字符串
     */
    public static String encode(long number) {
        if (number == 0) {
            return String.valueOf((char) ALPHABET[0]);
        }
        StringBuilder sb = new StringBuilder();
        for (; number > 0; number /= 62) {
            sb.append((char) ALPHABET[(int) (number % 62)]);
        }
        return sb.reverse().toString();
    }

    /**
     * 解码long型数字
     *
     * @param string Base62字符串
     * @return long型数字
     */
    public static long decode(String string) {
        char[] chars = string.toCharArray();
        int length = chars.length;
        long number = 0;
        long power = 1;
        for (int i = 0; i < length; i++, power *= 62) {
            number += LOOKUP[chars[length - i - 1]] * power;
        }
        return number;
    }

    /**
     * 获取反向映射表
     *
     * @return 映射表
     */
    public static byte[] lookup() {
        byte[] lookup = new byte[123];
        for (int i = 0; i < ALPHABET.length; i++) {
            lookup[ALPHABET[i]] = (byte) i;
        }
        return lookup;
    }

}
