package cn.kunakun.mapper;

import java.util.List;
import java.util.Map;

import cn.kunakun.utils.StringBuilderHolder;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.mapper.provider.ArticleMapperProvider;
import cn.kunakun.pojo.Article;
import cn.kunakun.pojo.Student;

@Repository
public interface ArticleMapper extends MyMapper<Article> {
	public static final String ARTICLE_TABLE = "t_article";
	@SelectProvider(type=ArticleMapperProvider.class ,method ="count")
	List<Student> count(Map<String,Object> params);

    List<Article> queryTitleAndTimeByTime(Article article);

	@Update(value = "UPDATE t_article SET click_hit = click_hit+1 WHERE id = #{id}")
	Integer incrClickHit(Long id);
}
