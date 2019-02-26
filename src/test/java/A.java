import org.joda.time.DateTime;

/**
 * @Author YangKun
 * @Date 2018/10/17
 */
public class A {
    public static void main(String[] args) {
        String string = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        System.out.println(string);

    }
}
