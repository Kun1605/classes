package cn.kunakun.controller.admin;


import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.pojo.ArticleType;
import cn.kunakun.service.ArticleTypeService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * 班集-文章分类
 * @author YangKun
 * @date 2018年06月09日 11:48
 */
@Controller
public class AdminArticleTypeController {
    private static final Logger logger = LoggerFactory.getLogger(AdminArticleTypeController.class);

    @Autowired
    ArticleTypeService articleTypeService;
    /**
     *
     * @return
     */
    @RequestMapping(value = "admin/articletype/list",method = RequestMethod.GET)
    public String list(){
        return "admin/articletype/list";
    }
    /**
     *
     * @param pageVO
     * @return
     */
    @RequestMapping(value = "admin/articletype/datagrid",method = RequestMethod.GET)
    @ResponseBody
    public Datagrid datagrid(PageVO pageVO){
        Datagrid datagrid = new Datagrid();
        PageInfo<ArticleType> pageInfo = articleTypeService.queryPageListByLike(pageVO, ArticleType.class);
        datagrid.setRows(pageInfo.getList());
        datagrid.setTotal(pageInfo.getTotal());
        return datagrid;
    }

    /**
     * 添加界面
     * @return
     */
    @RequestMapping(value = "/admin/articletype/add" ,method = RequestMethod.GET)
    public String add(){
        return "admin/articletype/add";
    }
    /**
     * 保存
     * @param articleType
     * @return
     */
    @RequestMapping(value = "admin/articletype/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(ArticleType articleType){
        Result result =new Result();
        Integer code = articleTypeService.saveSelective(articleType);
        result.setCode(code);
        result.setMsg("success");
        return result;
    }
    /**
     * 删除
     */
    @RequestMapping(value = "/admin/articletype/del",method = RequestMethod.GET)
    public Result del(String ids){
        Result result = new Result();
        Integer cloumns = articleTypeService.deleteByIds(ArticleType.class, "id", Arrays.asList(ids.split(",")));
        result.setCode(cloumns);
        result.setMsg("ok");
        return result;
    }





}
