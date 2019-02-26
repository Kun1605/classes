package cn.kunakun.mapper;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.kunakun.pojo.Message;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageMapperTest {
	private static Logger logger=LoggerFactory.getLogger(MessageMapperTest.class);
	@Autowired
	MessageMapper messageMapper;

	@Test
	public void test() {
		Message record =new Message();
		record.setStudentId(1L);
		record.setState(3);
		List<Message> selectAvaiMessagePageInfo = messageMapper.selectAvaiMessagePageInfo(record );
		for (Message message : selectAvaiMessagePageInfo) {
			System.out.println(message.toString());
		}
	}

}
