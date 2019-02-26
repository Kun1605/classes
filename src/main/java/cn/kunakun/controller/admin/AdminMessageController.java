package cn.kunakun.controller.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.MessageMapper;
import cn.kunakun.pojo.Message;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.MessageService;
import cn.kunakun.service.StudentService;

@Controller
@RequestMapping(value="/admin/message")
public class AdminMessageController {
	private final static Logger logger = LoggerFactory.getLogger(AdminStudentController.class);
	@Autowired
	MessageService messageService;
	@Autowired
	StudentService studentService;
	
	@Autowired
	MessageMapper messageMapper;
	// 列表界面
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/message/list";
	}
	// 添加界面
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		return "admin/message/add";
	}
	
	//------
	// 填充list表格
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public Datagrid datagrid(PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<Message> pageInfo = messageService.queryPageListByLike(pageVO, Message.class);
		List<Message> list = pageInfo.getList();
		
		for (Message message : list) {
			Long studentId = message.getStudentId();
			Student student = studentService.queryById(studentId);
			message.setStudent(student);
		}
		
		datagrid.setRows(pageInfo.getList());
		datagrid.setTotal(pageInfo.getTotal());
		return datagrid;
		
	}
	// 保存全体消息
	@RequestMapping(value="save",method=RequestMethod.POST)
	@ResponseBody
	public Result add(Message message, @RequestParam Long classId) {
		Result result = new Result();

		if (message.getId() == null) {
			Student record = new Student();
			record.setClassId(classId);
			List<Student> studentList = studentService.queryListByWhere(record);
			ArrayList<Message> messageList = Lists.newArrayList();
			studentList.parallelStream().forEach(student->{
				Message message_template = new Message ();
				message_template.setContent("<span style='color:red'>"+message.getContent()+"<span>");
				message_template.setTitle("班级通知->"+student.getName()+":"+message.getTitle());
				message_template.setStudentId(student.getId());
				message_template.setState(1);
				message_template.setCreate_time(new Timestamp(System.currentTimeMillis()));
				message_template.setUpdate_time(new Timestamp(System.currentTimeMillis()));
				
				messageList.add(message_template);
			});
			int insertList = this.messageMapper.insertList(messageList);
			result.setCode(1);
			result.setMsg("保存消息成功");
		}
		return result;

	}
	

}
