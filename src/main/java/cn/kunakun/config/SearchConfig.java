package cn.kunakun.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

@Configuration
public class SearchConfig {
	@Value("${solr.url}")
	private String url;
	
	@Bean
	public HttpSolrClient httpSolrServer(){
		HttpSolrClient httpSolrServer = new HttpSolrClient(url);
		httpSolrServer.setParser(new XMLResponseParser());//设置相应格式
		httpSolrServer.setMaxRetries(1);// 设置重置次数.
		httpSolrServer.setConnectionTimeout(500);//设置链接最长时间
		return httpSolrServer;
	}
	@Bean
	public SolrTemplate solrTemplate() {
		return new SolrTemplate(httpSolrServer(),"banji");
	}

}
