package cn.kunakun.controller.admin;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

import cn.kunakun.common.Const;
import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.controller.BaseController;
import cn.kunakun.pojo.Homework;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.HomeWorkService;

/**
 * 班集-班集作业
 * @author YangKun
 * @date 2018年4月22日下午2:54:27
 */
@Controller
@RequestMapping("/admin/homework")
public class AdminHomeWorkController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(AdminHomeWorkController.class);
	
	@Autowired
	HomeWorkService homeWorkService;
	
	@RequestMapping(value="list")
	public String list(){
//		班级作业列表
		return "admin/homework/list";
	}
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return "admin/homework/add";
	}
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,String ids){
		Homework homework = homeWorkService.queryById(Long.parseLong(ids));
		model.addAttribute("homework", homework);
		return "admin/homework/edit";
	}
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<Homework> pageInfo = homeWorkService.queryPageListByLike(pageVO,Homework.class);
		datagrid.setRows(pageInfo.getList());
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
	}
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Result save(Homework homework,HttpServletRequest request){
		Result result =new Result();
		// 如果没有id,那么是新增的
		if (homework.getId()==null) {
			homework.setCreate_time(new Date());
			Student student = (Student)request.getSession(false).getAttribute(Const.SESSION_STUDENT);
			homework.setClass_id(student.getClassId());
			Integer saveSelective = homeWorkService.saveSelective(homework);
			result.setMsg("新增成功");
			result.setCode(saveSelective);
		}else {
			Integer updateSelective = homeWorkService.updateSelective(homework);
			result.setMsg("更新成功");
			result.setCode(updateSelective);
		}
		return result;
	}
	@RequestMapping(value="/del",method =RequestMethod.POST)
	@ResponseBody
	public Result  del(String ids){
		Result result =new Result();
		ArrayList<Object> newArrayList = Lists.newArrayList(ids.split(","));
		Integer code = homeWorkService.deleteByIds(Homework.class, "id", newArrayList);
		result.setMsg("删除成功");
		result.setCode(code);
		return result;
	}
	

}
