package cn.kunakun.controller.admin;

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

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.pojo.HomeWorkReocrd;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.HomeWorkReocrdService;
import cn.kunakun.service.HomeWorkService;
import cn.kunakun.service.StudentService;

/**
 * 班集-提交记录-
 * @author YangKun
 * @date 2018年4月22日下午3:00:07
 */
@Controller
@RequestMapping(value="/admin/homeworkrecord")
public class AdminHomeWorkRecordController {
	private static final Logger logger = LoggerFactory.getLogger(AdminHomeWorkRecordController.class);
	
	@Autowired
	HomeWorkService homeWorkService;
	@Autowired
	HomeWorkReocrdService homeWorkReocrdService;
	@Autowired
	StudentService studentService;
	
	@RequestMapping(value="/list/{work_id}",method=RequestMethod.GET)
	public String list(@PathVariable(value="work_id")Long work_id,Model model ){
		
		model.addAttribute("work_id", work_id);
		return "admin/homeworkrecord/list";
	}
	
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public  Datagrid datagrid(PageVO pageVO,Long work_id){
		Datagrid datagrid =new Datagrid();
		HomeWorkReocrd homeWorkReocrd = new HomeWorkReocrd();
		homeWorkReocrd.setWork_id(work_id);
		
		
		PageInfo<HomeWorkReocrd> pageInfo = homeWorkReocrdService.queryPageListByLike1(pageVO,homeWorkReocrd);
		List<HomeWorkReocrd> list = pageInfo.getList();
		list.stream().forEach(a->{
			Student queryById = studentService.queryById(a.getStudent_id());
			a.setStudent(queryById);
		});
		datagrid.setRows(pageInfo.getList());
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
	}
	
	
}
