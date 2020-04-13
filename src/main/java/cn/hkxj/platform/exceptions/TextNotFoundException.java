package cn.hkxj.platform.exceptions;

/**
 * 微博找不到异常
 * @author Allams
 */
public class TextNotFoundException extends RuntimeException {
    public TextNotFoundException() {
        super("该微博不存在或已经被删除");
    }
}
