package cn.kunakun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.DianZan;
import cn.kunakun.service.DianZanService;

/**
 * 点赞表
 * @author YangKun
 * 2018年1月30日 下午11:12:01
 * 
 */
@Controller
@RequestMapping(value="/web/dianzan")
public class DianZanController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DianZanController.class);
	
	@Autowired
	DianZanService dianZanService;
	// 添加进数据库
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result add(DianZan dianZan){
		Result result =new Result();
		DianZan record =new DianZan();
		record.setUserId(dianZan.getUserId());
		record.setStudentId(dianZan.getStudentId());
		DianZan queryOne = dianZanService.queryOne(record );
		if (queryOne==null) {
			dianZan.setState(true);
			Integer code = dianZanService.saveSelective(dianZan);
			result.setCode(code);
		}else {
			dianZan.setId(queryOne.getId());
			dianZan.setState(!queryOne.getState());
			Integer code = dianZanService.updateSelective(dianZan);
			result.setCode(code);
		}
		if (dianZan.getState()) {
			result.setMsg("点赞成功");
		}else {
			result.setMsg("取消赞成功");
		}
		return result;
		
	}
}
