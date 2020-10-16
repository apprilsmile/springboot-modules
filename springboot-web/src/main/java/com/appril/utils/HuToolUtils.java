package com.appril.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author zhangyang
 * @date 2020/09/25
 * @description
 **/
public class HuToolUtils {
    public static long getId(){
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        return snowflake.nextId();
    }
}
