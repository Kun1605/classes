package cn.kunakun.controller;

import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.ArticleTypeMapper;
import cn.kunakun.pojo.ArticleType;
import cn.kunakun.service.ArticleTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 班集-文章类型
 * @author YangKun
 * @date 2018年06月08日 17:22
 */
@Controller
public class ArticleTypeController {
    private final static Logger logger = LoggerFactory.getLogger(ArticleTypeController.class);

    @Autowired
    ArticleTypeService articleTypeService;

    @RequestMapping(value="/web/articletype/datagrid",method = RequestMethod.POST)
    @ResponseBody
    public Result list(){
        Result result = new Result();
        //查出数据库中所有的类型
        List<ArticleType> queryAll = articleTypeService.queryAll();
        result.setObj(queryAll);
        result.setMsg("success");
        result.setCode(200);
        return result;
    }
}
