package cn.kunakun.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.kunakun.common.Const;
import cn.kunakun.common.cookie.UserCookieUtil;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.FunctionMenu;
import cn.kunakun.pojo.Student;
import cn.kunakun.utils.Encoder;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	private static final Logger logger =LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private HomeMenuSerivce homeMenuSerivce;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	DepartService departService;

	@Autowired
	ClickService clickService;

	@Autowired
	RedisService redisService;
	
	@RequestMapping(value="/index")
	public String index(Model model,HttpServletRequest request){
	   Student student =  (Student)request.getSession().getAttribute(Const.SESSION_STUDENT); 
	   Long studentId = student.getId();
	   Depart depart = departService.queryById(studentId);
	   student.setDepart(depart);
	   request.getSession(false).setAttribute(Const.SESSION_STUDENT, student);
	   List<Pair<FunctionMenu, List<FunctionMenu>>> menu = homeMenuSerivce.menu(student);
		model.addAttribute("menus", menu);
		return "admin/index";
	}
	@RequestMapping(value="/dashboard" )
	public String dashboard(){
		return "admin/dashboard";
	}
	@RequestMapping(value="/login",method =RequestMethod.GET)
	public String get_login(){
		logger.debug("to Login");
		return "admin/login";
	}
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String post_login( @RequestParam String username ,
			@RequestParam String password ,
			@RequestParam(required=false ,defaultValue="N") String keeplogin ,
			HttpServletRequest request,Model model,
			HttpServletResponse response
			) throws Exception{
		Student student =new Student();
		student.setStudentNo(username);
		Student queryOne = studentService.queryOne(student);
		if (null!=queryOne) {
			String encryptPassword = Encoder.AESEncryptId(password);
			if (queryOne.getPassword().equals(encryptPassword)) {
				//密码相同登陆
				if (StringUtils.isNotEmpty(keeplogin) && keeplogin.equals("Y")) {
					// 记住密码
					UserCookieUtil.saveCookie(queryOne, response);
				}else {
					//没有点击那么清除cookie的密码
					UserCookieUtil.clearCookie(response);
				}
				request.getSession().setAttribute(Const.SESSION_STUDENT, queryOne);
				return "redirect:/admin/index" ;// 跳转至访问页面
			}
		}else {
			logger.info("登陆密码错误");
			model.addAttribute("message", "账号或者密码错误");
		}
		return "redirect:/admin/login";
	}
	@RequestMapping(value="/logout" ,method=RequestMethod.GET)
	public String logout(HttpSession session,HttpServletResponse response){
		session.removeAttribute(Const.SESSION_STUDENT);
		UserCookieUtil.clearCookie(response);
		return "redirect:/admin/login";
	}

	@RequestMapping("/click")
	@ResponseBody
	public Object click() {
		Result result = new Result();
		result.setCode(200);
		List list= clickService.queryClick();
		result.setObj(list);
		result.setMsg("OK");
		HashMap<Object, Object> resultMap = Maps.newHashMap();
		ArrayList<Object> createdata = Lists.newArrayList();
		ArrayList<Object> ipList = Lists.newArrayList();
		ArrayList<Object> clickList = Lists.newArrayList();
		list.stream().forEach(item->{
			Map map = (Map) item;
			createdata.add(map.get("time").toString());
			ipList.add(map.get("dup_click"));
			clickList.add(map.get("click"));
		});

		String click = redisService.get(Const.CLICK);
		click=click == null ? "0" : click;
		String dup_click = redisService.scard(Const.DUP_CLICK);
		dup_click=dup_click == null ? "0" : dup_click;
		clickList.add(click);
		ipList.add(dup_click);
		createdata.add("今天");
		resultMap.put("column", createdata);
		resultMap.put("paydata", ipList);
		resultMap.put("createdata", clickList);
		return resultMap;
	}
}
