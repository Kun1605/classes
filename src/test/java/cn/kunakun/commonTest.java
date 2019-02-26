package cn.kunakun;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Range;
import org.junit.Test;

import java.util.ArrayList;

public class commonTest {
    @Test
    public void test() {
        ArrayList<Integer> objects = Lists.newArrayList();
        objects.add(1);
        objects.add(2);
        Range<Integer> integerRange = Range.between(1, 3);
        System.out.println(integerRange);
    }
}
