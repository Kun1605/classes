package cn.kunakun.controller.wap;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.common.pojo.Result;
import cn.kunakun.mapper.ArticleMapper;
import cn.kunakun.pojo.Article;
import cn.kunakun.service.ArticleService;
import com.github.pagehelper.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author YangKun
 * @Date 2018/11/18
 */
@Controller
@RequestMapping("vue")
public class MArticleController {
    private static final Logger logger = LoggerFactory.getLogger(MArticleController.class);

    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleMapper articleMapper;

    //http://localhost:8001/vue/article/list?pageindex=1
    @RequestMapping("/article/list")
    @ResponseBody
    public Result articleList(Integer pageindex,int id) {
        PageVO pageVO = new PageVO();
        if (pageindex != null) {
            pageVO.setPage(pageindex);
        } else {
            pageVO.setPage(0);
        }
        pageVO.setLimit(5);
        Result result =new Result();
        Article tempArticle = new Article();
        tempArticle.setType(id);
        PageInfo<Article> pageInfo = articleService.queryTitleAndTimeByTime(pageVO,tempArticle);
        List<Article> list = pageInfo.getList();
        for (Article article : list) {
            List<String> imageList = article.getImageList();
            String content = article.getContent();
//            String content = articleService.querymArticleContentById(article.getId());
            Document document = Jsoup.parse(content);
            Elements elements = document.select("img");
            for (int i = 0; i < elements.size(); i++) {
                if (i==3) {
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

    @RequestMapping("/article")
    @ResponseBody
    public Article article(Long id) {
        if (id == null
                ) {
            return null;
        }
        Article article = articleService.queryById(id);
       /* article.setContent(articleService.querymArticleContentById(id));*/
        articleMapper.incrClickHit(id);
        return article;

    }
}
