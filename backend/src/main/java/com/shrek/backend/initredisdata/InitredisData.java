package com.shrek.backend.initredisdata;

import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: 吴署
 * @Date: 2019/12/13 14:51
 * @Description: 启动项目时加载数据库的秒杀表入redis
 */
@Component
@Slf4j
public class InitredisData implements CommandLineRunner {
    @Autowired
    private ProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("预热redis数据");
        initRedis();
    }

    /**
     * 预热秒杀数据到redis
     */
    public void initRedis(){
        List<JSONObject> list= productService.select();
        Map<String,JSONObject> map = new ConcurrentHashMap<String,JSONObject>();

        list.stream().forEach(jsonObject -> {
            System.out.println(jsonObject.toJSONString());
            redisTemplate.opsForValue().set("productcache::"+jsonObject.getString("seckillId"),jsonObject);
            //预热库存数据
            redisTemplate.opsForValue().set(jsonObject.getString("seckillId"),jsonObject.getInteger("inventory"));
        });
    }

}
