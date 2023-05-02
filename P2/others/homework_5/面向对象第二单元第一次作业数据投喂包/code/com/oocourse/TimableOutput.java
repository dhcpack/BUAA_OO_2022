package com.oocourse;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 时间化输出模块
 */

@SuppressWarnings("ALL")
public abstract class TimableOutput {
    /**
     * 初始时间戳
     */
    private static long startTimestamp = 0;

    /**
     * 随机起始字符串
     */
    private static final String RANDOM_START_STRING = "This is random start string.";

    /**
     * 初始化初始时间戳
     * 需要在程序最开始的时候执行，且只能初始化一次
     *
     * @return 是否初始化成功
     */
    public static boolean initStartTimestamp() {
        if (startTimestamp == 0) {
            startTimestamp = System.currentTimeMillis();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(stream);
            println(RANDOM_START_STRING, printStream);
            printStream.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取相对时间戳
     *
     * @param timestamp 时间戳
     * @return 相对时间戳
     */
    private static long getRelativeTimestamp(long timestamp) {
        return timestamp - startTimestamp;
    }

    /**
     * 获取相对时间戳（当前时间）
     *
     * @return 当前时间的相对时间戳
     */
    private static long getRelativeTimestamp() {
        return getRelativeTimestamp(System.currentTimeMillis());
    }

    /**
     * 秒 --> 毫秒，换算比例
     */
    private static final double SECOND_TO_MILLISECOND = 1000.0;

    /**
     * 带时间戳的字符串
     */
    private static class ObjectWithTimestamp {
        private final long timestamp;
        private final Object object;

        /**
         * 构造函数
         *
         * @param timestamp 时间戳
         * @param object    对象
         */
        ObjectWithTimestamp(long timestamp, Object object) {
            this.timestamp = timestamp;
            this.object = object;
        }

        /**
         * 构造函数（使用当前时间戳）
         *
         * @param object 对象
         */
        ObjectWithTimestamp(Object object) {
            this(System.currentTimeMillis(), object);
        }

        /**
         * 获取时间戳
         *
         * @return 时间戳
         */
        public long getTimestamp() {
            return timestamp;
        }

        /**
         * 获取相对时间戳
         *
         * @return 相对时间戳
         */
        public long getRelativeTimestamp() {
            return TimableOutput.getRelativeTimestamp(getTimestamp());
        }

        /**
         * 获取秒为单位的相对时间戳
         *
         * @return 秒为单位的相对时间戳
         */
        public double getRelativeSecondTimestamp() {
            return getRelativeTimestamp() / SECOND_TO_MILLISECOND;
        }

        /**
         * 获取存储对象
         *
         * @return 存储对象
         */
        public Object getObject() {
            return object;
        }

        /**
         * 获取原文本
         *
         * @return 原文本
         */
        @Override
        public String toString() {
            return String.format("[%9.4f]%s", getRelativeSecondTimestamp(), getObject().toString());
        }

    }

    /**
     * 默认输出流
     */
    private static final PrintStream DEFAULT_PRINT_STREAM = System.out;

    /**
     * 输出对象（换行）
     *
     * @param obj    对象
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(Object obj, PrintStream stream) {
        ObjectWithTimestamp value = new ObjectWithTimestamp(obj);
        stream.println(value.toString());
        stream.flush();
        return value.getTimestamp();
    }

    /**
     * 输出对象（换行，默认输出流）
     *
     * @param obj 对象
     * @return 输出的时间戳
     */
    public static long println(Object obj) {
        return println(obj, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出整型值（换行）
     *
     * @param i      整型值
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(int i, PrintStream stream) {
        return println(String.valueOf(i), stream);
    }

    /**
     * 输出整型值（换行，默认输出流）
     *
     * @param i 整型值
     * @return 输出的时间戳
     */
    public static long println(int i) {
        return println(i, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出布尔型值（换行）
     *
     * @param b      布尔型值
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(boolean b, PrintStream stream) {
        return println(String.valueOf(b), stream);
    }

    /**
     * 输出布尔型值（换行，默认输出流）
     *
     * @param b 布尔型值
     * @return 输出的时间戳
     */
    public static long println(boolean b) {
        return println(b, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出字符值（换行）
     *
     * @param c      字符值
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(char c, PrintStream stream) {
        return println(String.valueOf(c), stream);
    }

    /**
     * 输出字符值（换行，默认输出流）
     *
     * @param c 字符值
     * @return 输出的时间戳
     */
    public static long println(char c) {
        return println(c, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出长整型值（换行）
     *
     * @param l      长整型值
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(long l, PrintStream stream) {
        return println(String.valueOf(l), stream);
    }

    /**
     * 输出长整型值（换行，默认输出流）
     *
     * @param l 长整型值
     * @return 输出的时间戳
     */
    public static long println(long l) {
        return println(l, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出单精度浮点型值（换行）
     *
     * @param f      单精度浮点型值
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(float f, PrintStream stream) {
        return println(String.valueOf(f), stream);
    }

    /**
     * 输出单精度浮点型值（换行，默认输出流）
     *
     * @param f 单精度浮点型值
     * @return 输出的时间戳
     */
    public static long println(float f) {
        return println(f, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出字符数组（换行）
     *
     * @param s      字符数组
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(char[] s, PrintStream stream) {
        return println(String.valueOf(s), stream);
    }

    /**
     * 输出字符数组（换行，默认输出流）
     *
     * @param s 字符数组
     * @return 输出的时间戳
     */
    public static long println(char[] s) {
        return println(s, DEFAULT_PRINT_STREAM);
    }

    /**
     * 输出双精度浮点型（换行）
     *
     * @param d      双精度浮点型
     * @param stream 输出流
     * @return 输出的时间戳
     */
    private static long println(double d, PrintStream stream) {
        return println(String.valueOf(d), stream);
    }

    /**
     * 输出双精度浮点型（换行，默认输出流）
     *
     * @param d 双精度浮点型
     * @return 输出的时间戳
     */
    public static long println(double d) {
        return println(d, DEFAULT_PRINT_STREAM);
    }
}
