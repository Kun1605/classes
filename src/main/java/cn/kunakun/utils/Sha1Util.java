package cn.kunakun.utils;

import java.security.MessageDigest;

public class Sha1Util {
    public static String encode(String text) {
        //---拼接
        StringBuffer sb = new StringBuffer();
        try {
            //获取sha1加密算法
            MessageDigest instance = MessageDigest.getInstance("MD5");
            //对字符窜进行加密 ---返回数组
            byte[] digest = instance.digest(text.getBytes());
            //遍历数组---转为16进制 32位的
            for (byte b : digest) {
                //获取字节低八位
                int i = b & 0xff;
                //转为16进制
                String hexString = Integer.toHexString(i);
                //如果i为1位--前方补0--凑成两位
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}