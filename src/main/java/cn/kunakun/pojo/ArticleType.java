package cn.kunakun.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 文章类型
 * @author YangKun
 * @date 2018年06月08日 17:16
 */
@Table(name = "t_article_type")
@Data
public class ArticleType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;// 更新时间
    private String name;

}
