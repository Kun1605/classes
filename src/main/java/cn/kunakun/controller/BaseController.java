package cn.kunakun.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.kunakun.pojo.Student;
import cn.kunakun.thread.StudentThreadLocal;


public class BaseController {
	public static Logger logger = LoggerFactory.getLogger(BaseController.class);
	public Student getStudent(){
		return StudentThreadLocal.get();
	}
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	public final static ObjectMapper OBJECT_MAPPER =new ObjectMapper();
}
