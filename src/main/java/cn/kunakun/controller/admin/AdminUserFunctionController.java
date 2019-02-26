package cn.kunakun.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.FunctionMenuMapper;
import cn.kunakun.mapper.StudentMapper;
import cn.kunakun.mapper.UserFunctionMapper;
import cn.kunakun.pojo.FunctionMenu;
import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import cn.kunakun.service.FunctionMenuService;
import cn.kunakun.service.StudentService;
import cn.kunakun.service.UserFunctionService;
import tk.mybatis.mapper.entity.Example;

/**
 * userFunction
 * 用户权限控制器
 * @author YangKun
 * 2018年1月23日 下午6:39:43
 * 
 */
@RequestMapping(value="/admin/userFunction")
@Controller
public class AdminUserFunctionController {
	private final static Logger logger = LoggerFactory.getLogger(AdminUserFunctionController.class);
	
	@Autowired
	UserFunctionService userFunctionService;
	@Autowired
	UserFunctionMapper userFunctionMapper;
	
	@Autowired
	StudentService studentService;
	@Autowired
	FunctionMenuService functionMenuService;
	@Autowired
	FunctionMenuMapper functionMenuMapper;
	
	@Autowired
	StudentMapper studentMapper;
	//list
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/userFunction/list";
	}
	//add
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		FunctionMenu record =new FunctionMenu();
		record.setOnemenu(0L);
		List<FunctionMenu> list = functionMenuMapper.select(record );
		model.addAttribute("list", list);
		return "admin/userFunction/add";
	}
	//add
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model){
		return "admin/userFunction/edit";
	}
	//grank 跳转界面
	@RequestMapping(value="/granklist/{id}",method =RequestMethod.GET)
	public String granklist(@PathVariable(value="id") Long id, Model model){
		UserFunction userFunction = userFunctionService.queryById(id);
		//用户权限的id
		ArrayList<Object> idList = Lists.newArrayList(userFunction.funids.split(","));
		Collection<Long> funidList = Collections2.transform(idList, (e)->Long.parseLong(e.toString()));
		model.addAttribute("funidList", funidList);
		// 查询二级菜单
		FunctionMenu record =new FunctionMenu();
		record.setOnemenu(0L);
		List<FunctionMenu> list = functionMenuService.queryListByWhere(record  );
		model.addAttribute("list", list);
		// 用户id
		model.addAttribute("userId", userFunction.getUser_id());
		model.addAttribute("id", userFunction.getId());
		return "admin/userFunction/granklist";
		
	}

	//-----------------------------------------------------
	// 填充数据表格
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(PageVO<UserFunction> pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<UserFunction> pageInfo = userFunctionService.queryUserAuthList(pageVO);
		List<UserFunction> list = pageInfo.getList();

		datagrid.setRows(list);
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
	}
	/**
	 * 用户授权
	 * @return
	 * @author YangKun
	 * 2018年1月23日 下午9:50:06
	 */
	@RequestMapping(value="/grank",method=RequestMethod.POST)
	@ResponseBody
	public Result grank(UserFunction userFunction,String studentNo){
		Result result =new Result();
		Integer code=0;
		if (userFunction.getId()!=null) {
			code = userFunctionService.updateSelective(userFunction);
		}else {
			Student record =new Student();
			record.setStudentNo(studentNo);
			Student student = studentService.queryOne(record);
			if (student!=null) {
				userFunction.setUser_id(student.getId());
				code = userFunctionService.saveSelective(userFunction);
			}else {
				result.setCode(code);
				result.setMsg("没有这个学号,授权失败");
				return result;
			}
		}
		result.setCode(code);
		if (code<=0) {
			result.setMsg("授权失败");
		}
		result.setMsg("授权成功");
		return result;
		
	}
	
	@RequestMapping(value="/del",method =RequestMethod.POST)
	@ResponseBody
	public Result  del(String ids){
		Result result =new Result();
		ArrayList<Object> newArrayList = Lists.newArrayList(ids.split(","));
		Integer code = userFunctionService.deleteByIds(UserFunction.class, "id", newArrayList);
		result.setMsg("删除成功");
		result.setCode(code);
		return result;
		
	}
	
	
	
	
}
