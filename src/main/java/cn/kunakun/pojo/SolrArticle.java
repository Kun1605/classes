package cn.kunakun.pojo;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class SolrArticle {
	@Field("id")
	private String id;
	@Field("title")
	private String title;
	@Field("contentNoTag")
	private String contentNoTag;
	@Field("image")
	private String image;
	@Field("update_time")
	private String update_time ;

}
