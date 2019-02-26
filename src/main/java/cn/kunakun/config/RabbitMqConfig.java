package cn.kunakun.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitMq 配置
 * @author YangKun
 * @date 2018年3月30日下午5:29:55
 */
@Configuration
public class RabbitMqConfig {
    public final static String QUEUE_NAME = "banji-article-queue";
    public final static String EXCHANGE_NAME = "banji-article-exchange";
    public final static String ROUTING_KEY = "article.#";

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    
    
    /**
     * 1.创建一个连接工厂
     * @return
     * @date 2018年3月29日下午4:35:52
     * @auth YangKun
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.host, this.port);
        connectionFactory.setUsername(this.userName);
        connectionFactory.setPassword(this.password);
        connectionFactory.setVirtualHost(this.virtualHost);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
    /**
     * 创建交换机
     * @return
     * @date 2018年3月31日上午10:26:53
     * @auth YangKun
     */
    @Bean
    public TopicExchange exchange() {
    	return new TopicExchange(EXCHANGE_NAME);
    }
    /**
     * @return
     * @date 2018年3月31日上午10:27:15
     * @auth YangKun
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }
    /**
     * 将队列和exchange绑定,当然这里也可以手动执行绑定,完成绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
    }

    /**
     * 配置监听
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     * @date 2018年3月29日下午4:57:50
     * @auth YangKun
     */
}