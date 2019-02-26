package cn.kunakun.crawler;

import cn.kunakun.pojo.Article;
import cn.kunakun.service.APIService;
import cn.kunakun.service.AsynHttpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public abstract class BaseCrawler implements Crawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCrawler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    AsynHttpService asynHttpService;

    @Autowired
    APIService apiService;


    /**
     * 开始抓取数据
     */
    public void start() {
        Integer totalPage = getTotalPage();
        // 分页抓取
        for (int i = 1; i <= totalPage; i++) {
            LOGGER.info("当前第{}页，总共{}页。", i, totalPage);
            Collection<Article> items = doStart(i);
            if (items == null) {
                LOGGER.info("抓取到 0 条数据");
                continue;
            }
            LOGGER.info("抓取到{}条数据", items.size());
            for (Object item : items) {
                try {
                    LOGGER.info(item.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public String doGet(String url) throws Exception {
        return this.asynHttpService.doGet(url);
    }


    /**
     * 抓取获取到商品集合
     * 
     * @param page
     * @return
     */
    protected Collection<Article> doStart(Integer page) {
        String url = getPageUrl(page);
        LOGGER.info(" URL is " + url);
        String html = null;
        try {
            html = this.asynHttpService.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (html == null) {
            return null;
        }
        return doParser(html);
    }

    /**
     * 功能描述: 生成对象
     * @auther: YangKun
     * @date: 2018/7/13 16:47
     */
    protected abstract Collection<Article> doParser(String html);

    /**
     * 功能描述:
     * @auther: YangKun
     * @date: 2018/7/13 16:47
     */
    protected abstract String getPageUrl(Integer page);


    /**
     * 功能描述:
     * @auther: YangKun
     * @date: 2018/7/13 16:46
     */
    protected abstract Integer getTotalPage();

    @Override
    public void run() {
        start();
    }

}
