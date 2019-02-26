package cn.kunakun.utils;

import cn.kunakun.service.QiniuService;
import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

import static com.google.common.base.Throwables.getStackTraceAsString;

/**
 * 解决图片和base64相互转换的问题...
 *
 * @Author YangKun
 * @Date 2018/11/30
 */
public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 把图片转换成Base64
     *
     * @Author YangKun
     * @Date 2018/11/30
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * @Author YangKun
     * @Date 2018/11/30
     */
    public static InputStream generateImage(String imgStr) {
        try {
            if (imgStr == null)
                return null;
            BASE64Decoder decoder = new BASE64Decoder();
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new ByteArrayInputStream(b);
        } catch (Exception e) {
            logger.debug(getStackTraceAsString(e));
            return null;
        }
    }


}
