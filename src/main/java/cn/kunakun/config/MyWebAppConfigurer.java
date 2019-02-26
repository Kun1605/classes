package cn.kunakun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.kunakun.intercepter.AdminLoginIntercepter;
import cn.kunakun.intercepter.UserLoginHandlerInterceptor;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {
    /**
     * 后端拦截器
     *
     * @return
     * @author YangKun
     * 2018年1月23日 下午11:10:56
     */
    @Bean
    public AdminLoginIntercepter intercept() {
        AdminLoginIntercepter inter = new AdminLoginIntercepter();
        return inter;
    }

    /**
     * 前端拦截器
     *
     * @return
     * @author YangKun
     * 2018年1月23日 下午11:11:04
     */
    @Bean
    public UserLoginHandlerInterceptor intercept2() {
        UserLoginHandlerInterceptor inter = new UserLoginHandlerInterceptor();
        return inter;
    }

    /**
     * 前段跳转页面
     * @Author YangKun
     * @Date 2018/11/22
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/web/home/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    /*
     * 添加拦截器规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //admin
        registry.addInterceptor(intercept()).addPathPatterns("/admin/**")
                // code验证码
                .excludePathPatterns("/admin/code");
        // public
        registry.addInterceptor(intercept2()).addPathPatterns("/web/**").addPathPatterns("/wap/**")
                .excludePathPatterns("/web/regist")
                .excludePathPatterns("/web/article/**")
                .excludePathPatterns("/wap/article/**")
                .excludePathPatterns("/web/login")
                .excludePathPatterns("/wap/login");
        super.addInterceptors(registry);
    }
}