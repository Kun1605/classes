package cn.kunakun.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.pojo.Result;

/**
 * @author YangKun 2018年1月9日 下午3:06:44
 * 
 */
@ControllerAdvice
public class MVCExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(MVCExceptionHandler.class);

	/**
	 * 发生异常返回
	 * 
	 * @param req
	 * @param response
	 * @param e
	 * @return
	 * @author YangKun 2018年1月9日 下午3:06:49
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
		logger.error("{}发生异常", req.getRequestURI());
		logger.error("", e);
		Result ret = new Result();
		ret.setMsg("系统繁忙，请稍后重试！" + e);
		response.setStatus(520);
		return ret;
	}
}
