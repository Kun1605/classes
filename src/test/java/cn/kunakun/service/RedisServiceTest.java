package cn.kunakun.service;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(RedisServiceTest.class);

	@Autowired
	RedisService redisService;

	@Test
	public void testSet() {
		logger.debug("testSet() - start");

		String set = redisService.set("username", "11");
		System.out.println(set);
		logger.debug("testSet() - end");
	}

	@Test
	public void testGet() {
		logger.debug("testGet() - start");
		redisService.del("hehe");
		String json = redisService.get("TOKEN_+8JFX6PrsGuo2EHkELquSw==");
		String json2 = redisService.get("TOKEN_+8JFX6PrsGuo2EHkELquSw==");
		logger.debug("------------"+json);
		logger.debug("------------"+json2);
		logger.debug("testGet() - end");
	}

}
