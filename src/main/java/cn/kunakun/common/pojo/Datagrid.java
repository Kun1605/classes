package cn.kunakun.common.pojo;

import java.util.List;

/**
 * @author 杨坤
 * 2017年9月8日上午10:29:58
 */
public class Datagrid implements java.io.Serializable {

	private Long total;	// 总记录数
	private List rows;	// 每行记录
	
	public Datagrid() {
		super();
	}

	/**
	 * @param total
	 * @param rows
	 */
	public Datagrid(Long total, List rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

}
