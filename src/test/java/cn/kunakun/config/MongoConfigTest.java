package cn.kunakun.config;

import cn.kunakun.pojo.Article;
import cn.kunakun.service.ArticleService;
import com.mongodb.client.MongoDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoConfigTest {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfigTest.class);
    @Autowired
    MongoDatabase mongoDatabase;
    @Autowired
    ArticleService articleService;
    @Test
    public void test101(){
        List<Article> articles = articleService.queryAll();
        articles.forEach(article -> {
            articleService.insertMongoArticle(article);
        });
    }

    
}