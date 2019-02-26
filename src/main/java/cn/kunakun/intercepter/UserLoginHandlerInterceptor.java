package cn.kunakun.intercepter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.kunakun.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

import cn.kunakun.common.Const;
import cn.kunakun.mapper.UserFunctionMapper;
import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.StudentService;
import cn.kunakun.thread.StudentThreadLocal;
import cn.kunakun.utils.CookieUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserLoginHandlerInterceptor.class);

    public static final String LOGIN_URL = "/web/login";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	DepartService departService;
    @Autowired
    private StudentService studentService;

    @Autowired
    UserFunctionMapper userFunctionMapper;
    @Autowired
    RedisService redisService;

	private void setCssJsVtime(HttpServletRequest req){
		req.setAttribute("vtimestamp",Const.TIMESTAMP);
	}
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	setCssJsVtime(request);
        redisService.incr(Const.CLICK);
//        logger.debug("Host->{},X-Real-IP->{},X-Forwarded-For->{},X-Forwarded-Proto->{}",
//                request.getHeader("Host"),
//                request.getHeader("X-Real-IP"),
//                request.getHeader("X-Forwarded-For"),
//                request.getHeader("X-Forwarded-Proto")
//                );
        String ip = request.getHeader("X-Real-IP");
        redisService.sadd(Const.DUP_CLICK, ip == null ? "127.0.0.1" : ip);
        String authListJson = redisService.get("authList");
        if (isEmpty(authListJson)) {
            ArrayList<Object> objects = Lists.newArrayList();
            List<UserFunction> selectAll = userFunctionMapper.selectAll();
            selectAll.parallelStream().forEach(userFun->{
                objects.add(userFun.getUser_id());
            });
            authListJson = OBJECT_MAPPER.writeValueAsString(objects);
            redisService.set("authList", authListJson);
        }
        ArrayList<Long> newArrayList = OBJECT_MAPPER.readValue(authListJson, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Long.class));
        request.setAttribute("authList", newArrayList);

        String token = CookieUtils.getCookieValue(request, Const.COOKIE_NAME);
        if (isEmpty(token)) {
            response.sendRedirect(LOGIN_URL);
            return false;
        }
        Student student= this.studentService.queryUserByToken(token);
        if (null == student) {
            response.sendRedirect(LOGIN_URL);
            return false;
        }
        StudentThreadLocal.set(student); //将student对象放置到本地线程中，方便在Controller和Service中获取
        request.setAttribute("student", student);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        StudentThreadLocal.set(null); //清空本地线程中的User对象数据
    }

}
