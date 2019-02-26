package cn.kunakun.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Table(name="t_homework")
public class Homework {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间

	private Long class_id;//班
	private String title;//标题
	private String content;//描述
	private String type;//分类
	
}