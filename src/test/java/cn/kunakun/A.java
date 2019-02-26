package cn.kunakun;

import java.math.BigDecimal;

public class A {
    public static void main(String[] args) {
        BigDecimal timelimit35 = new BigDecimal(100).divide(
                new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(timelimit35);

    }
}
