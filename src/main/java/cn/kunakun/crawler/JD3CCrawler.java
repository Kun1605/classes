package cn.kunakun.crawler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kunakun.pojo.Article;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

@Component
public class JD3CCrawler extends BaseCrawler {


    @Override
    protected Collection<Article> doParser(String html) {
        return null;
    }

    @Override
    protected String getPageUrl(Integer page) {
        return null;
    }

    @Override
    protected Integer getTotalPage() {
        return null;
    }
}
