package cn.kunakun.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@Table(name = "t_depart")
public class Depart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;// 更新时间

	private String name;// 班名称
    private String logo;// 班级logo
    private Long balance;// 班费余额
    private String weibo; // 班微博
    private String intro; // 班介绍

}