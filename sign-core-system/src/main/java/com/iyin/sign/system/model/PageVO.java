package com.iyin.sign.system.model;

import java.util.List;

/**
 * 分页Vo，此类源自com.github.pagehelper.Page的重包装
 * 
 * @author Liu
 * 
 * @param <T>
 */

/**
 * @ClassName:   PageVO
 * @Description: 分页 VO ，此类源自com.github.pagehelper.Page的重包装
 * @Author:      Rambo
 * @CreateDate:  2018/7/11 12:02
 * @UpdateUser:  Rambo
 * @UpdateDate:  2018/7/11 12:02
 * @Version:     0.0.1
 */
public class PageVO<T> implements java.io.Serializable {
	/**
	 * serial_number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private Long totalRows;
	/**
	 * 总页数
	 */
	private Integer totalPages;
	/**
	 * 当前第几页
	 */
	private Integer pageNum;
	/**
	 * 每页记录数
	 */
	private Integer pageSize;
	/**
	 * 当前页记录数
	 */
	private Integer curPageSize;
	/**
	 * 数据列表
	 */
	private List<T> list;

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurPageSize() {
		return curPageSize;
	}

	public void setCurPageSize(Integer curPageSize) {
		this.curPageSize = curPageSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "PageVO [totalRows=" + totalRows + ", totalPages=" + totalPages + ", pageNum=" + pageNum + ", pageSize="
				+ pageSize + ", curPageSize=" + curPageSize + ", list=" + list + "]";
	}

}
