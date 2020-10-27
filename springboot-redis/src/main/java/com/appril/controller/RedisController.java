package com.appril.controller;

import com.appril.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用单元测试取代controller
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/stringSet")
    public Map<String,Object> stringSet(@RequestParam("key") String key, @RequestParam("value") String value){
        Map<String,Object> result = new HashMap<>();
       try {
           String res = redisUtil.set(key, value);
           result.put("flag",res);
       }catch (Exception e){
           e.printStackTrace();
           result.put("flag","ERROR");
       }
       return  result;
    }

    @GetMapping("/stringSetExpire")
    public Map<String,Object> stringSetExpire(@RequestParam("key") String key, @RequestParam("value") String value,@RequestParam("expireSecond") Long expireSecond){
        Map<String,Object> result = new HashMap<>();
        try {
            String res = redisUtil.setExpire(key, value,expireSecond);
            result.put("flag",res);
        }catch (Exception e){
            e.printStackTrace();
            result.put("flag","ERROR");
        }
        return  result;
    }

    @GetMapping("/stringGet")
    public Map<String,Object> stringGet(@RequestParam("key") String key){
        Map<String,Object> result = new HashMap<>();
        try {
            String res = redisUtil.get(key);
            result.put("res",res);
            result.put("flag","OK");
        }catch (Exception e){
            e.printStackTrace();
            result.put("flag","ERROR");
        }
        return result;
    }

    @GetMapping("/stringDelete")
    public Map<String,Object> stringDelete(@RequestParam("key") String key){
        Map<String,Object> result = new HashMap<>();
        try {
            Long res = redisUtil.delete(key);
            result.put("res",res);
            result.put("flag","OK");
        }catch (Exception e){
            e.printStackTrace();
            result.put("flag","ERROR");
        }
        return result;
    }
}
