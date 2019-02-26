package cn.kunakun.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.mapper.StudentMapper;
import cn.kunakun.pojo.Student;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
/**
 * @author YangKun
 * 2018年12月2日 下午7:30:37
 * 
 */
public abstract class BaseService<T> {
	// public abstract Mapper<T> getMapper();
	public ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	// 注入Mapper<T>
	@Autowired
	protected MyMapper<T> mapper;
	
	@Autowired
	StudentMapper studentMapper;

	/**
	 * 根据id查询数据
	 * 
	 * @param id
	 * @return
	 */
	public T queryById(Long id) {
		return mapper.selectByPrimaryKey(id);
	}
	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	public List<T> queryAll() {
		return mapper.select(null);
	}

	/**
	 * 根据条件查询一条数据，如果有多条数据会抛出异常
	 * 
	 * @param record
	 * @return
	 */
	public T queryOne(T record) {
		return mapper.selectOne(record);
	}

	/**
	 * 根据条件查询数据列表
	 * 
	 * @param record
	 * @return
	 */
	public List<T> queryListByWhere(T record) {
		return mapper.select(record);
	}

	/**
	 * 模糊查询
	 * @param pageVO
	 * @param clazz
	 * @return
	 * @throws Exception
	 * @author YangKun
	 * 2018年1月22日 上午1:35:10
	 */
	public PageInfo<T> queryPageListByLike(PageVO pageVO,Class<T> clazz){
		// 设置分页条件
		PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
		@SuppressWarnings("unchecked")
		Map<String, String> map = JSONObject.parseObject(pageVO.getFilter(), Map.class);
		Example example=new Example(clazz);
		if (map!=null) {
			Criteria criteria = example.createCriteria();
			for (Entry<String, String> entry : map.entrySet()) {
				criteria.andLike(entry.getKey(), "%" + entry.getValue() + "%");
			} 
		}
		example.setOrderByClause("create_time DESC");
		List<T> selectByExample = mapper.selectByExample(example);
		return new PageInfo<T>(selectByExample);
	}
	public PageInfo<T> queryPageListEQ(PageVO pageVO, T t){
		// 设置分页条件
		PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
		Example example=new Example(t.getClass());
		example.createCriteria().andEqualTo(t);
		example.setOrderByClause("create_time DESC");
		List<T> selectByExample = mapper.selectByExample(example);
		return new PageInfo<T>(selectByExample);
	}

	/**
	 * 新增数据，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer save(T record) {
		return mapper.insert(record);
	}

	/**
	 * 新增数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer saveSelective(T record) {
		return mapper.insertSelective(record);
	}

	/**
	 * 修改数据，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer update(T record) {
		return mapper.updateByPrimaryKey(record);
	}

	/**
	 * 修改数据，使用不为null的字段，返回成功的条数
	 * 
	 * @param record
	 * @return
	 */
	public Integer updateSelective(T record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 * @return
	 */
	public Integer deleteById(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param clazz
	 * @param property
	 * @param values
	 * @return
	 */
	public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
		Example example = new Example(clazz);
		example.createCriteria().andIn(property, values);
		return mapper.deleteByExample(example);
	}

	/**
	 * 根据条件做删除
	 * 
	 * @param record
	 * @return
	 */
	public Integer deleteByWhere(T record) {
		return mapper.delete(record);
	}

}