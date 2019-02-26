package cn.kunakun.test;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
  @EventListener
  public void handleOrderEvent(OrderEvent event) {
    System.out.println("我监听到了handleOrderEvent发布的message为:" + event.getMsg());
  }
}