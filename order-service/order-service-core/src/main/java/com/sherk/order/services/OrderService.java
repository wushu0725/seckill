package com.sherk.order.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sherk.order.dao.OrderDao;
import com.sherk.order.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: 吴署
 * @Date: 2019/12/15 14:57
 * @Description:
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    public int createOrder(JSONObject jsonObject){
        return orderDao.createOrder(jsonObject);
    }

    public JSONObject createOrders(JSONArray jsonArray){
        JSONObject jsonObject = new JSONObject();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            //创建订单
            jsonObject.put(jsonObject1.getString("userid"),orderDao.createOrder(jsonArray.getJSONObject(i)));
            //更新库存
            productDao.updataNumById(jsonObject1.getString("seckillId"));
        }
        return jsonObject;
    }
}
