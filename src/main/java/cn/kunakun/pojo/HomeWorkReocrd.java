package cn.kunakun.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author YangKun
 * @date 2018年4月20日下午8:01:53
 */
@Getter
@Setter
@ToString
@Table(name="t_homework_record")
public class HomeWorkReocrd {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间


	private Long work_id;
	private Long student_id;
	private String path;
	@Transient
	private Student student;
}
