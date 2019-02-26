package cn.kunakun.controller.upload;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.kunakun.common.Const;
import cn.kunakun.common.UploadResult;
import cn.kunakun.controller.BaseController;
import cn.kunakun.pojo.HomeWorkReocrd;
import cn.kunakun.pojo.Student;
import cn.kunakun.service.HomeWorkReocrdService;
import cn.kunakun.service.HomeWorkService;
import cn.kunakun.service.PropertiesService;

/**
 * 图片上传
 */
@Controller
@RequestMapping("/web/homework/")
public class UploadController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private PropertiesService propertieService;
    
    @Autowired
    HomeWorkReocrdService homeWorkReocrdService;
    
    @Autowired
    HomeWorkService homeWorkService;
    

    /**
     * produces： 指定响应类型
     * 
     * @param uploadFile
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile uploadFile, Long work_id,HttpServletResponse response)
            throws Exception {
    	logger.debug(" home work _id .{}",work_id);
        // 校验文件的格式
        MutableBoolean isLegal = new MutableBoolean(false);
        Const.HOME_WORK_EXT.stream().forEach(type->{
        	if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)) {
                isLegal.setTrue();
            }
        });
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        UploadResult fileUploadResult = new UploadResult();
        // 状态
        fileUploadResult.setError(isLegal.getValue() ? 0 : 1);
        Student student = getStudent();
        // 文件新路径
        String filePath = getFilePath(uploadFile.getOriginalFilename(),work_id,student);
        logger.debug("Pic file upload .[{}] to [{}] .", uploadFile.getOriginalFilename(), filePath);
        // 生成文件的绝对引用地址
        String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, propertieService.REPOSITORY_PATH),
                "\\", "/");
        fileUploadResult.setUrl(propertieService.REPOSITORY_PATH + picUrl);
        File newFile = new File(filePath);
        // 写文件到磁盘
        uploadFile.transferTo(newFile);
		//更新数据库操作
        HomeWorkReocrd record;  
        
        HomeWorkReocrd record2 =new HomeWorkReocrd();
        record2.setStudent_id(student.getId());
        record2.setWork_id(work_id);
		HomeWorkReocrd queryOne1 = homeWorkReocrdService.queryOne(record2 );
        if (queryOne1==null) {
			record = new HomeWorkReocrd();
			record.setStudent_id(student.getId());
			record.setWork_id(work_id);
			record.setPath(filePath);
			homeWorkReocrdService.saveSelective(record);
		}else {
			record = new HomeWorkReocrd();
			record.setId(queryOne1.getId());
			record.setPath(filePath);
			homeWorkReocrdService.updateSelective(record);
			
		}
        //将java对象序列化成json字符串
        return OBJECT_MAPPER.writeValueAsString(fileUploadResult);
    }

    private String getFilePath(String sourceFileName,Long work_id,Student student) {
        String baseFolder = propertieService.REPOSITORY_PATH + File.separator + "homework"+File.separator+work_id;
        Date nowDate = new Date();
        // yyyy/MM/dd
//        String fileFolder = baseFolder + File.separator + new DateTime(nowDate).toString("yyyy")
//                + File.separator + new DateTime(nowDate).toString("MM") + File.separator
//                + new DateTime(nowDate).toString("dd");
        File file = new File(baseFolder);
        if (!file.isDirectory()) {
            // 如果目录不存在，则创建目录
            file.mkdirs();
        }
        String newFileName=student.getStudentNo()+"_"+student.getName()+ "." +StringUtils.substringAfterLast(sourceFileName, ".");
        File file2 = new File(baseFolder + File.separator + newFileName);
		if (!file2.exists()) {
			return baseFolder + File.separator + newFileName;
		}else {
			file2.delete();
			return baseFolder + File.separator + newFileName;
		}
    }
    
    
    @RequestMapping(value = "/download/{work_id}/{student_id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@PathVariable Long work_id,
    		@PathVariable Long student_id,
    		HttpServletResponse response) throws Exception{
    	HomeWorkReocrd record =new HomeWorkReocrd();
    	record.setWork_id(work_id);
    	record.setStudent_id(student_id);
		HomeWorkReocrd queryOne = homeWorkReocrdService.queryOne(record);
		String path = queryOne.getPath();
	    FileSystemResource file = new FileSystemResource(path);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(file.getFilename(), "UTF-8")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));

		
		
    }

}
