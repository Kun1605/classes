package events;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ThreadTest {

    @Test
    public void test() {
        List<String> titles = new ArrayList<String>();
        titles.add("订单编号");
        titles.add("用户名");
        titles.add("企业名称");
        titles.add("子产品名称");
        titles.add("订单状态");
        titles.add("工号数");
        titles.add("购买年数");
        titles.add("企业全称");
        titles.add("统一社会信用代码");
        titles.add("营业执照注册时间");
        titles.add("营业执照经营范围");
        titles.add("所在地-省");
        titles.add("所在地-市");
        titles.add("注册资金");
        titles.add("法人/经办人姓名");
        titles.add("法人/经办人身份证号");
        titles.add("企业联系人姓名");
        titles.add("联系人手机号");
        titles.add("管理员QQ");
        titles.add("联系人邮箱");
        titles.add("经办人职位");
        titles.add("订单开通时间");
        titles.add("订单结束时间");
        titles.add("签署行业");
        titles.add("业务员");
        titles.add("订单金额");
        System.out.println(titles.size());
    }
}
