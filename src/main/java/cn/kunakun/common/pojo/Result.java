package cn.kunakun.common.pojo;

/**
 * reuslt 返回状态封装类
 * 
 * @author YangKun 2018年1月9日 下午3:17:20
 * 
 */
public class Result implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String msg = "";

	private Object obj = null;

	private Integer code;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
