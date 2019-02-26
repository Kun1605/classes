package cn.kunakun.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import cn.kunakun.common.Const;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.DianZanMapper;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.DianZan;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.DepartService;
import cn.kunakun.service.DianZanService;
import cn.kunakun.service.RedisService;
import cn.kunakun.service.StudentService;
import cn.kunakun.thread.StudentThreadLocal;
import cn.kunakun.utils.Encoder;
import cn.kunakun.utils.PoiExcelExport;

/**
 * 前台学生控制器
 *
 * @author YangKun
 *         2018年1月26日 上午12:13:12
 */
@Controller
@RequestMapping(value = "/web/student")
public class StudentController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    @Autowired
    StudentService studentService;

    @Autowired
    DepartService departService;
    @Autowired
    DianZanService dianZanService;

    @Autowired
    DianZanMapper dianZanMapper;

    @Autowired
    RedisService redisService;

    /**
     * 编辑学生方法
     *
     * @param model
     * @param session
     * @return
     * @date 2018年2月24日下午2:06:04
     * @auth YangKun
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model, HttpSession session) {
        Student student = this.getStudent();
        model.addAttribute("id", student.getId());
        model.addAttribute("avatar", student.getAvatar());
        model.addAttribute("hobbies", student.getHobbies());
        model.addAttribute("birthday", student.getBirth());
        model.addAttribute("email", student.getEmail());
        model.addAttribute("formCity", student.getFromCity());
        model.addAttribute("name", student.getName());
        model.addAttribute("qq", student.getQq());
        model.addAttribute("sex", student.getSex());
        model.addAttribute("wx", student.getWx());
        model.addAttribute("phone", student.getPhone());
        return "web/student/edit";
    }

    /**
     * 保存学生
     *
     * @param model
     * @param request
     * @param session
     * @param student
     * @return
     * @throws JsonProcessingException
     * @date 2018年2月24日下午2:06:13
     * @auth YangKun
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Model model, HttpServletRequest request, HttpSession session, Student student) throws JsonProcessingException {
        Result result = new Result();
        if (student.getId() != null) {
            Integer code = studentService.updateSelective(student);
            Student student2 = studentService.queryById(student.getId());
//			session.setAttribute(Const.SESSION_STUDENT, student2);
            String token = Encoder.AESEncryptId(student2.getStudentNo());
            redisService.set(Const.COOKIE_NAME + token, OBJECT_MAPPER.writeValueAsString(student2));
            result.setCode(code);
            result.setMsg("修改成功");
        } else {
            // 保存新增
            Integer code = studentService.save(student);
            result.setCode(code);
            result.setMsg("保存成功");
        }
        return result;
    }

    /**
     * 跳转到同学圈子
     *
     * @param model
     * @param student
     * @param pageVO
     * @return
     * @date 2018年2月24日下午2:06:22
     * @auth YangKun
     */
    @RequestMapping(value = "/zoneList")
    public String list(Model model, PageVO pageVO) {
        // 查询出所有班
        List<Depart> classList = departService.queryAll();
        // 根据班查出所有学生
        Student student = this.getStudent();
        if (student.getClassId() == null || student.getClassId() == 0) {
            student.setClassId(null);
        }
        PageInfo<Student> pageInfo = studentService.queryPageListEQ(pageVO, student);
        model.addAttribute("studentList", pageInfo.getList());
        model.addAttribute("classList", classList);
        return "web/student/zoneList";
    }

    /**
     * 同学圈子数据
     *
     * @param student
     * @param pageVO
     * @return
     * @date 2018年2月24日下午2:06:30
     * @auth YangKun
     */
    @RequestMapping(value = "/datagrid", method = RequestMethod.POST)
    @ResponseBody
    public Result datagrid(Student student, PageVO pageVO) {
        Result result = new Result();
        // 查询出所有班
        List<Depart> classList = departService.queryAll();
        // 根据班查出所有学生
        if (student.getClassId() == null || student.getClassId() == 0) {
            student.setClassId(null);
        }
        if (StringUtils.isEmpty(student.getSex())) {
            student.setSex(null);
        }
        if (StringUtils.isEmpty(student.getName())) {
            student.setName(null);
        }
        PageInfo<Student> pageInfo = studentService.queryPageListEQ(pageVO, student);
        List<Student> list = pageInfo.getList();
        for (Student student2 : list) {
            Long studentId = student2.getId();
            // 查出点赞的数量
            DianZan record = new DianZan();
            record.setStudentId(studentId);
            record.setState(true);
            int dianzan = dianZanMapper.selectCount(record);
            student2.setDianzan(dianzan);
        }
        pageInfo.setList(list);
        result.setCode(200);
        result.setMsg("success");
        result.setObj(pageInfo);
        return result;
    }

    /**
     * 联系人界面
     *
     * @param model
     * @return
     * @date 2018年3月28日上午10:45:28
     * @auth YangKun
     */
    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public String contactsList(Model model) {
        Student record = new Student();
        record.setClassId(StudentThreadLocal.get().getClassId());
        List<Student> list = studentService.queryListByWhere(record);
        model.addAttribute("list", list);
        return "web/student/contacts";
    }

    @RequestMapping(value = "excel", method = RequestMethod.GET)
    public void excel(HttpServletResponse response) throws IOException {
        Student record = new Student();
        record.setClassId(StudentThreadLocal.get().getClassId());
        List<Student> list = studentService.queryListByWhere(record);

        PoiExcelExport pee = new PoiExcelExport("/", "sheet1");
        String titleColumn[] = {"name", "phone", "email", "wx", "qq"};
        String titleName[] = {"姓名", "手机号", "邮箱", "微信", "QQ"};
        int titleSize[] = {13, 13, 13, 13, 13};
        response.setHeader("Content-Type", "application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("联系方式.xls", "UTF-8"));
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] wirteExcelBytes = pee.wirteExcelBytes(titleColumn, titleName, titleSize, list);
            outputStream.write(wirteExcelBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册校验参数合法
     *
     * @param param
     * @param type
     * @return
     * @date 2018年2月24日下午2:08:58
     * @auth YangKun
     */
    @RequestMapping("/web/student/check/{param}/{type}")
    private ResponseEntity<Boolean> check(@PathVariable("param") String param, @PathVariable("type") Integer type) {
        try {
            Boolean bool = studentService.check(param, type);
            if (null == bool) {
                // 返回400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(bool);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


}
