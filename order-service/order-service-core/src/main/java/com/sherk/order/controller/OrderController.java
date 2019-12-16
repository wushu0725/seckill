package com.sherk.order.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sherk.order.services.OrderService;
import com.sherk.order.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 吴署
 * @Date: 2019/12/15 15:02
 * @Description:
 */

@RestController
@RequestMapping("/orderservice")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("createone")
    public JSONObject createOrder(@RequestBody JSONObject jsonObject){
        return CommonUtil.successJson(orderService.createOrder(jsonObject));
    }
    @PostMapping("createmany")
    public JSONObject createOrderList(@RequestBody JSONArray jsonArray){
        return CommonUtil.successJson(orderService.createOrders(jsonArray));
    }
}
