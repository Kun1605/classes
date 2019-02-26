package cn.kunakun;

import org.crazycake.jdbcTemplateTool.JdbcTemplateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author YangKun
 * @date 2018年2月7日下午11:34:42
 */
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(value = "cn.kunakun.mapper", markerInterface = Mapper.class)
@EnableTransactionManagement
@ServletComponentScan
@EnableScheduling
public class Application {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();

        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("*");
        config.addAllowedOrigin("http://localhost");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcTemplateTool jdbcTemplateTool() {
        JdbcTemplateTool jdbcTemplateTool = new JdbcTemplateTool();
        jdbcTemplateTool.setJdbcTemplate(jdbcTemplate);
        return jdbcTemplateTool;
    }



    /**
     * 启动入口
     *
     * @param args
     * @date 2018年2月7日下午11:35:03
     * @auth YangKun
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
