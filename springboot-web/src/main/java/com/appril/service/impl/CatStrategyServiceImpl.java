package com.appril.service.impl;

import com.appril.service.IAnimalStrategyService;
import org.springframework.stereotype.Service;

/**
 * @author zhangyang
 * @date 2021/12/30 16:16
 */
@Service("catStrategyService")
public class CatStrategyServiceImpl implements IAnimalStrategyService {
    @Override
    public String printName() {
        return "I am a cat";
    }
}
