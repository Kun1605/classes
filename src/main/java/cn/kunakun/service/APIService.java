package cn.kunakun.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kunakun.common.httpclient.HttpResult;

@Service
public class APIService implements BeanFactoryAware {
	private static final Logger logger = LoggerFactory.getLogger(APIService.class);

	private BeanFactory beanFactory;

	@Autowired(required = false)
	private RequestConfig requestConfig;

	@Autowired
	BasicCookieStore basicCookieStore;

	/**
	 * 根据url获取数据
	 * 
	 * @param url
	 * @return 可以访问...返回content,否则返回null
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @date 2018年2月8日下午4:08:00
	 * @auth YangKun
	 */
	public String doGet(String url) throws ClientProtocolException, IOException {
		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		logger.debug("CloseableHttpResponse response 是 {}", response);

		try {
			// 执行请求
			response = getCloseableHttpClient().execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				return content;

			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return null;
	}

	/**
	 * 带有参数的get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @date 2018年2月8日下午5:03:59
	 * @auth YangKun
	 */
	public String doGet(String url, Map<String, String> params)
			throws ClientProtocolException, IOException, URISyntaxException {
		// 定义请求的参数
		URIBuilder builder = new URIBuilder(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		return this.doGet(builder.build().toString());
	}

	/**
	 * 创建post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @date 2018年2月8日下午5:20:47
	 * @auth YangKun
	 */
	public HttpResult doPost(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
		if (null != params) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// 构造一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = getCloseableHttpClient().execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null == entity) {
				return new HttpResult(response.getStatusLine().getStatusCode(), null);
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}


	/**
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @date 2018年2月8日下午5:23:46
	 * @auth YangKun
	 */
	public HttpResult doPostJson(String url, String json) throws ClientProtocolException, IOException {
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(this.requestConfig);
		if (null != json) {
			StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = this.getCloseableHttpClient().execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null == entity) {
				return new HttpResult(response.getStatusLine().getStatusCode(), null);
			}
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * 没有参数的post请求
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @date 2018年2月8日下午5:24:09
	 * @auth YangKun
	 */
	public HttpResult doPost(String url) throws ClientProtocolException, IOException {
		return this.doPost(url, null);
	}

	public CloseableHttpClient getCloseableHttpClient() {
		return this.beanFactory.getBean(CloseableHttpClient.class);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// 该方法是在Spring容器初始化时会调用该方法，传入beanFactory
		this.beanFactory = beanFactory;
	}
}
