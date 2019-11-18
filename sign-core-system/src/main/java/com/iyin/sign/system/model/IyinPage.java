package com.iyin.sign.system.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName:   IyinPage
 * @Description: 分页
 * @Author:      luwenxiong
 * @CreateDate:  2018/7/11 12:02
 * @UpdateUser:  luwenxiong
 * @UpdateDate:  2018/9/11 12:02
 * @Version:     0.0.1
 */
@ApiModel(description = "分页结果")
@Slf4j
@Data
public class IyinPage<T> implements IPage<T> {
	private static final long serialVersionUID = 8545996863226528798L;

	/**
	 * 查询数据列表
	 */
	@ApiModelProperty("查询数据列表")
	private List<T> records = Collections.emptyList();
	/**
	 * 总数，当 total 为 null 或者大于 0 分页插件不在查询总数
	 */
	@ApiModelProperty("总数")
	private long total = 0;
	/**
	 * 每页显示条数，默认 10
	 */
	@ApiModelProperty("每页显示条数")
	private long size = 10;
	/**
	 * 当前页
	 */
	@ApiModelProperty("当前页")
	private long current = 1;
	/**
	 * <p>
	 * SQL 排序 ASC 数组
	 * </p>
	 */
	@ApiModelProperty(hidden = true)
	private String[] ascs;
	/**
	 * <p>
	 * SQL 排序 DESC 数组
	 * </p>
	 */
	@ApiModelProperty(hidden = true)
	private String[] descs;
	/**
	 * <p>
	 * 自动优化 COUNT SQL
	 * </p>
	 */
	@ApiModelProperty(hidden = true)
	private boolean optimizeCountSql = true;

	public IyinPage() {
		// to do nothing
	}

	/**
	 * <p>
	 * 分页构造函数
	 * </p>
	 *
	 * @param current 当前页
	 * @param size    每页显示条数
	 */
	public IyinPage(long current, long size) {
		this(current, size, 0L);
	}

	public IyinPage(long current, long size, Long total) {
		if (current > 1) {
			this.current = current;
		}
		this.size = size;
		this.total = total;
	}

	/**
	 * <p>
	 * 是否存在上一页
	 * </p>
	 *
	 * @return true / false
	 */
	public boolean hasPrevious() {
		return this.current > 1;
	}

	/**
	 * <p>
	 * 是否存在下一页
	 * </p>
	 *
	 * @return true / false
	 */
	public boolean hasNext() {
		return this.current < this.getPages();
	}

	@Override
	public List<T> getRecords() {
		return this.records;
	}

	@Override
	public IPage<T> setRecords(List<T> records) {
		this.records = records;
		return this;
	}

	@Override
	public long getTotal() {
		return this.total;
	}

	@Override
	public IPage<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public IPage<T> setSize(long size) {
		this.size = size;
		return this;
	}

	@Override
	public long getCurrent() {
		return this.current;
	}

	@Override
	public IPage<T> setCurrent(long current) {
		this.current = current;
		return this;
	}

	@Override
	public String[] ascs() {
		return ascs;
	}

	public IPage<T> setAscs(List<String> ascs) {
		if (CollectionUtils.isNotEmpty(ascs)) {
			this.ascs = ascs.toArray(new String[0]);
		}
		return this;
	}


	/**
	 * <p>
	 * 升序
	 * </p>
	 *
	 * @param ascs 多个升序字段
	 * @return
	 */
	public IPage<T> setAsc(String... ascs) {
		this.ascs = ascs;
		return this;
	}

	@Override
	public String[] descs() {
		return descs;
	}

	public IPage<T> setDescs(List<String> descs) {
		if (CollectionUtils.isNotEmpty(descs)) {
			this.descs = descs.toArray(new String[0]);
		}
		return this;
	}

	/**
	 * <p>
	 * 降序
	 * </p>
	 *
	 * @param descs 多个降序字段
	 * @return
	 */
	public IPage<T> setDesc(String... descs) {
		this.descs = descs;
		return this;
	}

	@Override
	public boolean optimizeCountSql() {
		return optimizeCountSql;
	}

	public IPage<T> setOptimizeCountSql(boolean optimizeCountSql) {
		this.optimizeCountSql = optimizeCountSql;
		return this;
	}

}
