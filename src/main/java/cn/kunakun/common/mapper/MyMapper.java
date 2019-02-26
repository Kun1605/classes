package cn.kunakun.common.mapper;

import java.util.List;

import cn.kunakun.pojo.Message;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
	
	
}