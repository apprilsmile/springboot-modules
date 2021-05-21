package com.appril.utils.tree;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TreeUtil {
    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

        List<T> trees = new ArrayList<>();
        for (T treeNode : treeNodes) {

            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }

            for (T it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
    /**
     * 组织架构递归查找子节点包含自己
     *
     * @param treeNodes
     * @return
     */
    public OrgTree findOrgChildren(OrgTree treeNode, List<OrgTree> treeNodes) {
        List<TreeNode> children = new ArrayList<>();
        OrgTree all = new OrgTree();
        all.setId(treeNode.getId());
        all.setName("全部");
        all.setParentId(treeNode.getParentId());
        children.add(all);
        treeNode.setChildren(children);
        for (OrgTree it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                treeNode.add(findOrgChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public List<OrgTree> buildOrgByRecursive(List<OrgTree> treeNodes, Object root) {
        List<OrgTree> trees = new ArrayList<>();
        for (OrgTree treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findOrgChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }
}
