package cn.kunakun.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import cn.kunakun.common.httpclient.IdleConnectionEvictor;

/**
 * @author YangKun
 * @date 2018年2月8日上午10:01:24
 */
@Configuration
public class HttpClientConfig {

	@Value("${http.maxTotal}")
	private Integer maxToatal;

	@Value("${http.defaultMaxPerRoute}")
	private Integer defaultMaxPerRoute;

	@Value("${http.connectTimeout}")
	private Integer connectTimeout;

	@Value("${http.connectionRequestTimeout}")
	private int connectionRequestTimeout;
	
	@Value("${http.socketTimeout}")
	private int socketTimeout;

	/**
	 * 定义链接管理器
	 * 
	 * @return
	 * @date 2018年2月8日上午9:56:11
	 * @auth YangKun
	 */
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		poolingHttpClientConnectionManager.setMaxTotal(maxToatal);
		return poolingHttpClientConnectionManager;
	}
	/**
	 * @return
	 * @date 2018年2月8日下午5:57:11
	 * @auth YangKun
	 */
	@Bean(name="basicCookieStore")
	@Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
	public BasicCookieStore basicCookieStore(){
		return new BasicCookieStore();
	}
	/**
	 * 定义构建器
	 * 
	 * @return
	 * @date 2018年2月8日上午10:04:14
	 * @auth YangKun
	 */
	@Bean
	@DependsOn(value={"poolingHttpClientConnectionManager","basicCookieStore"})
	public HttpClientBuilder httpClientBuilder() {
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setConnectionManager(poolingHttpClientConnectionManager())
				.setDefaultCookieStore(basicCookieStore());
		return httpClientBuilder;
	}

	/**
	 * 获取httpclient对象..并且是多例的
	 * 
	 * @return
	 * @date 2018年2月8日上午11:12:31
	 * @auth YangKun
	 */
	@Bean
	@DependsOn(value = "httpClientBuilder")
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CloseableHttpClient closeableHttpClient() {
		return httpClientBuilder().build();
	}

	/**
	 * 自动释放链接 5秒一次
	 * 
	 * @return
	 * @date 2018年2月8日上午11:15:18
	 * @auth YangKun
	 */
	/*@Bean
	@DependsOn(value = "poolingHttpClientConnectionManager")
	public IdleConnectionEvictor idleConnectionEvictor() {
		return new IdleConnectionEvictor(poolingHttpClientConnectionManager());
	}*/

	/**
	 * 设置HttpClientConfig
	 * 
	 * @return
	 * @date 2018年2月8日下午5:11:13
	 * @auth YangKun
	 */
	@Bean
	public RequestConfig requestConfig() {
		return RequestConfig.custom().setConnectTimeout(connectTimeout)
							  .setConnectionRequestTimeout(connectionRequestTimeout)
							  .setSocketTimeout(socketTimeout)
							  .build();
	}
}
