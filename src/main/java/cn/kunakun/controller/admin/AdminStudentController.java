package cn.kunakun.controller.admin;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.StudentService;
import cn.kunakun.utils.Encoder;

/**
 * @author YangKun
 * 2018年1月24日 下午1:35:44
 * 
 */
@Controller
@RequestMapping(value="/admin/student")
public class AdminStudentController {
	private final static Logger logger = LoggerFactory.getLogger(AdminStudentController.class);
	@Autowired
	StudentService studentService;
	
	/**
	 * 跳转界面
	 * @return
	 * @author YangKun
	 * 2018年1月13日 下午9:52:10
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/student/list";
	}
	/**
	 * 添加界面
	 * @return
	 * @author YangKun
	 * 2018年1月22日 下午12:40:29
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return "admin/student/edit";
	}
	/**
	 * 跳转到编辑页面
	 * @return
	 * @author YangKun
	 * 2018年1月22日 上午1:43:02
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(Model model,String ids){
		Student student = studentService.queryById(Long.parseLong(ids));
		model.addAttribute("student", student);
		String password = Encoder.AESDecryptId(student.getPassword());
		model.addAttribute("password", password);
		return "admin/student/edit";
	}
	
	/**
	 * 填充表格数据
	 * @param pageVO
	 * @return
	 * @author YangKun
	 * 2018年1月13日 下午9:52:24
	 * @throws Exception 
	 */
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<Student> pageInfo = studentService.queryPageListByLike(pageVO,Student.class);
		datagrid.setRows(pageInfo.getList());
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
	}
	
	/**
	 * 保存student
	 * @param student
	 * @return
	 * @author YangKun
	 * 2018年1月22日 下午12:32:14
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Result save(Student student){
		Result result =new Result();
		// 如果没有id,那么是新增的
		student.setPassword(Encoder.AESEncryptId(student.getPassword()));
		if (student.getId()==null) {
			student.setCreate_time(new Date());
			Integer saveSelective = studentService.saveSelective(student);
			result.setMsg("新增成功");
			result.setCode(saveSelective);
		}else {
			Integer updateSelective = studentService.updateSelective(student);
			result.setMsg("更新成功");
			result.setCode(updateSelective);
		}
		return result;
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @author YangKun
	 * 2018年1月22日 下午7:50:29
	 */
	@RequestMapping(value="/del",method =RequestMethod.POST)
	@ResponseBody
	public Result  del(String ids){
		Result result =new Result();
		ArrayList<Object> newArrayList = Lists.newArrayList(ids.split(","));
		Integer code = studentService.deleteByIds(Student.class, "id", newArrayList);
		result.setMsg("删除成功");
		result.setCode(code);
		return result;
		
	}

}
