package cn.kunakun.controller;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kunakun.common.QingGuoConst;
import cn.kunakun.service.APIService;
import cn.kunakun.service.AsynHttpService;
import cn.kunakun.utils.Sha1Util;
import io.netty.handler.codec.http.cookie.Cookie;

/**
 * 跟多功能
 *
 * @author YangKun
 * @date 2018年2月3日下午2:43:45
 */
@Controller
@RequestMapping(value = "/web/other")
public class OtherConroller {
    private static Logger logger = LoggerFactory.getLogger(OtherConroller.class);

    @Autowired
    APIService apiService;

    @Autowired
    AsynHttpService asynHttpService;

    private List<io.netty.handler.codec.http.cookie.Cookie> cookies;

    /**
     * 跳转到other界面
     *
     * @return
     * @date 2018年2月3日下午2:41:54
     * @auth YangKun
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "web/other/list";
    }

    @RequestMapping(value = "/chengji", method = RequestMethod.GET)
    public String chengji() throws Exception {
        RequestBuilder requestBuilder = Dsl.get(QingGuoConst.get_Cookie);
        AsyncHttpClient httpClient = asynHttpService.getAsyncHttpClient();
        // 获取此次登录的cookie  以后的操作都需要这个cookie来维持，否则无法登录
        Response responseBody = asynHttpService.getAsyncHttpClient().executeRequest(requestBuilder).toCompletableFuture().get();
        cookies = responseBody.getCookies();
        logger.debug("获取的cookie是{}", cookies);
        return "web/other/chengji";
    }

    @RequestMapping(value = "/qingguo/code")
    @ResponseBody
    public void qingguoCode(HttpServletResponse response) throws Exception {
        RequestBuilder getCodeBuilder = Dsl.get("http://112.124.54.19/Score/score/getVerCode.action?identity=B7A561993EB57FE0E83B06D362EDF660&schoolIdentity=F94074A9821395402CDAC85171F7E3DF&st=1517629284757");
        Response response2 = asynHttpService.getAsyncHttpClient().executeRequest(getCodeBuilder).toCompletableFuture().get();
        InputStream inputStream = response2.getResponseBodyAsStream();
        cookies = response2.getCookies();
        BufferedImage image = ImageIO.read(inputStream);
        ImageIO.write(image, "JPEG", response.getOutputStream());// 不管输出什么格式图片，此处不需改动
    }

    /**
     * 唉气死了....之前直接查询青果的数据不管了....出现了系统升级还是怎么滴?算了.懒的搞了...现在还是玩  大学课表人家研究好的interface吧...这个也可以
     *
     * @param schoolIdentity
     * @param s_Id
     * @param b_y
     * @param t_m
     * @param identity
     * @param s_n
     * @param p_d
     * @param v_c
     * @return
     * @throws Exception
     * @date 2018年2月9日下午4:57:02
     * @auth YangKun
     */
    @RequestMapping(value = "/qingguo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String qingguo( String schoolIdentity,  String s_Id, String b_y, String t_m,
                           String identity, String s_n,String p_d, String v_c
    ) throws Exception {
        //登陆操作
        RequestBuilder loginBuilder = Dsl.post("http://112.124.54.19/Score/score/importScoreFromSchool.action");
        loginBuilder.addFormParam("schoolIdentity", schoolIdentity);
        loginBuilder.addFormParam("s_Id", s_Id);
        loginBuilder.addFormParam("b_y", b_y);
        loginBuilder.addFormParam("t_m", t_m);
        loginBuilder.addFormParam("identity", identity);
        loginBuilder.addFormParam("s_n", s_n);
        loginBuilder.addFormParam("p_d", p_d);
        loginBuilder.addFormParam("v_c", v_c);
        // 防盗链
        loginBuilder.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
        // 添加cookie
        for (Cookie cookie2 : cookies) {
            loginBuilder.addCookie(cookie2);
        }
        Response loginResponse = asynHttpService.getAsyncHttpClient().executeRequest(loginBuilder).toCompletableFuture().get();
        logger.debug(" response Body is {}", loginResponse.getResponseBody());
        return loginResponse.getResponseBody();
    }


    /**
     * 青果教务系统的加密方式
     *
     * @param username
     * @param password
     * @return
     * @date 2018年2月9日下午5:18:55
     * @auth YangKun
     */
    public String getDs(String username, String password) {
        StringBuffer textString = new StringBuffer();
        textString.append(username);
        textString.append(Sha1Util.encode(password).substring(0, 30).toUpperCase()
                + "10479");
        String pwdString = Sha1Util.encode(textString.toString()).substring(0, 30)
                .toUpperCase();
        return pwdString;
    }

    /**
     * 加密code
     *
     * @param validateCode
     * @return
     * @date 2018年2月9日下午5:19:07
     * @auth YangKun
     */
    public String getFg(String validateCode) {
        StringBuffer codeBuffer = new StringBuffer();
        codeBuffer.append(validateCode);
        StringBuffer buff2 = new StringBuffer();
        buff2.append(Sha1Util.encode(codeBuffer.toString().toUpperCase()).substring(0, 30).toUpperCase());
        buff2.append("10479");
        String code = Sha1Util.encode(buff2.toString()).substring(0, 30).toUpperCase();
        return code;
    }


}
