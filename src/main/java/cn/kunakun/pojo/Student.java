package cn.kunakun.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YangKun 2018年1月9日 下午2:08:07
 * 
 */
@Getter
@Setter
@ToString
@Table(name = "t_student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date update_time;// 更新时间

    @Length(min = 9, max = 9, message = "学号的长度应该是9位.")
	private String studentNo; // 学号
	
    @Length(min =6 ,max =15 ,message="密码长度应该是6-15;")
    @JsonIgnore
	private String password;// 密码
	private Long classId;// 班集id
	@Length(min=1 ,max=8 ,message="请输入自己的真实姓名" )
	private String name;// 姓名
	
	private String sex;// 性别
	
	@Length(min = 11, max = 11, message = "手机号的长度必须是11位.")
	private String phone;// 手机号码
	
	@Email(message="邮箱的格式不正确")
	private String email;// 邮箱
	
	private String avatar;// 头像
	
	@JSONField(format = "yyyy-MM-dd")
	private Date birth;// 生日
	private String hobbies;// 爱好
	private String fromCity;// 家乡
	private String type;// 类型:1班长 2 团支书 3 班级委员
	private String qq;// QQ号码
	private String wx;// 微信号码
	private String status;// 是否启用
	private String position;// 是否启用
	// -------所属班集
	@Transient
	private Depart depart;
	@Transient
	private Integer dianzan;

}