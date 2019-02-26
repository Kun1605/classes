package cn.kunakun.mapper;

import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFunctionMapperTest {
    @Autowired
    UserFunctionMapper userFunctionMapper;
    @Test
    public void queryUserAuthList() {
        UserFunction userFunction = new UserFunction();
        Student student = new Student();
        student.setStudentNo("144802063");
        userFunction.setStudent(student);
        List<UserFunction> userFunctions = userFunctionMapper.queryUserAuthList(userFunction);
        System.out.println(userFunctions);
    }
}