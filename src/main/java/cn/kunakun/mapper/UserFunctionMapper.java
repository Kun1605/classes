package cn.kunakun.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.pojo.UserFunction;

import java.util.List;

@Repository
public interface UserFunctionMapper extends MyMapper<UserFunction> {

    List<UserFunction> queryUserAuthList(UserFunction userFunction);
}
