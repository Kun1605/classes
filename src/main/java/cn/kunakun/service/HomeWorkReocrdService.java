package cn.kunakun.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.mapper.HomeWorkReocrdMapper;
import cn.kunakun.pojo.HomeWorkReocrd;
import cn.kunakun.pojo.Student;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author YangKun
 * @date 2018年4月20日下午8:04:08
 */
@Service
public class HomeWorkReocrdService extends BaseService<HomeWorkReocrd> {
	@Autowired
	HomeWorkReocrdMapper mapper;
	
	public PageInfo<HomeWorkReocrd> queryPageListByLike1(PageVO pageVO,HomeWorkReocrd t){
		// 设置分页条件
		PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
		@SuppressWarnings("unchecked")
		Map<String, String> map = JSONObject.parseObject(pageVO.getFilter(), Map.class);
		Example example=new Example(t.getClass());
		Criteria criteria = example.createCriteria();
		if (map!=null) {
			for (Entry<String, String> entry : map.entrySet()) {
				if (entry.getKey().contains(".")) {
					String[] strings = entry.getKey().split("\\.");
					String domain1=strings[0];
					String field=strings[1];
					Student record =new Student();
					if (field.equals("name")) {
						record.setName(entry.getValue());
					}
					Example example2 =new Example(Student.class);
					example2.createCriteria().andLike(field, "%"+entry.getValue()+"%");
					List<Student> selectByExample = studentMapper.selectByExample(example2);
					if (!selectByExample.isEmpty()) {
						Long studentId = selectByExample.get(0).getId();
						criteria.andCondition(" student_id = " + studentId);
					}else {
						criteria.andCondition(" student_id = 0 ");
					}
					continue;
				}
				criteria.andLike(entry.getKey(), "%" + entry.getValue() + "%");
			} 
			
		}
		example.setOrderByClause("create_time DESC");
		if (t.getWork_id()!=null) {
			criteria.andEqualTo(t);
		}
		List<HomeWorkReocrd> selectByExample = mapper.selectByExample(example);
		return new PageInfo<HomeWorkReocrd>(selectByExample);
	}

}
