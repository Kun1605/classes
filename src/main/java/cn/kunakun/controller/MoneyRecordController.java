package cn.kunakun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.MoneyRecord;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.MoneyRecordService;
import cn.kunakun.thread.StudentThreadLocal;

/**
 * 班费 
 * @author YangKun
 * 2018年2月1日 下午2:26:22
 * 
 */
@RequestMapping(value="/web/moneyRecord")
@Controller
public class MoneyRecordController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	DepartService departService;
	
	@Autowired
	MoneyRecordService moneyRecordService;
	
	// 列表界面
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model){
		
		// 查询出班费余额
		Long classId = getStudent().getClassId();
		Depart depart = departService.queryById(classId);
	    model.addAttribute("balance", String.format("%,.2f", depart.getBalance() / 100.0));
		return "web/moneyRecord/list";
		
	}
	// 填充绘制表格
	@RequestMapping(value="/datagrid",method=RequestMethod.POST)
	@ResponseBody
	public  Result datagrid(PageVO pageVO){
		Result result =new Result();
		result.setCode(200);
		
		MoneyRecord t =new MoneyRecord();
		Long classId = StudentThreadLocal.get().getClassId();
		t.setClassId(classId);
		PageInfo<MoneyRecord> pageInfo = moneyRecordService.queryPageListEQ(pageVO, t );
		
		result.setObj(pageInfo);
		return result;
	}
	
}
