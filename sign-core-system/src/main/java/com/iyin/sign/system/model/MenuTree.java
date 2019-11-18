package com.iyin.sign.system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: MenuTree.java
 * @Description: 树形节点
 * @Author: yml
 * @CreateDate: 2019/7/17
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/17
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {
	private String icon;
	private String name;
	private boolean spread = false;
	private String path;
	private String component;
	private String authority;
	private String redirect;
	private String keepAlive;
	private String code;
	private String type;
	private String label;
	private Integer sort;

	public MenuTree() {
	}

	public MenuTree(String id, String name, String parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
	}

	public MenuTree(String id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
	}

	public MenuTree(MenuVO menuVo) {
		this.id = menuVo.getMenuId();
		this.parentId = menuVo.getParentId();
		this.icon = menuVo.getIcon();
		this.name = menuVo.getName();
		this.path = menuVo.getPath();
		this.component = menuVo.getComponent();
		this.type = menuVo.getType();
		this.label = menuVo.getName();
		this.sort = menuVo.getSort();
		this.keepAlive = menuVo.getKeepAlive();
	}
}
