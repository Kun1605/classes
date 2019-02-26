package cn.kunakun.service;

import cn.kunakun.pojo.Article;
import cn.kunakun.pojo.ArticleType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章类型service
 * @author YangKun
 * @date 2018年06月08日 17:19
 */
@Service
public class ArticleTypeService extends BaseService<ArticleType> {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleTypeService.class);
    
}
