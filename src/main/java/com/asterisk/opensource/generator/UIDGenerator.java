package com.asterisk.opensource.generator;

import org.joda.time.DateTime;

/**
 * Title: UIDGenerator
 * Description: Unique id for common business order number
 *
 */
public final class UIDGenerator {

    private static final int[] DEFAULT_CONFOUNDED = {3, 6, 7, 1, 8, 9, 5, 2};

    private UIDGenerator() {
        // Helper function
    }

    /**
     * 把一个输入整数置换为另一个数
     *
     * @param num        想要乱序的数
     * @param confounded 乱序因子, 长度要大于或等于想要乱序的数
     * @return 乱序结果, 长度为乱序因子这个数组的长度
     */
    private static long confuse(long num, int[] confounded) {
        int numLength = String.valueOf(num).length();
        int length = confounded.length;

        //检查输入数值是否过大
        if (length < numLength) {
            throw new IllegalArgumentException(
                "confounded length must greater then number length, " + length + " : " + numLength);
        }

        long output = 0L;

        int confoundedIndex = (int) (num % 10 % 8);
        int paddingLength = length - numLength;

        for (int i = 0; i < paddingLength; i++) {
            confoundedIndex = (confoundedIndex + 1) % 8;
            output = output * 10 + (confounded[confoundedIndex] % 10);
        }

        for (int i = 0; i < numLength; i++) {
            confoundedIndex = (confoundedIndex + 1) % 8;
            output = (long) (output * 10 + ((num / Math.pow(10, numLength - i - 1.0) + confounded[confoundedIndex]) % 10));
        }

        return output;
    }

    /**
     * 生成业务流水号, 生成逻辑, 前三位为业务逻辑前缀标记, 然后是年月日的6位数字, 然后是基于sequence的8位乱序数字
     * 唯一性上限为: 不考虑业务前缀情况下, 每日可以保证有一亿个唯一的流水号.
     *
     * @param sequence 序列号
     * @param bizFlag  业务标识
     * @return 业务流水号
     */
    public static String generateBizUID(long sequence, String bizFlag) throws IllegalArgumentException { // NOSONAR
        return generateBizUID(sequence, bizFlag, new DateTime().toString("yyMMdd"));
    }

    /**
     * 生成业务流水号
     *
     * @param sequence 序列号 最终变为8位乱序字串
     * @param bizFlag  业务标识 由外部系统指定
     * @param dateStr  时间字符串 年月日
     * @return 业务流水号
     */
    private static String generateBizUID(long sequence, String bizFlag, String dateStr) {
        if (bizFlag == null || bizFlag.length() != 3) {
            throw new IllegalArgumentException("bizFlag must be 3 length");
        }
        return bizFlag + dateStr + confuse(sequence, DEFAULT_CONFOUNDED);
    }
}
