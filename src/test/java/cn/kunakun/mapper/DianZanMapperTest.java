package cn.kunakun.mapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.kunakun.pojo.DianZan;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DianZanMapperTest {
	@Autowired
	DianZanMapper dianZanMapper;

	@Test
	public void test() {
		DianZan record =new DianZan();
		record.setStudentId(1L);
		record.setState(true);
		int code = dianZanMapper.selectCount(record);
		System.out.println("查询出来的点赞的总数是"+code);
	}

}
