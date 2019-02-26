package cn.kunakun.crawler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
 * @author zhijun
 * 
 */
public class Item  {

	/**
	 * 商品id，同时也是商品编号
	 */
	private Long id;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 商品卖点
	 */
	private String sellPoint;
	
	/**
	 * 商品价格，单位为：分
	 */
	private Long price;
	
	/**
	 * 库存数量
	 */
	private Long num;
	
	/**
	 * 商品条形码
	 */
	private String barcode;
	
	/**
	 * 商品图片
	 */
	private String image;
	
	/**
	 * 所属类目，叶子类目
	 */
	private Long cid;
	
	/**
	 * 商品状态，1-正常，2-下架，3-删除
	 */
	private Integer status;
	


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	} 
	

}
