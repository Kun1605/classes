package cn.kunakun.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 个人消息
 * @author YangKun
 * 2018年1月27日 下午3:19:33
 * 
 */
@Getter
@Setter
@ToString
@Table(name="t_message")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间

	private Long studentId;
	private String title;
	private String content;//json
	private Integer state;//1 已读 2 未读，3 删除
	@Transient
	private Student student;
	
	

}
