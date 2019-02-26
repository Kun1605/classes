package cn.kunakun.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author YangKun
 * @date 2018年3月27日下午3:30:55
 */
@Service
public class PropertiesService {
	 @Value("${REPOSITORY_PATH}")
	 public String REPOSITORY_PATH;
}
