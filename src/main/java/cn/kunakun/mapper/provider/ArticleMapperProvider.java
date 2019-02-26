package cn.kunakun.mapper.provider;

import java.util.Map;

import cn.kunakun.utils.StringBuilderHolder;

/**
 * @author YangKun
 * @date 2018年5月3日下午2:14:05
 */
public class ArticleMapperProvider {
	
	public String count(Map<String,Object> params){
		StringBuilder sql = StringBuilderHolder.getGlobal();
		sql.append(" SELECT ");
		sql.append(" 	* ");
		sql.append(" FROM ");
		sql.append(" 	t_student WHERE 1=1");
		if (params != null) {
			Integer id=(Integer)params.get("id");
			if (id!=null)
				sql.append(" AND id = #{id}");
		}
		return sql.toString();
	}

}
