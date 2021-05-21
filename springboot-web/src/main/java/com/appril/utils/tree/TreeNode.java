package com.appril.utils.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyang
 * @date 2021/5/21 11:47
 */
@Data
public class TreeNode {
    protected long id;
    protected long parentId;
    protected String name;
    protected List<TreeNode> children = new ArrayList<TreeNode>();

    public void add(TreeNode node) {
        children.add(node);
    }
}
