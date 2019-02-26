package cn.kunakun;

import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @auther: YangKun
 * @date: 2018/7/25 15:52
 */
public class Test04 {
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(Test04.class);

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String string = "{\"student\":{\"name\":\"11\"}}";
        String s = jStoJson(string);
        System.out.println(s);
        UserFunction userFunction = objectMapper.readValue(s, UserFunction.class);
        System.out.println(userFunction);


    }
    //{"student":{"name":"杨坤"}}  {"student":{"\name":"yangk"}}
    public void test() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        UserFunction userFunction = new UserFunction();
        Student student = new Student();
        student.setName("杨坤");
        userFunction.setStudent(student);
        String string = objectMapper.writeValueAsString(userFunction);
        System.out.println(string);
    }
    public static String jStoJson(String str) {
        if (!str.contains(".") ){
            return str;
        }
        String substringAfter = StringUtils.substringAfter(str, "{\"");
        String key = StringUtils.substringBefore(substringAfter,".");
        String value = StringUtils.replaceOnce(str, key+".", "");
        String result = "{\"" + key + "\":" + value + "}";
        return jStoJson(result);
    }

}
