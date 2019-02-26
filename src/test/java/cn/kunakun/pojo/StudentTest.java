package cn.kunakun.pojo;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.kunakun.utils.Encoder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentTest {
	private static final Logger logger = LoggerFactory.getLogger(StudentTest.class);

	@Test
	public void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		String aesEncryptId = Encoder.AESEncryptId("24");
//		String aesDecryptId = Encoder.AESDecryptId(aesEncryptId);
//		logger.debug("加密后是{}", aesEncryptId);
//		logger.debug("解密后是{}", aesDecryptId);

		String str = "{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}";
		Map<String,Object> map = JSONObject.parseObject(str, Map.class);
        Set<Map.Entry<String, Object>> set = map.entrySet();
        List<Map.Entry<String, Object>> collect = set.stream().collect(Collectors.toList());
        collect.forEach(entry->{
            logger.debug("keys is {} ,value is {}",entry.getKey(),entry.getValue());
        });
//		Student student =new Student();
//		student.setName("ly");
//		student.setId(1L);
//		Field field = student.getClass().getDeclaredField("name");
//		field.setAccessible(true);
//		String string = field.get(student).toString();
//		logger.debug("拿到的是{}",string);
		
	}

}
