package cn.kunakun.controller;

import static cn.kunakun.config.RabbitMqConfig.EXCHANGE_NAME;
import static cn.kunakun.config.RabbitMqConfig.ROUTING_KEY;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoDatabase;

import cn.kunakun.pojo.Article;
import cn.kunakun.service.ArticleService;

@Controller
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @Autowired
    ExecutorService executorService;
    @Autowired
    MongoDatabase mongoDatabase;
    @Autowired
    ArticleService articleService;

    @Autowired
    HttpSolrClient httpSolrServer;

    @Autowired
    RabbitTemplate rabbitTemplate;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @RequestMapping("test")
    public String test101(){

        return "test/test.html";
    }
    @RequestMapping("test1")
    @ResponseBody
    public Object test102(){
        HashMap<Object, Object> map = Maps.newHashMap();
        map.put("aa", "大扎好");
        return map;
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
