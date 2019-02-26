package cn.kunakun.config;


import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.netty.ssl.DefaultSslEngineFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import cn.kunakun.utils.ThreadPoolUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * @author YangKun
 * @date 2018年2月9日上午1:41:21
 */
@Configuration
public class AsynHttpClientConfig {
	
	@Bean
	public AsyncHttpClientConfig asyncHttpClientConfig() throws Exception{
		SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		return Dsl.config().setConnectTimeout(4000)//
		.setRequestTimeout(5 * 60 * 1000)//
		.setPooledConnectionIdleTimeout(100)
		.setKeepAlive(true)//
		.setSslContext(sslContext)
		.setSslEngineFactory(new DefaultSslEngineFactory())
		.setMaxConnections(3000)// 最大总连接数
		.setMaxConnectionsPerHost(50)// 最大连接数
		.setThreadPoolName("threadPool-BJHttpAsyncClient")
		.setThreadFactory(ThreadPoolUtil.buildThreadFactory("banJi", true))
		.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
		.build();
	}
	@Bean
	@DependsOn(value={"asyncHttpClientConfig"})
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AsyncHttpClient asyncHttpClient() throws Exception{
		return new DefaultAsyncHttpClient(asyncHttpClientConfig());
	}
	
}
