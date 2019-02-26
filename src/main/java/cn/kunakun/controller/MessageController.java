package cn.kunakun.controller;

import java.util.Map;

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
import com.google.common.collect.Maps;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Message;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.MessageService;
import cn.kunakun.service.StudentService;

@Controller
@RequestMapping(value="/web/message")
public class MessageController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	StudentService studentService ;
	@Autowired
	MessageService messageService;
	
	//全部信息
	@RequestMapping(value="/my_message_list" ,method=RequestMethod.GET)
	public String myMessageList(Model model,PageVO pageVO ) {
		Long id = getStudent().getId();
		Message message =new Message();
		message.setStudentId(id);
		message.setState(3);
		PageInfo<Message> pageInfo= messageService.queryAvaiMessagePageInfo(pageVO,message);
		model.addAttribute("page", pageInfo);
		model.addAttribute("list", pageInfo.getList());
		return "web/message/my_message_list";
	}
	//已读消息
	@RequestMapping(value="/read_message_list" ,method=RequestMethod.GET)
	public String readMessageList(Model model,PageVO pageVO ) {
		Long id = getStudent().getId();
		Message message =new Message();
		message.setStudentId(id);
		message.setState(2);
		PageInfo<Message> pageInfo= messageService.queryPageListEQ(pageVO, message);
		model.addAttribute("page", pageInfo);
		model.addAttribute("list", pageInfo.getList());
		return "web/message/read_message_list";
	}
	//未读消息
	@RequestMapping(value="/unread_message_list" ,method=RequestMethod.GET)
	public String unreadMessageList(Model model,PageVO pageVO) {
		Student student = getStudent();
		Long id = student.getId();
		Message message =new Message();
		message.setStudentId(id);
		message.setState(1);
		PageInfo<Message> pageInfo= messageService.queryPageListEQ(pageVO, message);
		model.addAttribute("page", pageInfo);
		model.addAttribute("list", pageInfo.getList());
		model.addAttribute("student", student);
		return "web/message/unread_message_list";
	}
	// 删除信息 把状态置位3
	@RequestMapping(value="/del" ,method=RequestMethod.GET)
	@ResponseBody
	public Result del(@RequestParam Long id){
		Result result =new Result();
		Message record =new Message();
		record.setId(id);
		record.setState(3);
		Integer code = messageService.updateSelective(record );
		result.setCode(code);
		result.setMsg("删除成功");
		return result;
	}
	// 标记已经读取  状态置位2
	@RequestMapping(value="/read" ,method=RequestMethod.GET)
	@ResponseBody
	public Result read(@RequestParam Long id){
		Result result =new Result();
		Message record =new Message();
		record.setId(id);
		record.setState(2);
		Integer code = messageService.updateSelective(record );
		result.setCode(code);
		result.setMsg("标记已读成功");
		return result;
	}
	
	@RequestMapping(value="/unreadmessage" ,method =RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unreadCount(){
		Student student=this.getStudent();
		Long studentId = student.getId();
		Message record =new Message();
		record.setStudentId(studentId);
		record.setState(1);
		Map<String, Object> resultMap = Maps.newHashMap();
		Integer unreadCount=messageService.queryunReadCount(record);
		resultMap.put("unreadCount", unreadCount);
		resultMap.put("code", 200);
		return resultMap;
		
	}
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Result add(Message message){
		Result result =new Result();
		Integer code = messageService.saveSelective(message);
		result.setMsg("发送消息成功");
		result.setCode(code);
		return result;
	}

}
