package cn.kunakun.service;

import cn.kunakun.mapper.ClickMapper;
import cn.kunakun.pojo.Click;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClickService extends BaseService<Click>{

    @Autowired
    ClickMapper clickMapper;

    public List queryClick() {
        return clickMapper.queryClick();
    }
}
