package cn.kunakun.controller.admin;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.kunakun.common.Const;
import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.controller.BaseController;
import cn.kunakun.pojo.Depart;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.DepartService;

/**
 * @author YangKun
 *         2018年1月24日 下午1:35:49
 */
@Controller
@RequestMapping(value = "/admin/depart")
public class AdminDepartController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(AdminDepartController.class);

    @Autowired
    DepartService departService;

    // 列表界面
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "admin/depart/list";
    }

    // 编辑界面
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model, @RequestParam Long ids) {
        Depart depart = departService.queryById(ids);
        model.addAttribute("id", depart.getId());
        model.addAttribute("name", depart.getName());
        model.addAttribute("weibo", depart.getWeibo());
        model.addAttribute("logo", depart.getLogo());
        model.addAttribute("balance", depart.getBalance() / 100 + "." + depart.getBalance() % 100);
        model.addAttribute("intro", depart.getIntro());
        return "admin/depart/add";
    }

    // 添加界面
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        return "admin/depart/add";
    }

    //=========
    // 填充list表格
    @RequestMapping(value = "/datagrid", method = RequestMethod.GET)
    @ResponseBody
    public Datagrid datagrid(PageVO pageVO, HttpSession session) {
        Datagrid datagrid = new Datagrid();
        PageInfo<Depart> pageInfo = departService.queryPageListByLike(pageVO, Depart.class);
        Student student = (Student) session.getAttribute(Const.SESSION_STUDENT);
        datagrid.setRows(pageInfo.getList());
        datagrid.setTotal(pageInfo.getTotal());
        return datagrid;
    }

    // 保存
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Depart depart) {
        Result result = new Result();
        if (depart.getId() != null) {
            Integer code = departService.updateSelective(depart);
            result.setCode(code);
        } else {
            depart.setCreate_time(new Date());
            depart.setBalance(depart.getBalance() * 100);
            Integer code = departService.saveSelective(depart);
            result.setCode(code);
        }
        return result;
    }


}
