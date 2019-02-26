package cn.kunakun.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CollectionTest {
    private static final Logger logger = LoggerFactory.getLogger(CollectionTest.class);

    /**
     * 如果遍历的集合是有序的，那么是有序的，
     * 如果不是，难么不是
     * @Author Yangkun
     * @Date 18-12-29
     */
    @Test
    public void test1() {

        Collection coll = new ArrayList();
        coll.add("Tody");
        coll.add("is");
        coll.add("Sunday.");

// Output all elements by iterator
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            logger.debug(it.next().toString());
        }
    }

    /**
     * 如果遍历的集合是有序的，那么是有序的，
     * 如果不是，难么不是   Pragma
     * @Author Yangkun
     * @Date 18-12-29
     */
    @Test
    public void test2() {

        Collection coll = new HashSet();
        coll.add("Tody");
        coll.add("is");
        coll.add("Sunday.");
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            logger.debug(it.next().toString());
        }
    }

}
