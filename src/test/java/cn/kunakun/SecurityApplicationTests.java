package cn.kunakun;

import cn.kunakun.test.OrderEvent;
import cn.kunakun.utils.ThreadPoolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {
    @Autowired
    private ApplicationContext context;

    @Test
    public void listener() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setMsg("aa");
        IntStream.range(0, 10).forEach(num->{
            context.publishEvent(orderEvent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }
}