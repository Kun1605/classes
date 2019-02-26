package cn.kunakun.mapper;

import java.util.HashMap;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Maps;

import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.Student;

/**
 * Created by Administrator on 2018/5/4.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class StudentMapperTest {

    @Autowired
    StudentMapper studentMapper;
    @Autowired
    DepartMapper departMapper;

    @Test
    public void queryStudentById() throws Exception {
        Student student1 = new Student();
        student1.setName("杨");
        HashMap<Object, Object> newHashMap = Maps.newHashMap();
        List<Student> maps = studentMapper.queryStudentById(student1);
        maps.forEach(student -> {
            System.out.println(student);
        });


    }

    @Test
    public void test1() {
        Depart depart = departMapper.selectById((long) 1);
        System.out.print(depart);
    }

    @Test
    public void queryAllStudent() {
        Student student = new Student();
        student.setId(1L);
        List<Student> students = studentMapper.queryAllStudent(student);
        System.out.println(students);

    }

    @Test
    public void hehe() {

    }
}