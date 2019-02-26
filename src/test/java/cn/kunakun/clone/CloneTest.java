package cn.kunakun.clone;

import cn.kunakun.pojo.Student;
import org.junit.Test;

/**
 *
 * @Author Yangkun
 * @Date 18-12-29
 */
public class CloneTest {
    @Test
    public void testClone() {
        Student student = new Student();
        student.setAvatar("头像");
    }
}
