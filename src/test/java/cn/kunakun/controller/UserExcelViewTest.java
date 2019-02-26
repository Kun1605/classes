package cn.kunakun.controller;

import cn.kunakun.pojo.Student;
import cn.kunakun.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserExcelViewTest extends  UserExcelView {
    private static final Logger logger = LoggerFactory.getLogger(UserExcelViewTest.class);

    @Autowired
    StudentService studentService;
    @Test
    public void testPoi() {
    }
}