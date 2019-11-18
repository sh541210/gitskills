package com.iyin.sign.system.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TreeNode.java
 * @Description: 树形结构
 * @Author: yml
 * @CreateDate: 2019/7/17
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/17
 * @Version: 1.0.0
 */
@Data
public class TreeNode {
	protected String id;
	protected String parentId;
	protected List<TreeNode> children = new ArrayList<>();

	public void add(TreeNode node) {
		children.add(node);
	}
}
