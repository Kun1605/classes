package cn.kunakun.controller.wap;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.Depart;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 班集-手机站登陆控制器
 *
 * @Author YangKun
 * @Date 2018/11/18
 */
@Controller
@RequestMapping(value="vue")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    StudentService studentService;
    @Autowired
    DepartService departService;

    @RequestMapping("login")
    @ResponseBody
    public Result login(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
                        Model model, HttpServletResponse response) {
        Result result = new Result();

        Map<String, String> map = null;
        try {
            map = this.studentService.doLogin(username, password);
            String token = map.get("token");
            if (StringUtils.isNotEmpty(token)) {
                result.setMsg("登陆成功");
                result.setCode(200);
                result.setObj(map);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result.setCode(0);
            result.setMsg("登陆失败");
        }
        return result;
    }
}
