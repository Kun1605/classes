package cn.kunakun.intercepter;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.kunakun.common.Const;
import cn.kunakun.common.cookie.UserCookieUtil;
import cn.kunakun.common.request.RequestUtil;
import cn.kunakun.mapper.UserFunctionMapper;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.StudentService;


/**
 * 前台拦截器
 * @author 杨坤
 * 2017年9月14日下午8:43:43
 */
public class WebLoginIntecepter implements HandlerInterceptor {
	private final Logger logger = LoggerFactory.getLogger(WebLoginIntecepter.class);
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	UserFunctionMapper userFunctionMapper;
	
	void setCssJsVtime(HttpServletRequest req){
		req.setAttribute("vtimestamp",Const.TIMESTAMP);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse response, Object arg2, Exception ex)
			throws Exception {
//		if(ex!=null){
//			Result ret = new Result();
//			ret.setCode(500);
//			ret.setMsg("系统繁忙，请稍后重试！");
//			logger.error("请求发生异常",ex);
//	        response.setStatus(500);
//			response.setHeader("Cache-Control", "no-cache");
//			response.setHeader("Content-Type", "text/json;charset=UTF-8");
//			response.setCharacterEncoding("UTF-8");
//			response.getWriter().write(JSON.toJSONString(ret));
//		}
		response.sendRedirect("/");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}
	@Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
    	
		
		setCssJsVtime(request);
    	if ("GET".equalsIgnoreCase(request.getMethod())) {
    		// 保存请求页面.....登陆了直接跳转用户请求的页面
    		RequestUtil.saveRequest();
        }
    	
        //log.info("==============执行拦截: preHandle================");  
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		
		//这里对拉入黑名单的ip进行处理【扩展】
		logger.info("来自 >>>>>>"+RequestUtil.getIpAddr(request)+" 请求访问。");	
		
        Student student =  (Student)request.getSession().getAttribute(Const.SESSION_STUDENT); 
        
        // 判断用户是否登录，一般是都没登录的
        if(student == null){
        	// 取cookie值，这里还有其他网站的
        	Cookie[] cookies = request.getCookies();
        	logger.debug("cookies is "+cookies);
        	if(cookies!=null && cookies.length>0){
	  			  String cookieValue = null;
	  			  // 下面是找到本项目的cookie
	  			  for (int i = 0; i < cookies.length; i++) {
	  				  if(Const.COOKIEDOMAINNAME.equals(cookies[i].getName())){
	  					  cookieValue = cookies[i].getValue();
	  					  break;
	  				  }
	  			  }
	  			  // 如果cookieValue为空说明用户上次没有选择“记住下次登录”执行其他
	  			  if(cookieValue==null){
	  				  if (url.contains("login")){
	  					  return true;
	  				  }
	  				 UserCookieUtil.clearCookie(response);
				     response.sendRedirect("/web/login");
		  	          return false;
	  			  }else{
	  				  String cookieValueAfterDecode = new String(Base64.decodeBase64(cookieValue),"UTF-8");
	  				  // 对解码后的值进行分拆,得到一个数组,如果数组长度不为3,就是非法登陆
	  				  String cookieValues[] = cookieValueAfterDecode.split(":");
	  				  if(cookieValues.length!=3){ 
	  					  logger.debug("非法登陆....cookie的长度不是3");
	  					  UserCookieUtil.clearCookie(response);
 						  response.sendRedirect("/web/login");
	  		        	  return false;
	  				  }
	  				  // 判断是否在有效期内,过期就删除Cookie
	  				  long validTimeInCookie = new Long(cookieValues[1]);
	  				  // 如果自定义的有效期小于当前日期那么过期删除
	  				  if (validTimeInCookie < System.currentTimeMillis()) {
	  					  UserCookieUtil.clearCookie(response);
 						  response.sendRedirect("/web/login");
	  		        	  return false;
	  				  }
	  				  // 取出cookie中的用户名,并到数据库中检查这个用户名,
	  				  String studentNo = cookieValues[0];
	  				  Student record =new Student();
	  				  record.setStudentNo(studentNo);
	  				  Student temp = studentService.queryOne(record );
	  				  if(temp!=null){
	  					  // 将结果与Cookie中的MD5码相比较,如果相同,写入Session,自动登陆成功,并继续用户请求
	  					  if (temp.getPassword().equals(cookieValues[2])) {
	  						  request.getSession().setAttribute(Const.SESSION_STUDENT, temp);
	  						  //XXX去首页
	  						  response.sendRedirect("/web/home/index");
	  						  return true;
	  					  }else {
	  						  UserCookieUtil.clearCookie(response);
	  						  response.sendRedirect("/web/login");
							return false;
						}
	  				  }
  			      }
  		     }else{
  		    	if (url.contains("login")){
				    return true;
			    }
  		    	UserCookieUtil.clearCookie(response);
			    response.sendRedirect("/web/login");
  	            return false;
  		    }
        }
        return true;   
    }  
  

}
