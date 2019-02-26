package cn.kunakun.controller.admin;

import java.util.ArrayList;
import java.util.Date;
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
import com.google.common.collect.Lists;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.FunctionMenuMapper;
import cn.kunakun.pojo.FunctionMenu;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.FunctionMenuService;
import cn.kunakun.service.StudentService;

/**
 * functionMenu
 * 权限菜单控制器
 * @author YangKun
 * 2018年1月22日 下午6:14:42
 * 
 */
@RequestMapping("/admin/functionMenu")
@Controller
public class AdminFunctionMenuController {
	private final static Logger logger = LoggerFactory.getLogger(AdminFunctionMenuController.class);
	
	@Autowired
	FunctionMenuService functionMenuService ;
	
	@Autowired
	FunctionMenuMapper functionMenuMapper;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/functionMenu/list";
	}
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		
		// 带上所有的1级菜单
		FunctionMenu record=new FunctionMenu();
		record.setOnemenu(1L);
		List<FunctionMenu> list = functionMenuMapper.select(record);
		model.addAttribute("oneMenus", list);
		return "admin/functionMenu/add";
	}
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,Long ids){
		FunctionMenu functionMenu = functionMenuService.queryById(ids);
		// 带上所有的1级菜单
		FunctionMenu record=new FunctionMenu();
		record.setOnemenu(1L);
		List<FunctionMenu> list = functionMenuMapper.select(record);
		model.addAttribute("oneMenus", list);
		model.addAttribute("functionMenu",functionMenu);
		return "admin/functionMenu/edit";
	}
	//---------------------------------------------------
	// 便利
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(@SuppressWarnings("rawtypes") PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo pageInfo = functionMenuService.queryPageListByLike(pageVO, FunctionMenu.class);
		datagrid.setTotal(pageInfo.getTotal());
		datagrid.setRows(pageInfo.getList());
		return datagrid;
	}
	
	//保存
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Result save(FunctionMenu functionMenu){
		Result result =new Result();
		// 如果没有id,那么是新增的
		if (null== functionMenu.getId()) {
			//设置新增时间
			functionMenu.setCreate_time(new Date());
			logger.debug("准备入库functionMenu:{}",functionMenu);
			Integer code = functionMenuService.save(functionMenu);
			logger.debug("入库结果:code:{}",code);
			result.setCode(code);
			result.setMsg("保存成功");
		}else {
			logger.debug("修改functionMenu");
			Integer code = functionMenuService.updateSelective(functionMenu);
			logger.debug("修改结果:code:{}",code);
			result.setCode(code);
			result.setMsg("修改成功");
		}
		return result;
	}
	//删除
	@RequestMapping(value="/del",method =RequestMethod.POST)
	@ResponseBody
	public Result  del(String ids){
		Result result =new Result();
		ArrayList<Object> idList = Lists.newArrayList(ids.split(","));
		logger.debug("删除的id:{}",idList);
		Integer code = functionMenuService.deleteByIds(FunctionMenu.class, "id", idList);
		logger.debug("delete code:{}",code);
		result.setCode(code);
		result.setMsg("删除成功");
		return result;
	}
	
}
