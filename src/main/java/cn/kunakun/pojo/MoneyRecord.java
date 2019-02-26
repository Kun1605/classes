package cn.kunakun.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 班费流水
 * 
 * @author YangKun 2018年2月1日 上午1:33:12
 * 
 */
@Getter
@Setter
@ToString
@Table(name = "t_money_record")
public class MoneyRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间

	
	
	private Long classId;//班级的id
	private Integer amount;//花费数额
	private String reason; // 花费原因
	
	@Transient
	private Depart depart;// 关联对象


	
	
	
}
