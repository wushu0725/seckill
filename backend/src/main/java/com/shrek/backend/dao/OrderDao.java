package com.shrek.backend.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    public int createOrder(@Param("seckillId") String seckillId,@Param("userid") String userid);
}
