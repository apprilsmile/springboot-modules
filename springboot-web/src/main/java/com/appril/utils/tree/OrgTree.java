package com.appril.utils.tree;

/**
 * @author zhangyang
 * @date 2021/5/21 11:48
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrgTree extends TreeNode{
    private String name;
}
