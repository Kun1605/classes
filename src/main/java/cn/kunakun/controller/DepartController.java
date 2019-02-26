package cn.kunakun.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Depart;
import cn.kunakun.service.DepartService;
import cn.kunakun.thread.StudentThreadLocal;

@Controller
@RequestMapping(value="/web/depart")
public class DepartController {
	private static final Logger logger =LoggerFactory.getLogger(WebController.class);
	
	@Autowired
	DepartService departService;
	@RequestMapping("/data")
	@ResponseBody
	public Result datagrid(){
		// 查询出所有的班集
		Result result =new Result();
		List<Depart> list = departService.queryAll();
		result.setCode(200);
		result.setMsg("success");
		result.setObj(list);
		return result;
	}
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(Model model){
		Depart depart = departService.queryById(StudentThreadLocal.get().getClassId());
		model.addAttribute("depart", depart);
		return "web/depart/departHome";
	}
	

	
}
