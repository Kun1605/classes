package cn.kunakun.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import cn.kunakun.utils.StringBuilderHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.pojo.Datagrid;
import cn.kunakun.pojo.SolrArticle;

/**
 * 搜索控制层
 *
 * @author YangKun
 * @date 2018年2月14日下午8:47:06
 */
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final Integer rows = 3;

    @Autowired
    HttpSolrClient httpSolrClient;


    @RequestMapping(value = "/web/search", method = RequestMethod.GET)
    @ResponseBody
    public Datagrid search(Model model, @RequestParam(value = "q") String keyWords,
                           @RequestParam(value = "page") Integer page) throws SolrServerException, IOException {
        //	keyWords = new String(keyWords.getBytes("iso8859-1"),"UTF-8");
        SolrQuery solrQuery = new SolrQuery();//构造搜索条件
        StringBuilder queryString = StringBuilderHolder.getGlobal();
        queryString.append("title:").append(keyWords).append(" OR " +
                "");
        queryString.append("contentNoTag:").append(keyWords);
        logger.debug("查询的条件是 -> {}", queryString.toString());
//        solrQuery.setQuery(queryString.toString());
        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(rows);
        solrQuery.setSort("id", SolrQuery.ORDER.desc);
        solrQuery.setParam("q", queryString.toString());

        // 是否开启高亮显示
        boolean isHighLight = !StringUtils.equals("*", keyWords) && StringUtils.isNotEmpty(keyWords);
        if (isHighLight) {
            //设置高亮
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("title");
            solrQuery.addHighlightField("contentNoTag");
            solrQuery.setHighlightSimplePre("<em>");
            solrQuery.setHighlightSimplePost("</em>");
            solrQuery.setHighlightFragsize(200);
            solrQuery.setHighlightSnippets(3);


        }
        QueryResponse queryResponse = this.httpSolrClient.query(solrQuery);
        List<SolrArticle> list = queryResponse.getBeans(SolrArticle.class);
        if (isHighLight) {
            Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
                for (SolrArticle solrArticle : list) {
                    if (!entry.getKey().equals(solrArticle.getId().toString())) {
                        continue;
                    }
                    if (entry.getValue().containsKey("title")) {
                        solrArticle.setTitle(StringUtils.join(entry.getValue().get("title"), ""));
                    }
                    if (entry.getValue().containsKey("contentNoTag")) {
                        solrArticle.setContentNoTag(StringUtils.join(entry.getValue().get("contentNoTag"), ""));
                    }
                }
            }
        }
        Datagrid datagrid = new Datagrid();
        datagrid.setTotal(queryResponse.getResults().getNumFound());
        datagrid.setRows(list);
        return datagrid;
    }

}
