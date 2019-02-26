package cn.kunakun.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.kunakun.pojo.ArticleType;
import cn.kunakun.service.ArticleTypeService;
import cn.kunakun.utils.ThreadPoolUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.mongodb.client.MongoDatabase;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.controller.BaseController;
import cn.kunakun.pojo.Article;
import cn.kunakun.service.ArticleService;

import static cn.kunakun.config.RabbitMqConfig.*;

/**
 * 后台文章管理类
 * @author YangKun
 *
 * @date 2018年2月2日
 */
@Controller
@RequestMapping(value="/admin/article")
public class AdminArticleController extends BaseController {
	private static final Logger logger =LoggerFactory.getLogger(AdminArticleController.class);
	@Autowired
	ArticleService articleService;
	@Autowired
	ArticleTypeService articleTypeService;
	@Autowired
    private RabbitTemplate rabbitTemplate;
	@Autowired
	MongoDatabase mongoDatabase;
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(Model model){
		//带上类型
		List<ArticleType> articleTypes = articleTypeService.queryAll();
		model.addAttribute("types",articleTypes);
		return "admin/article/add";
	}
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(){
		return "admin/article/list";
	}
	
	/**
	 * 保存文章
	 * @param article
	 * @return
	 * @date 2018年2月2日下午11:49:53
	 * @auth YangKun
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Result save(Article article){
		Result result =new Result();
		article.setClickHit(0);
		article.setReplyHit(0);
		ThreadPoolUtil.executorThreadPool(()->{
			String htmlNoTag = Jsoup.parse(article.getContent()).text();
			article.setContentNoTag(htmlNoTag);
			if (htmlNoTag.length()>200) {
				article.setSummary(htmlNoTag.replaceAll(" ","").substring(0, 200)+"点击查看更多...");
			}else {
				article.setSummary(htmlNoTag.replaceAll(" ","").substring(0, htmlNoTag.length()));
			}

			Integer code = articleService.saveSelective(article);
			Article newArticle = articleService.replaceImg(article);
			//发消息到rabbitMq
			sendMsg(newArticle.getId(),"insert");
			//插入mongo content
			articleService.insertMongoArticle(newArticle);

		});
		result.setCode(1);
		result.setMsg("添加成功");
		return result;
	}
	/**
	 * 填充数据表格
	 * @param pageVO
	 * @return
	 * @date 2018年2月2日下午11:49:40
	 * @auth YangKun
	 */
	@RequestMapping(value="/datagrid",method=RequestMethod.GET)
	@ResponseBody
	public Datagrid datagrid(PageVO pageVO){
		Datagrid datagrid =new Datagrid();
		PageInfo<Article> pageInfo = articleService.queryPageListByLike(pageVO, Article.class);
		datagrid.setTotal(pageInfo.getTotal());
		datagrid.setRows(pageInfo.getList());
		return datagrid;
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @author YangKun
	 * 2018年1月22日 下午7:50:29
	 */
	@RequestMapping(value="/del",method =RequestMethod.POST)
	@ResponseBody
	public Result  del(String ids){
		Result result =new Result();
		ArrayList<Object> newArrayList = Lists.newArrayList(ids.split(","));
		Integer code = articleService.deleteByIds(Article.class, "id", newArrayList);
		List<String> idList = Splitter.on(",").trimResults().splitToList(ids);
		for (String id : idList) {
			//清除solr的数据
			this.sendMsg(Long.valueOf(id), "delete");
			try {
				//把图片也删了
				articleService.deleteQiNiu(id);
			} catch (Exception e) {
				logger.debug("删除七牛图片出现->{}", e.getMessage());
				logger.error(Throwables.getStackTraceAsString(e));
			}
			//删除掉mongodb
			articleService.delMongoArticle(Long.parseLong(id));

		}
		result.setMsg("删除成功");
		result.setCode(1);
		return result;
		
	}
	private void sendMsg(Long articleId,String type  ){
		try {
			HashMap<Object, Object> hashMap = Maps.newHashMap();
			hashMap.put("id", articleId);
			hashMap.put("type", type);
			hashMap.put("date", System.currentTimeMillis());
			rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY+"."+type, OBJECT_MAPPER.writeValueAsString(hashMap));
		} catch (Exception e) {
		}
	}
	
	
}
