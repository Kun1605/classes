package cn.kunakun.mapper;

import java.util.List;
import java.util.Map;

import cn.kunakun.pojo.Article;
import cn.kunakun.utils.StringBuilderHolder;
import org.crazycake.jdbcTemplateTool.JdbcTemplateTool;
import org.crazycake.jdbcTemplateTool.exception.NoColumnAnnotationFoundException;
import org.crazycake.jdbcTemplateTool.exception.NoIdAnnotationFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Maps;

import cn.kunakun.pojo.Student;

/**
 * @author YangKun
 * @date 2018年5月3日下午2:28:32
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(ArticleMapperTest.class);


	@Autowired
	ArticleMapper bArticleMapper;

	@Autowired
    JdbcTemplateTool jdbcTemplateTool;

	@Test
	public void test() {
        StringBuilder sql = StringBuilderHolder.getGlobal();
        sql.append(" SELECT ");
        sql.append(" 	s.* , ");
        sql.append(" 	d.logo AS `depart.logo`");
        sql.append(" FROM ");
        sql.append(" 	t_student s, ");
        sql.append(" 	t_depart d ");
        sql.append(" 	WHERE  ");
        sql.append(" 	s.class_id =d.id limit 0,1");
        List<Student> students = jdbcTemplateTool.list(sql.toString(), null, Student.class);
        students.forEach(student -> {
            System.out.println(student.getDepart());
        });

    }

    @Test
    public void test02() throws NoColumnAnnotationFoundException, NoIdAnnotationFoundException {
        bArticleMapper.incrClickHit(87L);
	}

}
