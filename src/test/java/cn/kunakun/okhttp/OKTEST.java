package cn.kunakun.okhttp;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OKTEST {
    private static final Logger logger = LoggerFactory.getLogger(OKTEST.class);

    @Autowired
    OkHttpClient okHttpClient;

    @Test
    public void get() {
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()){
                logger.error("服务端错误：{}",response);
            }
            //获取响应头
            Headers headers = response.headers();
            System.out.println("======响应头===========");
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i)+":"+headers.value(i));
            }
            System.out.println("响应体："+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
