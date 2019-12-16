package com.shrek.backend.services;

import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.dao.OrderDao;
import com.shrek.backend.dao.ProductDao;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: 吴署
 * @Date: 2019/12/11 15:29
 * @Description:
 */
@Service
public class SecKillService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 秒杀一般只限购1个，所以数量都是1
     * @return 1 成功 0 失败
     */
    public String secKill(String seckillId,String userid){
        /**
         * 第一步：判断当前时间是否在秒杀时间段内
         * 第二步：判断是否有库存：有  库存减一  没有直接返回售完
         * 第三步：创建订单，返回成功
         */
        //判断当前时间是否在秒杀时间段内
        JSONObject jsonObject = productService.findById(seckillId);
        if(jsonObject==null){
            return "0";
        }
        if(judgeTime(jsonObject.getTimestamp("starttime"),jsonObject.getTimestamp("endtime"))){
            return "0";
        }

        //判断之前userid是否购买过，买过则直接返回0
        if(redisTemplate.opsForValue().getAndSet(userid,1)!=null){
            return "0";
        }
        redisTemplate.opsForValue().set(userid,"1",1000L*60,TimeUnit.MICROSECONDS);
        /**
         * 从redis获取库存数，因为redis的单线程，所以也不会出现超卖现象
         */
        long num = redisTemplate.opsForValue().decrement(seckillId);
        if(num<0){
            //库存已售完
            return "0";
        }

        //更新库存减一，成功返回1，失败返回0
        //优化：订单进入发送到订单MQ，由订单服务去消费
//        int isSelfOut = productDao.updataNumById(seckillId);
//        if(isSelfOut==0) {
//            return 0;
//        }
        //创建订单
//        orderDao.createOrder(seckillId,userid);
        try {
            return orderService.createOrder(seckillId,userid);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 如果当前时间在秒杀时间端，返回false，不在返回true
     * @param starttime
     * @param endtime
     * @return
     */
    private boolean judgeTime(Timestamp starttime, Timestamp endtime) {
        Date date = new Date();
        return !(date.getTime()>starttime.getTime()&&date.getTime()<endtime.getTime());
    }

}
