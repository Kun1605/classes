package cn.kunakun.pojo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author YangKun
 * 2018年1月23日 下午6:37:57
 * 
 */
@Getter
@Setter
@ToString
public class UserFunction{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间

	public Long user_id;
	public String funids;
	//---------------------------
	@Transient
	public Student student =new Student();
	@Transient
	private List<FunctionMenu> functionMenus;
	
	
	
}
