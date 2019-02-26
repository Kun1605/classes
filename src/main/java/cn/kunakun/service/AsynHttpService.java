package cn.kunakun.service;

import java.io.InputStream;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

@Service
public class AsynHttpService implements BeanFactoryAware {
	
	private BeanFactory beanFactory;
	
	public AsyncHttpClient getAsyncHttpClient(){
		return beanFactory.getBean(AsyncHttpClient.class);
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory=beanFactory;
	}
	
	public String doGet(String url) throws Exception{
		RequestBuilder requestBuilder = Dsl.get(url);
		Response response = getAsyncHttpClient().executeRequest(requestBuilder).toCompletableFuture().get();
		String responseBody = response.getResponseBody();
		return responseBody;
	}

	public String doGet(String url, String encode) throws Exception {
		RequestBuilder requestBuilder = Dsl.get(url);
		requestBuilder.setHeader("charset", encode);
		Response response = getAsyncHttpClient().executeRequest(requestBuilder).toCompletableFuture().get();
		String responseBody = response.getResponseBody();
		return responseBody;
	}
	public InputStream doGetInputStream(String url) throws Exception {
		RequestBuilder requestBuilder = Dsl.get(url);
		return getAsyncHttpClient().executeRequest(requestBuilder).toCompletableFuture().get().getResponseBodyAsStream();
	}
}
