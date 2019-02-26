package cn.kunakun.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.HomeWorkReocrd;
import cn.kunakun.pojo.Homework;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.HomeWorkReocrdService;
import cn.kunakun.service.HomeWorkService;
import cn.kunakun.service.StudentService;

/**
 * @author YangKun
 * @date 2018年3月21日上午9:57:10
 */
@Controller
public class HomeWorkController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(HomeWorkController.class);

	@Autowired
	HomeWorkService homeWorkService;
	
	@Autowired
	HomeWorkReocrdService homeWorkReocrdService;
	
	@Autowired
	StudentService studentService;
	
	@RequestMapping(value="/web/homework/list")
	public String list(Model model){
		// 带上本班作业
		Long classId = this.getStudent().getClassId();
		Homework record =new Homework();
		record.setClass_id(classId);
		List<Homework> homeworkList = this.homeWorkService.queryListByWhere(record );
		logger.debug("查询出的本班作业列表是  homeworkList 是 {}", homeworkList);
		List<Homework> collect = homeworkList.stream().map(a->{
			if(a.getContent().length()>40){
				a.setContent(a.getContent().substring(0, 40)+"...");
			}
			return a;
		}).collect(Collectors.toList());
		model.addAttribute("homeworkList", collect);
		
		
		return "web/homework/list";
	}
	@RequestMapping(value="/web/homework/detail")
	public String detail(Model model ,@RequestParam Long work_id){
		model.addAttribute("work_id", work_id);
		
		HomeWorkReocrd record =new HomeWorkReocrd();
		record.setWork_id(work_id);
		
		// 带上作业详情
		Homework homework = homeWorkService.queryById(work_id);
		model.addAttribute("work", homework);
		return "web/homework/detail";
	}
	@RequestMapping(value="/web/homework/save")
	@ResponseBody
	public Result detail(HomeWorkReocrd homeWorkReocrd){
		Result result =new Result();
		
		Long student_id = getStudent().getId();
		HomeWorkReocrd record =new HomeWorkReocrd();
		record.setStudent_id(student_id);
		record.setWork_id(homeWorkReocrd.getWork_id());
		HomeWorkReocrd queryOne = homeWorkReocrdService.queryOne(record);
		
		homeWorkReocrd.setId(queryOne.getId());
		homeWorkReocrdService.updateSelective(homeWorkReocrd);
		result.setCode(200);
		result.setObj("提交成功");
		return  result;
	}
	@RequestMapping(value="/web/homework/isSubmit" ,method =RequestMethod.POST)
	@ResponseBody
	public Result detail(@RequestParam Long work_id){
		Result result =new Result();
		HomeWorkReocrd record2 =new HomeWorkReocrd();
        record2.setStudent_id(this.getStudent().getId());
        record2.setWork_id(work_id);
		HomeWorkReocrd queryOne1 = homeWorkReocrdService.queryOne(record2 );
		if (queryOne1!=null) {
			result.setCode(200);
		}else {
			result.setCode(-1);
		}
		return result;
	}
	/*//带上学生
			ArrayList<Object> studentList = Lists.newArrayList();
			List<HomeWorkReocrd> recordList = homeWorkReocrdService.queryListByWhere(record );
			recordList.stream().forEach(a->{
				Long student_id = a.getStudent_id();
				Student queryById = studentService.queryById(student_id);
				studentList.add(queryById);
			});
			model.addAttribute("studentList", studentList);*/
	
	@RequestMapping(value="/web/homework/submitList" ,method =RequestMethod.POST)
	@ResponseBody
	public Result submitList(@RequestParam Long work_id){
		Result result =new Result();
		ArrayList<Object> studentList = Lists.newArrayList();
		HomeWorkReocrd record =new HomeWorkReocrd();
		record.setWork_id(work_id);
		List<HomeWorkReocrd> recordList = homeWorkReocrdService.queryListByWhere(record  );
		recordList.stream().forEach(a->{
			Long student_id = a.getStudent_id();
			Student queryById = studentService.queryById(student_id);
			studentList.add(queryById);
		});
		result.setObj(studentList);
		result.setCode(200);
		return result;
	}

}
