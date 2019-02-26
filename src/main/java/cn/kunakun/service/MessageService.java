package cn.kunakun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.mapper.MessageMapper;
import cn.kunakun.pojo.Message;

@Service
public class MessageService extends BaseService<Message> {
	
	@Autowired
	MessageMapper messageMapper;
	/**
	 * 查询未读消息
	 * @param record
	 * @return
	 * @author YangKun
	 * 2018年1月28日 上午11:09:38
	 */
	public Integer queryunReadCount(Message record) {
		int code = this.mapper.selectCount(record);
		return code;
	}

	/**
	 * 查询出未删除的消息 state!=3的消息
	 * @param pageVO
	 * @return
	 * @author YangKun
	 * 2018年1月28日 上午11:09:26
	 */
	public PageInfo<Message> queryAvaiMessagePageInfo(PageVO pageVO,Message message) {
		PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
		List<Message> list=this.messageMapper.selectAvaiMessagePageInfo(message);
		return new PageInfo<Message>(list);
	}

}
