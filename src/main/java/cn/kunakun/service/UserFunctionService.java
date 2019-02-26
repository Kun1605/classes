package cn.kunakun.service;

import cn.kunakun.common.pojo.PageVO;
import cn.kunakun.mapper.UserFunctionMapper;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kunakun.pojo.UserFunction;

import java.io.IOException;
import java.util.List;

@Service
public class UserFunctionService extends BaseService<UserFunction> {
    @Autowired
    UserFunctionMapper userFunctionMapper;

    public PageInfo<UserFunction> queryUserAuthList(PageVO<UserFunction> pageVO) {
        PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
        UserFunction userFunction = new UserFunction();
        userFunction = pageVO.getT(userFunction);
        List<UserFunction> userFunctions = userFunctionMapper.queryUserAuthList(userFunction);
        return new PageInfo<UserFunction>(userFunctions);
    }
}
