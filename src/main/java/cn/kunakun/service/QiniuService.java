package cn.kunakun.service;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Throwables;
import com.qiniu.storage.BucketManager;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import cn.kunakun.common.Const;

import static cn.kunakun.common.Const.ACCESS_KEY;
import static cn.kunakun.common.Const.SECRET_KEY;


/**
 * 上传图片到七牛云
 *
 * @author YangKun
 * 2018年1月25日 上午12:12:48
 */
@Service
public class QiniuService {
    private static Logger logger = LoggerFactory.getLogger(QiniuService.class);


    /**
     * @param file_type 1图片 ,2 视频
     * @param file
     * @param id
     * @param extName
     * @return
     * @author YangKun
     * 2018年1月25日 上午12:15:43
     */
    public Map<String, Object> uploadQiniu(final Integer file_type, final MultipartFile file, final String id, final String extName) {
        Map<String, Object> vResult = Maps.newHashMap();
        try {
            // 同步传输到七牛
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            // 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
            Zone z = Zone.autoZone();
            Configuration c = new Configuration(z);
            // 获取上传token
            String uptoken = "";
            // 设置key
            StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(id).append(".").append(extName);
            String key = keyBuilder.toString();
            String vQiniuPath = "";
            switch (file_type) {
                case 1:
                    // 图片类型
                    uptoken = auth.uploadToken("banjitest", key);
                    vQiniuPath = "http://kunakun.6655.la/";
                    break;
                case 2:
                    break;
                default:
                    break;
            }
            logger.debug("七牛上传S  file_type:{},id:{},extName:{}", file_type, id, extName);
            UploadManager uploadManager = new UploadManager(c);
            Response res = uploadManager.put(file.getBytes(), key, uptoken);
            logger.debug("七牛上传E  file_type:{},id:{},extName:{}", file_type, id, extName);
            logger.debug("七牛响应id:{},extName:{},res:{}", id, extName, res.bodyString());

            if (res.isOK()) {
                DefaultPutRet putRet = JSONObject.parseObject(res.bodyString(), DefaultPutRet.class);
                String extNameTmp = Const.UPLOAD_PIC.contains(extName) ? extName : "jpg";
                vResult.put("code", 200);
                vResult.put("msg", "上传成功");
                vResult.put("hash", putRet.hash);
                vResult.put("key", putRet.key);
                vResult.put("qiniu_path", vQiniuPath + putRet.key + "?imageMogr2/quality/70/format/" + extNameTmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("七牛上传错误");
        }

        return vResult;
    }

    public Map<String, Object> uploadInputStream(InputStream inputStream) {
        Map<String, Object> vResult = Maps.newHashMap();
        try {
            // 同步传输到七牛
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            // 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
            Configuration c = new Configuration(Zone.autoZone());
            // 获取上传token
            String uptoken = "";
            // 设置key
            StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(UUID.randomUUID()).append(".").append("jpg");
            String key = keyBuilder.toString();
            String vQiniuPath = "";
                    uptoken = auth.uploadToken("banjitest", key);
                    vQiniuPath = "http://kunakun.6655.la/";
            UploadManager uploadManager = new UploadManager(c);
            Response res = uploadManager.put(IOUtils.toByteArray(inputStream), key, uptoken);
            if (res.isOK()) {
                DefaultPutRet putRet = JSONObject.parseObject(res.bodyString(), DefaultPutRet.class);
                String extNameTmp = "jpg";
                vResult.put("code", 200);
                vResult.put("msg", "上传成功");
                vResult.put("hash", putRet.hash);
                vResult.put("key", putRet.key);
                vResult.put("qiniu_path", vQiniuPath + putRet.key + "?imageMogr2/quality/70/format/" + extNameTmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("七牛上传错误");
        }

        return vResult;
    }

    public void deleteImage(String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        Configuration config = new Configuration(Zone.autoZone());
        BucketManager bucketMgr = new BucketManager(auth, config);
        //指定需要删除的文件，和文件所在的存储空间
        String bucketName = "banjitest";
        Response delete = null;
        try {
            delete = bucketMgr.delete(bucketName, key);
            logger.debug("删除了 key->{}", key);
            delete.close();
        }catch (Exception e){
            logger.debug("七牛删除失败删除了key->{}", key);
            logger.debug(Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }finally {
            delete.close();
        }
    }
}
