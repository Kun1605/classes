package cn.kunakun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.kunakun.utils.Sha1Util;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AynuClassSetApplicationTests {

	@Test
	public void contextLoads() {
		String fg = getFg("E4YX");
		System.out.println("fg is =================="+fg);
	}
	 public String getFg(String validateCode) {
	        StringBuffer codeBuffer = new StringBuffer();
	        codeBuffer.append(validateCode);
	        StringBuffer buff2 = new StringBuffer();
	        buff2.append(Sha1Util.encode(codeBuffer.toString().toUpperCase()).substring(0,30).toUpperCase());
	        buff2.append("10479");
	        String code = Sha1Util.encode(buff2.toString()).substring(0, 30).toUpperCase();
	        return code;
	    }

}
