package cn.kunakun.controller;

import static org.springframework.util.StringUtils.getFilenameExtension;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.kunakun.common.number.IdGenerator;
import cn.kunakun.service.QiniuService;

/**
 * 上传七牛云Controller
 * @author YangKun
 * 2018年1月25日 上午12:21:54
 * 
 */
@Controller
@RequestMapping("/qiniu")
public class QiNiuController {
	private static Logger logger = LoggerFactory.getLogger(QiNiuController.class);
	
	@Autowired
	QiniuService qiniuService;
	/**
	 * 上传骑牛
	 * @param file input 的name 必须是file
	 * @author YangKun
	 * 2018年1月25日 上午12:22:10
	 */
	@RequestMapping(value="upload")
	@ResponseBody
	public Map<String, Object> upload(@RequestParam MultipartFile file){
		String id = IdGenerator.uuid2();
		String myFileName = file.getOriginalFilename();
		String extName = getFilenameExtension(myFileName);
		Map<String, Object> map = qiniuService.uploadQiniu(1, file, id, extName);
		logger.debug("upload image result:{}",JSON.toJSON(map));
		return map;
	}
	/**
	 * fastAdmin上传返回格式
	 * @param file
	 * @param request
	 * @return
	 * @date 2018年4月22日下午6:49:47
	 * @auth YangKun
	 */
	@RequestMapping(value="localupload")
	@ResponseBody
	public Map<String, Object> localupload(@RequestParam MultipartFile file,HttpServletRequest request){
		
		HashMap<String, Object> newHashMap = Maps.newHashMap();
		String id = IdGenerator.uuid2();
		String myFileName = file.getOriginalFilename();
		String extName = getFilenameExtension(myFileName);
		Map<String, Object> map = qiniuService.uploadQiniu(1, file, id, extName);
		newHashMap.put("code", 1);
		Data data = new Data();
		data.url=map.get("qiniu_path").toString();
		newHashMap.put("data", data);
		return newHashMap;
		
	}
	public class Data{
		public String url;
	}

}
