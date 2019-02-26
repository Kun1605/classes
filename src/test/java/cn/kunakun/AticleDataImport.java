package cn.kunakun;

import cn.kunakun.pojo.Article;
import cn.kunakun.pojo.SolrArticle;
import cn.kunakun.service.APIService;
import cn.kunakun.service.ArticleService;
import cn.kunakun.service.AsynHttpService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class AticleDataImport {
	private static final Logger logger = LoggerFactory.getLogger(AticleDataImport.class);

	private HttpSolrServer httpSolrServer;
	@Autowired
	private APIService apiService;
	@Autowired
	private AsynHttpService asynHttpService;
	
	@Autowired
	ArticleService articleService;

	@Autowired
	HttpSolrClient httpSolrClient;
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void testQueryData() throws IOException, SolrServerException {
		logger.debug("-----------开始solrClient");
		SolrQuery solrQuery =new SolrQuery();//构造搜索条件
		StringBuilder queryString = new StringBuilder();
		solrQuery.set("q", "contentNoTag:" + "java", "title:" + "java");
		solrQuery.setStart(1);
		solrQuery.setRows(1);
		solrQuery.setSort("id", SolrQuery.ORDER.desc);

		QueryResponse query = this.httpSolrClient.query(solrQuery);
		List<SolrArticle> list = query.getBeans(SolrArticle.class);
		for (SolrArticle solrArticle : list) {
			logger.debug("solr article->", solrArticle);
		}

	}

	@Before
	public void setUp() {
		logger.debug("setUp() - start");

		String url = "http://127.0.0.1:8983/banji";
		httpSolrServer = new HttpSolrServer(url);
		httpSolrServer.setParser(new XMLResponseParser());// 设置相应格式
		httpSolrServer.setMaxRetries(1);// 设置重置次数.
		httpSolrServer.setConnectionTimeout(500);// 设置链接最长时间

		logger.debug("setUp() - end");
	}

	/**
	 * hehe
	 * @Author: YangKun
	 * @Date: 2018/8/10
	 */
	@Test
	public void testImportData() throws Exception {
		logger.debug("testImportData() - start");

		SolrArticle article = new SolrArticle();
		article.setId(1 + "");
		article.setTitle("呵呵");
		article.setContentNoTag("aaa");
		article.setUpdate_time(new Date().toLocaleString());
		this.httpSolrServer.addBean(article);
		this.httpSolrServer.commit();
		System.out.println("--------------------------------执行完成");

		logger.debug("testImportData() - end");
	}

	/**
	 * 导入数据
	 * 
	 * @throws Exception
	 * @date 2018年2月15日下午8:52:05
	 * @auth YangKun
	 */
	@Test
	public void testImportData2() throws Exception {
		logger.debug("testImportData2() - start");

		// 查询数据
		String url = "http://127.0.0.1/admin/article/datagrid?page={page}&rows=2";
		Integer page = 1;
		Integer pageSize=0;
		do {
			String replace = StringUtils.replace(url, "{page}", "" + page);
//			String content = asynHttpService.doGet(replace);
			AsyncHttpClient asyncHttpClient = asynHttpService.getAsyncHttpClient();
			org.asynchttpclient.RequestBuilder request = Dsl.get(replace);
			// XXX 注意在这里需要带入当前的sessionId
			request.setHeader("Cookie", "JSESSIONID=1272B6B2AA6612DBD26659BFF9A402EC");
			String content = asyncHttpClient.executeRequest(request).toCompletableFuture().get().getResponseBody();
			JsonNode jsonNode = MAPPER.readTree(content);
			String rowsStr = jsonNode.get("rows").toString();
			// 反序列化
			List<SolrArticle>  articles = MAPPER.readValue(rowsStr,
					MAPPER.getTypeFactory().constructCollectionType(List.class, SolrArticle.class));
			pageSize=articles.size();
			this.httpSolrServer.addBeans(articles);
			this.httpSolrServer.commit();
			page += 1;
		} while (pageSize==2);

		logger.debug("testImportData2() - end");
	}
	@Test
	public void testImportData3() throws Exception {
		logger.debug("testImportData3() - start");

		// 查询数据
		Integer page = 1;
		Integer pageSize=0;
		do {
			Article record =new Article();
			List<Article> list = articleService.queryListByWhere(record );
			
			for (Article article : list
					) {
				SolrArticle solrArticle =new SolrArticle();
				solrArticle.setContentNoTag(article.getContentNoTag());
				solrArticle.setId(article.getId()+"");
				solrArticle.setTitle(article.getTitle());
				solrArticle.setUpdate_time(article.getUpdate_time().toLocaleString());
				logger.debug("正在准备入库 solrArticle is {}" ,articleService);;
				this.httpSolrServer.addBean(solrArticle);
				this.httpSolrServer.commit();
			}
			page += 1;
		} while (pageSize==10);

		logger.debug("testImportData3() - end");
	}
	
	

}
