package cn.kunakun.service;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.kunakun.mapper.FunctionMenuMapper;
import cn.kunakun.mapper.UserFunctionMapper;
import cn.kunakun.pojo.FunctionMenu;
import cn.kunakun.pojo.Student;
import cn.kunakun.pojo.UserFunction;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
* @author 杨涛
* 2017年11月12日下午15:20
* 主页动态显示菜单
*/
@Component
public class HomeMenuSerivce {
	private static Logger logger = LoggerFactory.getLogger(HomeMenuSerivce.class);

	@Autowired
	FunctionMenuMapper functionMenuMapper;
	@Autowired
	UserFunctionMapper userFunctionMapper;
	
	public List<Pair<FunctionMenu,List<FunctionMenu>>> menu(Student student){
		List<Pair<FunctionMenu,List<FunctionMenu>>> menus = new ArrayList<>();
		if("admin".equals(student.getName())) {
			//找出一级菜单 
			FunctionMenu temp =new FunctionMenu();
			temp.setOnemenu(1L);
			List<FunctionMenu> oneMenus = functionMenuMapper.select(temp);
			logger.debug("oneMenus:{}",oneMenus);
			for (FunctionMenu functionMenu : oneMenus) {
//				找出二级菜单
				temp=new FunctionMenu();
				temp.setPid(functionMenu.getId());
				List<FunctionMenu> twoMenus = functionMenuMapper.select(temp);
//				List<FunctionMenu> twoMenus = functionMenuMapper.findByTwo(functionMenu.id);
				logger.debug("twoMenus:{}",twoMenus);
				Pair<FunctionMenu,List<FunctionMenu>> pair = Pair.of(functionMenu, twoMenus);
				menus.add(pair);
			}
		}else {
//			UserFunction userFunction = userFunctionMapper.findByUserId(student.getId());
//			找出来userid
			UserFunction userFunctionTemp =new UserFunction();
			userFunctionTemp.setUser_id(student.getId());
			UserFunction userFunction = userFunctionMapper.selectOne(userFunctionTemp );
			if(userFunction != null) {
//				List<FunctionMenu> twoMenus = functionMenuMapper.findByIds(userFunction.funids);
				Example example = new Example(FunctionMenu.class);
				Criteria criteria = example.createCriteria();
				Iterable values =Arrays.asList(userFunction.funids.split(","));
				criteria.andIn("id", values );
				List<FunctionMenu> twoMenus = functionMenuMapper.selectByExample(example);
				logger.debug("twoMenus:{}",twoMenus);
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < twoMenus.size(); i++) {
					sb.append(twoMenus.get(i).pid);
					if(i!=twoMenus.size()-1) {
						sb.append(",");
					}
				}
//				List<FunctionMenu> oneMenus = functionMenuMapper.findByIds(sb.toString());
				Example example2 = new Example(FunctionMenu.class);
				 Criteria criteria2 = example2.createCriteria();
				 List<String> values1 = Arrays.asList(sb.toString().split(","));
				 criteria2.andIn("id", values1);
				List<FunctionMenu> oneMenus = functionMenuMapper.selectByExample(example2);
				logger.debug("oneMenus:{}",oneMenus);
				for (FunctionMenu one : oneMenus) {
					List<FunctionMenu> twos = new ArrayList<>();
					for (FunctionMenu two : twoMenus) {
						if(one.getId() == two.getPid()) {
							twos.add(two);
						}
					}
					logger.debug("twos:{}",twos);
					Pair<FunctionMenu,List<FunctionMenu>> pair = Pair.of(one, twos);
					menus.add(pair);
				}
			}		
		}
		return menus;
	}
}
