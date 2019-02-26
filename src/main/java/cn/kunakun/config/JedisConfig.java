package cn.kunakun.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class JedisConfig {
	@Value("${redis.maxTotal}")
	private Integer maxTotal;
	@Value("${redis.node1.Host}")
	private String host1;
	@Value("${redis.node1.port}")
	private Integer port1;
	@Value("${redis.node1.password}")
	private String password1;
	@Bean
	public ShardedJedisPool shardedJedisPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		List<JedisShardInfo> list =new ArrayList<JedisShardInfo>();
		JedisShardInfo jedisShardInfo = new JedisShardInfo(host1, port1);
		jedisShardInfo.setPassword(password1);
		list.add(jedisShardInfo);
		return new ShardedJedisPool(config, list );
	}
}
