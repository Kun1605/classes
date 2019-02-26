package cn.kunakun.drools;

import cn.kunakun.pojo.Student;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试rules
 *
 * @Author Yangkun
 * @Date 18-12-29
 */
public class Rules {
    private static final Logger logger = LoggerFactory.getLogger(Rules.class);

    public static void main(final String[] args) {
        //先从单例获取到容器
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        execute(kc);
    }

    private static void execute(KieContainer kc) {
        KieSession ksession = kc.newKieSession("RulesKs");

        Student yangkun = new Student();
        yangkun.setName("杨坤");
        yangkun.setSex("男生");

        Student guohao = new Student();
        guohao.setName("郭豪");
        guohao.setSex("男生");

        ksession.insert(yangkun);
        ksession.insert(guohao);
        //触发所有的rules
        ksession.fireAllRules();


    }
}
