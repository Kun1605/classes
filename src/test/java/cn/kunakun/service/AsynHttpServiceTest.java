package cn.kunakun.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AsynHttpServiceTest {
	
	@Autowired
	AsynHttpService asynHttpService;

	@Test
	public void testDoGet() throws Exception {
		String html = asynHttpService.doGet("http://list.jd.com/list.html?cat=9987,653,655&amp;page=1");
		String s = asynHttpService.doGet("http://www.baidu.com");
		System.out.println(s);
		System.out.println(html);
	}

}
