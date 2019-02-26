import cn.kunakun.utils.StringBuilderHolder;
import com.google.common.base.Splitter;
import com.google.common.net.UrlEscapers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class Test03 {
    public static void main(String[] args) throws InterruptedException, IOException {
       /* List<String> strings = FileUtils.readLines(new File("/home/yk/Desktop/a.txt"),"UTF-8");
        StringBuilder sql = StringBuilderHolder.getGlobal();
        strings.forEach(item->{
          *//*  try {
                item = new String(item.getBytes("GBK"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*//*
            sql.append("add(\""+item+"\");\n");
        });
        System.out.println(sql.toString());*/

        String encode = URLEncoder.encode("{\"stuff\":\"+50ebc10de7b2eaa3953d5ce34b314c027d4c6b72f18f6767defb21066af288ee760bb16a8cb00b6612dee3da17333b132febff0ed51eade2e989dbb64d3b463017fcc3c4694cc2c8da7c15be25ef2f77b393af14c1b4e213427769f5c6b792b8cf847d324569697540be016c959037fe3be74d5a37de442295b26882c4f59006a295e839dd742ae1f1db925009a96876a9d23bfc2322da1c5c82407cd78794ef89ecf3b0becf73e666874e517d7b25a2fcf743a54ebf5c6de74695db01e666f546ce87d8867d52be928b763e76fb3af5172b5142a3ff43f8328f5c57560aaa79b593c2b1b4fca0524eda401460456e1f\",\"sign\":\"6DMfXvMGkT4uQfNNJ+jaeXnZQx8=\",\"time\":1546607516}", "UTF-8");
        System.out.println(encode);
        String decode = URLDecoder.decode(encode, "UTF-8");
        System.out.println(decode);
    }
}