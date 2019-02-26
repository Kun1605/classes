package cn.kunakun.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.pojo.Depart;

@Repository
public interface DepartMapper extends MyMapper<Depart>{
    @Select(value = "select * from t_depart where id =#{id}")
    Depart selectById(@Param(value = "id") Long id);

}
