package cn.kunakun.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.Const;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.DianZanMapper;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.DianZan;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.MessageService;
import cn.kunakun.service.RedisService;
import cn.kunakun.service.StudentService;
import cn.kunakun.thread.StudentThreadLocal;
import cn.kunakun.utils.CookieUtils;
import cn.kunakun.utils.Encoder;

/**
 * @author YangKun
 * @date 2018年2月5日下午4:22:48
 */
@RequestMapping(value = "/web")
@Controller
public class WebController extends BaseController {

	@Autowired
	DepartService departService;
	@Autowired
	StudentService studentService;

	@Autowired
	MessageService messageService;

	@Autowired
	DianZanMapper dianZanMapper;
	
	@Autowired
	RedisService redisService;
	@Autowired
	
	private static final Logger logger = LoggerFactory.getLogger(WebController.class);

	@RequestMapping(value = "/home/index")
	public String index( HttpServletRequest request, Model model) {
		// 查询出该同学的班级信息
		Student student = this.getStudent();
		Long classId = student.getClassId();
		Depart depart = departService.queryById(classId);
		student.setDepart(depart);
		DianZan record = new DianZan();
		record.setStudentId(student.getId());
		int dianzan = dianZanMapper.selectCount(record);
		student.setDianzan(dianzan);
		model.addAttribute("student", student);
		logger.debug("该同学所属班集:{}", depart);
		model.addAttribute("depart", depart);
		return "web/home/main_info";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String get_login(Model model) {
		logger.debug("to Login");
//		带上注册的班级号
		List<Depart> departs = departService.queryAll();
		model.addAttribute("departs", departs);
		return "web/login/homeLogin";
	}
		
	  /**
	  * 检测账号是不是可用账号
	 * @param param 1 学号  2手机 3邮箱
	 * @param type 
	 * @return
	 * @date 2018年3月26日下午4:59:07
	 * @auth YangKun
	 */
		@RequestMapping(value = "/web/check/{param}/{type}", method = RequestMethod.GET)
	    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
	            @PathVariable("type") Integer type) {
	        try {
	            Boolean bool = this.studentService.check(param, type);
	            if (bool == null) {
	                // 参数有误
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	            }
	            return ResponseEntity.ok(bool);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }

	/**
	 * 注册
	 * 
	 * @param student
	 * @return
	 * @date 2018年2月7日下午9:49:49
	 * @auth YangKun
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	@ResponseBody
	public Result regist(@Valid Student student, BindingResult bindingResult) {
		Result result = new Result();
		// 如果有错误
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			String message = "";
			String msg_temp;
			for (ObjectError objectError : errors) {
				msg_temp = objectError.getDefaultMessage();
				message=message+msg_temp;
			}
			result.setCode(201);
			result.setMsg(message);
			return result;
		}
		logger.debug("注册");
		student.setStatus("disabled");
		student.setPassword(Encoder.AESEncryptId(student.getPassword()));
		Integer code = studentService.saveSelective(student);
		result.setCode(code);
		result.setMsg("您已经成功向后台发起请求....请等待管理员审核方可成功加入<班集>");
		return result;
	}

	/**
	 * 登陆
	 * @param username
	 * @param password
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 * @throws Exception
	 * @date 2018年2月13日下午10:06:02
	 * @auth YangKun
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {

		try {
		    Map<String, String> map = this.studentService.doLogin(username, password);
		    String token=map.get("token");
		    if (StringUtils.isEmpty(token)) {
		    	model.addAttribute("loginMessage", map.get("msg"));
		    	List<Depart> departs = departService.queryAll();
				model.addAttribute("departs", departs);
		        return "web/login/homeLogin";
		    }
		    
		    // 登录成功，保存token到cookie
		    CookieUtils.setCookie(request, response, Const.COOKIE_NAME, token,Const.cookieMaxAge);
		    return "redirect:/web/home/index";
		
		} catch (Exception e) {
		    e.printStackTrace();
		    // 登录失败
		    List<Depart> departs = departService.queryAll();
			model.addAttribute("departs", departs);
		    return "web/login/homeLogin";
		}
		
	
	
	}

	/**
	 * 登出
	 * @param session
	 * @param response
	 * @return
	 * @date 2018年2月13日下午10:05:56
	 * @auth YangKun
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		redisService.del(Const.COOKIE_NAME+Encoder.AESEncryptId(StudentThreadLocal.get().getStudentNo()));
		CookieUtils.deleteCookie(request, response, Const.COOKIE_NAME);
		return "redirect:/web/login";
	}

}
