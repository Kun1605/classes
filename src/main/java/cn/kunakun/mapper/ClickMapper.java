package cn.kunakun.mapper;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.pojo.Click;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickMapper extends MyMapper<Click> {

    List<Click> queryClick();
}
