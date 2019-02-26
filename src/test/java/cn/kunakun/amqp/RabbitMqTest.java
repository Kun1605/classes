package cn.kunakun.amqp;

import cn.kunakun.utils.ThreadPoolUtil;
import com.google.common.base.Throwables;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author YangKun
 * @date 2018年3月31日上午10:55:06
 */
public class RabbitMqTest {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqTest.class);
    public static final String TEST_EXCHANGE_NAME = "TEST_EXCHANGE_NAME";
    public static final String FANOUT = "fanout";
    private final String QUEUENAME = "SIMPLE_TEST_QUEUE";
    private final String QUEUENAME2 = "SIMPLE_TEST_QUEUE2";

    private ConnectionFactory connectionFactory;
    Connection connection;
    private String message;

    @Before
    public void before() {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("39.104.171.133");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/banji");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("Yang7728163");
        getConnection();
    }

    private Connection getConnection() {
        try {
            return connection = connectionFactory.newConnection();
        } catch (Exception e) {
            logger.debug(Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * amqp最简单的模式,点对点 ->生产者
     *
     * @Author YangKun
     * @Date 2018/11/30
     */
    @Test
    public void simple() throws Exception {
        Connection connection = getConnection();
        //从管道建立通道
        Channel channel = connection.createChannel();
        //声明简单的队列
        channel.queueDeclare(QUEUENAME, false, false, false, null);
        //消息内容
        for (int i = 1; i <= 100; i++) {
            message = "测试消息" + i;
            channel.basicPublish("", QUEUENAME, null, message.getBytes());
        }
        channel.basicQos(1);
        channel.close();
        connection.close();

        logger.debug("消息测试完成了");

    }

    /**
     * amqp最简单的模式,点对点消费者
     *
     * @Author YangKun
     * @Date 2018/11/30
     */
    @Test
    public void consume() throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //声明一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //消费者绑定队列
        channel.basicConsume(QUEUENAME, true, consumer);
        //
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String s = new String(delivery.getBody());
            logger.debug(s);
        }
    }

    /**
     * work模式,第一个消费者
     *
     * @Author YangKun
     * @Date 2018/11/30
     */
    @Test
    public void consumer_work1() throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();

        //声明一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //消费者绑定队列
        channel.basicConsume(QUEUENAME, false, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String s = new String(delivery.getBody());
            logger.debug("我是第--------一个消费者--------->>>{}", s);
            Thread.sleep(500);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
        }
    }

    /**
     * 第二个消费者  work
     *
     * @Author YangKun
     * @Date 2018/11/30
     */
    @Test
    public void consumer_work2() throws Exception {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //开启能者多劳模式
//        channel.basicQos(1);
        //声明一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //消费者绑定队列
        channel.basicConsume(QUEUENAME, false, consumer);
        //      channel.basicConsume(QUEUENAME, true, consumer); 是不是要开启自动档
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String s = new String(delivery.getBody());
            logger.info("我是第--------二个消费者---------->>>>{}", s);
            Thread.sleep(1);
            //手动提交消费
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    /*----------------------------WORK模式和能者多劳结束----------------------------------------------------*/
    @Test
    public void send() throws Exception {
        String message = "消息";
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //声明交换机的类型
        channel.exchangeDeclare(TEST_EXCHANGE_NAME, FANOUT);
        for (int i = 0; i < 100; i++) {
            channel.basicPublish(TEST_EXCHANGE_NAME, "", null, (message+i).getBytes());
        }
        channel.close();
        connection.close();
        logger.debug("发送者发送消息完成");
    }


}
