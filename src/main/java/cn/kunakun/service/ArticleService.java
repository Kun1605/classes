package cn.kunakun.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.utils.ImageUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kunakun.mapper.ArticleMapper;
import cn.kunakun.pojo.Article;
import tk.mybatis.mapper.entity.Example;

@Service
public class ArticleService extends BaseService<Article> {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    public static final String BASE64_PREFIX = "data:image/png;base64,";
    @Autowired
    MongoDatabase mongoDatabase;
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    AsynHttpService httpService;
    @Autowired
    QiniuService qiniuService;

    /**
     * @return
     * @date 2018年6月2日下午2:32:49
     * @auth YangKun
     */
    public PageInfo<Article> wapHomeArticle() {
        PageHelper.startPage(1, 2);
        Example example = new Example(Article.class);
        example.orderBy("create_time").desc();
        List<Article> list = articleMapper.selectByExample(example);
        return new PageInfo<Article>(list);
    }

    public String querymArticleContentById(Long id) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("t_article");
        BasicDBObject bson = new BasicDBObject("id", id);
        FindIterable<Document> find = collection.find(bson);
        for (Document doc : find) {
            return doc.getString("content");
        }
        return null;
    }

    public void insertMongoArticle(Article article) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection("t_article");
            Document doc = new Document();
            doc.put("id", article.getId());
            doc.put("content", article.getContent());
            collection.insertOne(doc);
        } catch (Exception e) {
            logger.error("{}插入失败", article.getId());
        }
        return;
    }

    public void delMongoArticle(Long id) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection("t_article");
            BasicDBObject bson = new BasicDBObject("id", id);
            collection.deleteOne(bson);
        } catch (Exception e) {
            logger.error("{}删除失败", id);
        }
        return;
    }

    public PageInfo<Article> queryTitleAndTimeByTime(PageVO pageVO, Article article) {
        PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
        List<Article> list = articleMapper.queryTitleAndTimeByTime(article);
        return new PageInfo<Article>(list);
    }

    /**
     * 替换图片
     *
     * @Author YangKun
     * @Date 2018/11/1
     */
    public Article replaceImg(Article article) {
        org.jsoup.nodes.Document document = Jsoup.parse(article.getContent());
        //XXX 先删除  cnblogs_code_toolbar   cnblogs_code_toolbar
        document.select(".cnblogs_code_toolbar").remove();
        Elements imgs = document.select("img");
        //判断是不是我的图片
        ArrayList<Pair> newImageSrcList = Lists.newArrayList();
        for (Element img : imgs) {
            String src = img.attr("src");
            //包含我的这个src
            if (StringUtils.contains(src, "kunakun.6655.la")) {
            } else {
                InputStream inputStream;
                //上传到七牛
                try {
                    if (StringUtils.contains(src, BASE64_PREFIX)) {
                        logger.debug("发现base64的正在替换S");
                        inputStream = ImageUtils.generateImage(src.replace(BASE64_PREFIX, ""));
                    } else {
                        inputStream = httpService.doGetInputStream(src);
                    }
                    //上传到七牛云里面
                    Map<String, Object> resultMap = qiniuService.uploadInputStream(inputStream);
                    //得到七牛云返回的url
                    String qiniu_path = resultMap.get("qiniu_path").toString();
                    Pair<String, String> pair = Pair.of(src, qiniu_path);
                    newImageSrcList.add(pair);
                } catch (Exception e) {
                    logger.error(Throwables.getStackTraceAsString(e));
                }
            }
        }
        logger.debug("已经外面图片都替换OK了");
        String content = article.getContent();
        for (Pair item : newImageSrcList) {
            String left = item.getLeft().toString();
            String right = item.getRight().toString();
            content = StringUtils.replaceAll(content, left, right);
        }
        logger.debug("替换全部完成,重新设置content");
        org.jsoup.nodes.Document parse = Jsoup.parse(content);
        parse.select(".cnblogs_code_toolbar").remove();
        logger.debug("删掉博客园的复制代码的那个方块完成");
        article.setContent(parse.toString());

        return article;
    }

    public void deleteQiNiu(String id) {
        String content = this.querymArticleContentById(Long.valueOf(id));
        org.jsoup.nodes.Document document = Jsoup.parse(content);
        Elements imgs = document.select("img");
        for (Element img : imgs) {
            String src = img.attr("src");
            if (src.contains("kunakun.6655.la")) {
                logger.debug("开始删除骑牛图片->{}", src);
                String substring = src.substring(23);
                String key = StringUtils.substringBefore(substring, "?");
                qiniuService.deleteImage(key);
            }
        }

    }

    public static void main(String[] args) {
//        String a = "http://kunakun.6655.la/abaa5540b21f4818a9251063dd4cd38b.jpg?imageMogr2/quality/70/format/jpg";
//        String substring = a.substring(23);
//        String key = StringUtils.substringBefore(substring, "?");
//        System.out.println(key);

        String str = "data:image/png;base64,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";
        InputStream inputStream = ImageUtils.generateImage(str);

    }
}
