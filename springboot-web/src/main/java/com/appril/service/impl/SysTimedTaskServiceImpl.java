package com.appril.service.impl;

import com.appril.entity.SysTimedTask;
import com.appril.mapper.SysTimedTaskMapper;
import com.appril.service.SysTimedTaskIService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author appril
 * @since 2020-10-28
 */
@Service
public class SysTimedTaskServiceImpl extends ServiceImpl<SysTimedTaskMapper, SysTimedTask> implements SysTimedTaskIService {

    @Override
    public void simpleTest() {
        System.out.println("*********************");
    }
}
