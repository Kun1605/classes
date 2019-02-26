package cn.kunakun.config;

import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.host}")
    private String mongodbHost;
    @Value("${spring.data.mongodb.database}")
    private String mongodatabase;


    @Bean
    public MongoClientOptions mongoClientOptions() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectTimeout(5000);//设置连接超时时间
        builder.socketTimeout(5000);//读取数据的超时时间
        builder.connectionsPerHost(30);//设置每个地址最大连接数
        builder.writeConcern(WriteConcern.NORMAL);//设置写入策略  ,只有网络异常才会抛出
        return builder.build();
    }

    @Bean
    public MongoClient mongoClient() {
        LinkedList<MongoCredential> list = Lists.newLinkedList();
        MongoCredential credential = MongoCredential.createScramSha1Credential("banji", mongodatabase, "Yang7728163".toCharArray());
        list.add(credential);
        return new MongoClient( new ServerAddress(mongodbHost),list, mongoClientOptions());
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase(mongodatabase);
    }



}

