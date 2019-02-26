package cn.kunakun.task;

import cn.kunakun.common.Const;
import cn.kunakun.pojo.Click;
import cn.kunakun.service.ClickService;
import cn.kunakun.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 用来统计每天的网站的访问量和点击次数
 * @Author YangKun
 * @Date 2018/11/1
 */
@Component
public class VisitTask {
    private static final Logger logger = getLogger(VisitTask.class);
    @Autowired
    RedisService redisService;
    @Autowired
    ClickService clickService;

    /**
     * 每天晚上12点,去执行
     * @Author YangKun
     * @Date 2018/11/1
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void run() {
        logger.debug("执行定时扫描任务开始");
        String click = redisService.get(Const.CLICK);
        click=click == null ? "0" : click;
        String dup_click = redisService.scard(Const.DUP_CLICK);
        dup_click=dup_click == null ? "0" : dup_click;
        Click clickObj = new Click();
        clickObj.setClick(Integer.parseInt(click));
        clickObj.setDup_click(Integer.parseInt(dup_click));
        clickService.saveSelective(clickObj);
        //删除redis的key
        redisService.del(Const.CLICK);
        redisService.del(Const.DUP_CLICK);
        logger.debug("执行定时扫描任务结束");

    }
}
