package cn.kunakun.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**保存常亮
 * @author 杨坤
 * 2017年9月8日上午10:41:21
 */
public interface Const {
	public static final String COOKIE_NAME = "BJ_TOKEN";
    public static final String REDIRECT_HOME = "/";
	public static final String SESSION_STUDENT= "Student";		//保存用户
	public static final String LAST_PAGE = "LAST_PAGE";		//上一个请求的路径
	// 设置cookie有效期是一个星期，根据需要自定义
	final static Integer cookieMaxAge = 60 * 60 * 24 * 7 * 1;
	// 保存cookie的cookieName
	public static final String COOKIEDOMAINNAME = "www.kunakun.cn";   //自己随便定义
	// 加密cookie时的网站自定码
	public static final String WEBKEY = "kak";  	 	//自己随便定义
	// css时间搓
	public static final String TIMESTAMP = String.valueOf(System.currentTimeMillis());
	
	public static final Integer REDIS_TIME = 60 * 60 * 24 * 7 * 1;
	/***
	 * 七牛AccessKey
	 */
	//public static final String ACCESS_KEY = "VM9ZloBrIZHomgXM2oxmFTVGtKoGQjqFdpbXot6K";
	public static final String ACCESS_KEY = "FVSXjD1YbB0ZhjZZUvAN9_qSy2esgA-qUrVeDX3N";

	/***
	 * 七牛SecretKey
	 */
//	public static final String SECRET_KEY = "-FsdpcG1HzI0VHjef3bJnpSl0NDJUO6p74VNBeMC";
	public static final String SECRET_KEY = "gsQmK8bnR289cEyCzAjWK7FLO69ogeFIp8JwaOth";
	
	public final class QiniuConsts {
		// 图片格式
		public static final CopyOnWriteArraySet<String> VALID_PIC = new CopyOnWriteArraySet<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("jpg");
				add("jpeg");
				add("bmp");
				add("gif");
				add("png");
				add("webp");
				add("zip");
				add("doc");
				add("docx");
				add("docrar");
			}
		};
	}
	 // 允许上传的格式
    public static final List<String> HOME_WORK_EXT=new ArrayList<String>(){
		private static final long serialVersionUID = 1L;

	{
		add("jpg");
		add("jpeg");
		add("bmp");
		add("gif");
		add("png");
		add("webp");
		add("zip");
		add("doc");
		add("docx");
		add("xls");
		
    	add(".rar");//
    	add(".doc");
    	add(".docx");
    	add(".txt");
    }};
	
	/**
	 * 设置redisUser的常量
	 */
	public static final int USER_EXPIRE_SECONDS = 60*60;
	
	// 图片格式2
	public static final CopyOnWriteArraySet<String> UPLOAD_PIC = new CopyOnWriteArraySet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("jpg");
			add("jpeg");
			add("gif");
			add("png");
		}
	};
	public static final String CLICK = "CLICK";
	public static final String DUP_CLICK = "DUP_CLICK";


}
