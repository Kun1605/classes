package cn.kunakun.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  private ApplicationContext context;

  public void publishOrder() {
//    context.publishEvent(OrderEvent.builder().msg("建立订单").build());
  }
}