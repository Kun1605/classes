package cn.kunakun.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.MoneyRecord;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.MoneyRecordService;

/**
 * moneyRecord  班费流水
 * @author YangKun
 * 2018年2月1日 上午1:42:24
 * 
 */
@RequestMapping(value="/admin/moneyRecord")
@Controller
public class AdminMoneyRecordController {
	private final static Logger logger = LoggerFactory.getLogger(AdminDepartController.class);

	@Autowired
	MoneyRecordService moneyRecordService;
	
	
	@Autowired
	DepartService departService;
	// 列表界面
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/moneyRecord/list";
	}
	// 添加界面
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		return "admin/moneyRecord/add";
	}
	
	// 填充list表格
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<MoneyRecord> pageInfo = moneyRecordService.queryPageListByLike(pageVO, MoneyRecord.class);
		// 转过来去查询班级
		List<MoneyRecord> list = pageInfo.getList();
		for (MoneyRecord moneyRecord : list) {
			Long classId = moneyRecord.getClassId();
			Depart depart = departService.queryById(classId);
			moneyRecord.setDepart(depart);
		}
		pageInfo.setList(list);
		datagrid.setRows(pageInfo.getList());
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
	}
	
	/**
	 * 保存班费记录
	 * @return
	 * @author YangKun
	 * 2018年2月1日 上午11:21:52
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Result save(MoneyRecord moneyRecord){
		
		Result result =new Result();
		// 保存到数据库
		Integer code = moneyRecordService.saveSelective(moneyRecord);
		// 然后同步
		Depart depart = departService.queryById(moneyRecord.getClassId());
		Long balance = depart.getBalance();
		Integer amount = moneyRecord.getAmount();
		depart.setBalance(balance+amount);
		Integer updateSelective = departService.updateSelective(depart);
		result.setCode(updateSelective);
		result.setMsg("保存成功");
		
		return result;
		
	}
	
}
