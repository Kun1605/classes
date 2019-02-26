package cn.kunakun.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.kunakun.config.RabbitMqConfig;
import cn.kunakun.pojo.Article;
import cn.kunakun.pojo.SolrArticle;
import cn.kunakun.service.ArticleService;

@Component
@RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
public class ArticleMqHandler {
    private static final Logger logger = LoggerFactory.getLogger(ArticleMqHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    ArticleService articleService;
    @Autowired
    HttpSolrClient httpSolrServer;

    @RabbitHandler
    public void execute(String message) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(message);
            Long articleId = jsonNode.get("id").asLong();
            String type = jsonNode.get("type").asText();
            if (StringUtils.equals("insert", type) || StringUtils.equals("update", type)) {
                Article article = articleService.queryById(articleId);
                SolrArticle solrArticle = new SolrArticle();
                solrArticle.setId(article.getId() + "");
                solrArticle.setTitle(article.getTitle());
                solrArticle.setContentNoTag(article.getContentNoTag());
                solrArticle.setUpdate_time(article.getUpdate_time().toString());
                String content = articleService.querymArticleContentById(articleId);
                Document document = Jsoup.parse(content);
                Elements elements = document.select("img");
                for (int i = 0; i < elements.size(); i++) {
                    if (i == 1) {
                        break;
                    }
                    String image = elements.get(i).attr("src").toString();
                    solrArticle.setImage(image);
                }
                this.httpSolrServer.addBean(solrArticle);
                this.httpSolrServer.commit();
            }
            if (StringUtils.equals("delete", type)) {
                //删除操作
                this.httpSolrServer.deleteById(articleId + "");
                this.httpSolrServer.commit();
                logger.info("从solr库删除完成，ids  is {}", articleId);
            }
        } catch (Exception e) {
            logger.error("删除失败了雾草-》{}", message);
        }
    }
}
