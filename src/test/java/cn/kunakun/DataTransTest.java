package cn.kunakun;

import cn.kunakun.controller.ArticleController;
import cn.kunakun.pojo.Article;
import cn.kunakun.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)

public class DataTransTest {
    private static final Logger logger = LoggerFactory.getLogger(DataTransTest.class);

    @Autowired
    ArticleService articleService;


    @Test
    public void transafor() {
        List<Article> articles = articleService.queryAll();
        for (Article article : articles) {
            logger.debug(article.getId().toString());
            String content = articleService.querymArticleContentById(article.getId());
            logger.debug(content);
            article.setContent(content);
            articleService.update(article);
        }
        logger.debug("跑完数据了OJBK");
    }
}
