package com.appril.controller;

import com.appril.service.IAnimalStrategyService;
import com.appril.strategy.AnimalContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangyang
 * @date 2020/10/16
 * @description
 **/
@RestController
@RequestMapping("/strategy")
public class StrategyController {
    @Resource
    private AnimalContext animalContext;

    @GetMapping("/test")
    public String test(@RequestParam("param") String param){
        IAnimalStrategyService iAnimalStrategyService = animalContext.getIAnimalStrategyService(param);
        System.out.println(iAnimalStrategyService.getClass());
        return iAnimalStrategyService.printName();
    }


}
