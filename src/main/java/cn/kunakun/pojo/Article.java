package cn.kunakun.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;


@Getter
@Setter
@ToString
@Table(name="t_article")
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;// id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date create_time; // 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")


	private Date update_time;// 更新时间
	private String title; // 通知标题
	private String summary; // 摘要
	@Column(name = "click_hit")
	private Integer clickHit; // 查看次数
	@Column(name = "reply_hit")
	private Integer replyHit; // 回复次数
	@Transient
	private String content; // 通知内容
	private String contentNoTag; // 通知内容 无网页标签Lunce分词用
	private Integer type; // 关键字 空格隔开
	@Transient
	private List<String> imageList =new ArrayList<>();
}
