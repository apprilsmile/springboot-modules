package com.appril.strategy;

import com.appril.service.IAnimalStrategyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zhangyang
 * @date 2021/12/30 16:19
 * 注:Spring会自动地将形如(@Component后面的名称，实现该接口的类)注入到该animalStrategyMap中
 * 在启动后，animalStrategyMap中就存在两个元素，("catStrategyService"，CatStrategyServiceImpl )与("dogStrategyService"，DogStrategyServiceImpl )
 * getIAnimalStrategyService方法返回animalStrategyMap中key=type的IAnimalStrategyService对象
 * ————————————————
 * 版权声明：本文为CSDN博主「丿旧梦、守夜人ヾ」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/weixin_43494537/article/details/118899585
 */
@Component
public class AnimalContext {

    @Resource
    Map<String, IAnimalStrategyService> animalStrategyMap;

    public IAnimalStrategyService getIAnimalStrategyService(String type){
        return animalStrategyMap.get(type);
    }

}
