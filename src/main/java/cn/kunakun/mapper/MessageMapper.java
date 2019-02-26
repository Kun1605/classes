package cn.kunakun.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.pojo.Message;

@Repository
public interface MessageMapper extends MyMapper<Message> {
	// 选出可用的消息
	List<Message> selectAvaiMessagePageInfo(Message message);
}
