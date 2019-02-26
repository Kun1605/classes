package cn.kunakun.service;

import cn.kunakun.service.ClickService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClickServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ClickServiceTest.class);


    @Autowired
    ClickService clickService;

    @Test
    public void test01() {
        List list = clickService.queryClick();
        System.out.println(list);
    }
}
