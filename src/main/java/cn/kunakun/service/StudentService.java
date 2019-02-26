package cn.kunakun.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import cn.kunakun.common.Const;
import cn.kunakun.mapper.StudentMapper;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.Student;
import cn.kunakun.utils.Encoder;

/**
 * 学生服务类
 * @author YangKun
 * 2018年1月22日 下午6:17:07
 * 
 */
@Service
public class StudentService extends BaseService<Student>{
	private static final Logger logger=LoggerFactory.getLogger(StudentService.class);
	@Autowired
	StudentMapper studentMapper;
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
	DepartService departService;
	
    private static final ObjectMapper MAPPER = new ObjectMapper();


	/**
	 * 校验参数是否合法
	 * @param param
	 * @param type
	 * @return
	 * @date 2018年2月24日下午2:09:49
	 * @auth YangKun
	 */
	public Boolean check(String param, Integer type) {
		if (type<1 || type>4) {
			return null;
		}
		Student student = new Student();
		switch (type) {
		case 1:
			//StudentNo
			student.setStudentNo(param);
			break;
		case 2:
			//手机
			student.setPhone(param);
			break;
		case 3:
			// 邮箱
			student.setEmail(param);
			break;
		default:
			break;
		}
		Student selectOne = studentMapper.selectOne(student);
		return null==selectOne;
	}
	
	 /**
	  * 根据token查询
	 * @param token
	 * @return
	 * @date 2018年3月14日上午10:15:21
	 * @auth YangKun
	 */
	public Student queryUserByToken(String token) {
	        String key = Const.COOKIE_NAME + token;
	        String jsonData = this.redisService.get(key);
	        if (StringUtils.isEmpty(jsonData)) {
	            // 登录超时
	            return null;
	        }
	        // 重新设置Redis中的生存时间
	        this.redisService.expire(key, Const.REDIS_TIME);

	        try {
	            return MAPPER.readValue(jsonData, Student.class);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	/**
	 * 登陆逻辑
	 * @param username
	 * @param password
	 * @return
	 * @throws JsonProcessingException 
	 * @date 2018年3月14日上午10:24:24
	 * @auth YangKun
	 */
	public Map<String, String> doLogin(String username, String password) throws JsonProcessingException {
		HashMap<String, String> newHashMap = Maps.newHashMap();
		Student student = new Student();
		student.setStudentNo(username);
		Student selectOne = studentMapper.selectOne(student);
		if (selectOne == null) {
			 // 用户不存在
			newHashMap.put("msg", "用户名不存在");
            return newHashMap;
		}
	    if (!StringUtils.equals(Encoder.AESEncryptId(password), selectOne.getPassword())) {
	        // 密码错误
	    	newHashMap.put("msg", "密码错误");
	        return newHashMap;
        }
	    if ( selectOne.getStatus().equals("disabled")) {
	    	newHashMap.put("msg", "未通过审核.请耐心等候");
	    	return newHashMap;
		}
	    // 登录成功，将用户的信息保存到Redis中
	    Depart depart = departService.queryById(selectOne.getClassId());
		selectOne.setDepart(depart);
        String token = Encoder.AESEncryptId(username/* + System.currentTimeMillis()*/);
        this.redisService.set(Const.COOKIE_NAME + token, MAPPER.writeValueAsString(selectOne), Const.REDIS_TIME);
        newHashMap.put("token", token);
		newHashMap.put("username", selectOne.getName());
		newHashMap.put("avatar", selectOne.getAvatar());
		return newHashMap;
	}

}
