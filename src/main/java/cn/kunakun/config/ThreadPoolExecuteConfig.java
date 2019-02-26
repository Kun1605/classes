package cn.kunakun.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolExecuteConfig {

    @Bean
    public ExecutorService threadPoolExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("BanJi-Thread-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(4, 8, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(256),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return pool;
    }

    public static void main(String[] args) {

    }
}
