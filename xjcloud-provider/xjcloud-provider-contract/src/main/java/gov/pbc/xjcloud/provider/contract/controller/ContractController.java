package gov.pbc.xjcloud.provider.contract.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2020年1月4日18:35:57
 * @author paungmiao@163.com
 */
@RestController
public class ContractController {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @GetMapping("get")
    public String test(){
        return "success";
    }

}
