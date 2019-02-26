package cn.kunakun.common.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kunakun.common.Const;
import cn.kunakun.pojo.Student;

/**
 * 记录用户信息到Cookie，下次自动登录
 * @author zh
 *
 */
public class UserCookieUtil {
	private static final Logger logger=LoggerFactory.getLogger(UserCookieUtil.class);
	
	// 设置cookie有效期是一个星期，根据需要自定义
	private final static long cookieMaxAge = 60 * 60 * 24 * 7 * 1;
	// 设置cookie的存活率是1年
	private static final int MAXAGE=60 * 60 * 24 * 365 * 1;
	
	/**
	 * 保存Cookie到客户端
	 * 传递进来的user对象中封装了在登陆时填写的用户名与密码
	 * @param user
	 * @param response
	 */
	public static void saveCookie(Student student, HttpServletResponse response) throws Exception {
		// 有效期
		long validTime = System.currentTimeMillis() + (cookieMaxAge * 1000);
		// MD5加密用户详细信息（其实就是把当前用户加密一下，后面判断是否是同一个用户）
		StringBuilder builder = new StringBuilder();
		// 将要被保存的完整的Cookie值
		String cookieValue = student.getStudentNo() + ":" + validTime +":"+student.getPassword();
		logger.debug("将要保存的cookie是{}" ,cookieValue);
		String cookieValueBase64 = new String(Base64.encodeBase64(cookieValue.getBytes("UTF-8")));
		// 开始保存Cookie（cookie是网站名和值）
		Cookie cookie = new Cookie(Const.COOKIEDOMAINNAME, cookieValueBase64);
		cookie.setMaxAge(MAXAGE);
		// cookie有效路径是网站根目录
		cookie.setPath("/");
		// 向客户端写入
		response.addCookie(cookie);
	}

	/**
	 * 用户注销时,清除Cookie
	 * @param response
	 */
	public static void clearCookie(HttpServletResponse response) {
	    //创建一个空cookie添加，覆盖原来的达到清除目的
		Cookie cookie = new Cookie(Const.COOKIEDOMAINNAME, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}

