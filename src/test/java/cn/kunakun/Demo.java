package cn.kunakun;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        //红包数
        int number = 10;
        //红包总额
        float total = 100;
        float money;
        //最小红包
        double min = 0;
        double max=20;
        int i = 1;
        List math = new ArrayList();
        DecimalFormat df = new DecimalFormat("###.##");
        while (i < number) {
            //保证即使一个红包是最大的了,后面剩下的红包,每个红包也不会小于最小值
            max = total - min * (number - i);
            int k = (int) (number - i) / 2;
            //保证最后两个人拿的红包不超出剩余红包
            if (number - i <= 2) {
                k = number - i;
            }
            //最大的红包限定的平均线上下
            max = max / k;
            //保证每个红包大于最小值,又不会大于最大值
            money = (int) (min * 100 + Math.random() * (max * 100 - min * 100 + 1));
            money = (float) money / 100;
            //保留两位小数
            money = Float.parseFloat(df.format(money));
            total = (int) (total * 100 - money * 100);
            total = total / 100;
            math.add(money);
            System.out.println("第" + i + "个人拿到" + money + "剩下" + total);
            i++;
            //最后一个人拿走剩下的红包
            if (i == number) {
                math.add(total);
                System.out.println("第" + i + "个人拿到" + total + "剩下0");
            }
        }
//取数组中最大的一个值的索引
        System.out.println("本轮发红包中第" + (math.indexOf(Collections.max(math)) + 1) + "个人手气最佳");
    }


    public static void randomPackage() {
        double total_money = 2;
        int total_people = 20;
        double min_money = 5; // 每个人最少能收到0.05元、
        double max_money = 20; // 每个人最少能收到0.05元、
        double lingjiezhi = (total_money / total_people) * 100;
        double allresult = 0;
        for (int i = 0; i < total_people; i++) {
//保护值
            double procte = (total_people - i - 1) * min_money / 100;
            ;
//可支配金额
            double zpje = total_money - procte;
            if (zpje * 100 < max_money) {
                max_money = zpje * 100;
            }
            double result = restRound(min_money, max_money, i, zpje, total_people - 1);
            total_money = total_money - result;
            allresult += result;
            System.out.format("红包  %.2f,余额  %.2f \n", result, total_money);
        }
        System.out.format("总金额%.2f", allresult);
    }

    public static double restRound(double min, double max, int i, double money, int count) {
        double redpac = 0;
        if (i == 19) {
            redpac = money;
        } else {
            redpac = (Math.random() * (max - min) + min) / 100;
        }
        return redpac;
    }
}