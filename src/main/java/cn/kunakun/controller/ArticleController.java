package cn.kunakun.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.kunakun.mapper.ArticleMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.kunakun.common.Const;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.UserFunctionMapper;
import cn.kunakun.pojo.Article;
import cn.kunakun.pojo.ArticleType;
import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import cn.kunakun.service.ArticleService;
import cn.kunakun.service.ArticleTypeService;
import cn.kunakun.service.StudentService;
import cn.kunakun.utils.CookieUtils;

@Controller
@RequestMapping(value="")
public class ArticleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired
	ArticleService articleService;
	@Autowired
	UserFunctionMapper userFunctionMapper;
	@Autowired
	StudentService studentService;

	@Autowired
	ArticleMapper articleMapper;
	@RequestMapping(value="/web/article/list")
	public String list(Model model ,HttpServletRequest request){
		List<UserFunction> selectAll = userFunctionMapper.selectAll();
		ArrayList<Long> newArrayList = Lists.newArrayList();
		selectAll.parallelStream().forEach(userFun->{
			newArrayList.add(userFun.getUser_id());
		});
		request.setAttribute("authList", newArrayList);
		String token = CookieUtils.getCookieValue(request, Const.COOKIE_NAME);
        Student student= this.studentService.queryUserByToken(token);
        if (null == student) {
        }
        model.addAttribute("student", student);
		//带上类型
		List<ArticleType> articleTypes = articleTypeService.queryAll();
		model.addAttribute("types",articleTypes);
		return "web/articleInfo/corpus_list";
	}
	// 
	@RequestMapping(value="/web/article/datagrid",method=RequestMethod.POST)
	@ResponseBody
	public Result datagrid(PageVO pageVO,Integer type ){
		Result result =new Result();
		Article article2 = new Article();
		if (type !=null && type !=0) {
			article2.setType(type);
		}
		PageInfo<Article> pageInfo = articleService.queryTitleAndTimeByTime(pageVO,article2);
		List<Article> list = pageInfo.getList();
		for (Article article : list) {
			List<String> imageList = article.getImageList();
//			String content = article.getContent();
			String content = articleService.querymArticleContentById(article.getId());
			Document document = Jsoup.parse(content);
			Elements elements = document.select("img");
			for (int i = 0; i < elements.size(); i++) {
				if (i==2) {
					break;
				}
				imageList.add(elements.get(i).attr("src").toString());
			}
		}
		result.setCode(200);
		result.setMsg("success");
		result.setObj(pageInfo);
		return result;
	}
	
	@RequestMapping(value="/web/article/{id}",method=RequestMethod.GET)
	public String select(@PathVariable(value="id") Long id,Model model){
		Article article = articleService.queryById(id);
		article.setContent(articleService.querymArticleContentById(id));
		articleMapper.incrClickHit(id);
		model.addAttribute("article", article);
		return "web/articleInfo/content";
	}
	
}
