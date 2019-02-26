package cn.kunakun.utils;

import cn.kunakun.common.encode.DeCodeUtil;
import cn.kunakun.common.encode.EncodeUtil;

public class Encoder {
	// ------------------------------业务属性
	private static final String AES_SALT = "MBVL0ggHFwDfT0148d/RuA=="; // 盐
	// AES + IV + SALT

	public static String AESEncryptId(String str) {
		byte[] key = EncodeUtil.decodeBase64(AES_SALT);
		byte[] encryptResult = DeCodeUtil.aesEncrypt(String.valueOf(str).getBytes(), key, key);
		return EncodeUtil.encodeBase64(encryptResult);
	}

	// 解密
	public static String AESDecryptId(String key) {
		byte[] decodeBase64Key = EncodeUtil.decodeBase64(key);
		String decryptResult = DeCodeUtil.aesDecrypt(decodeBase64Key, EncodeUtil.decodeBase64(AES_SALT),
				EncodeUtil.decodeBase64(AES_SALT));
		return decryptResult;
	}
}
