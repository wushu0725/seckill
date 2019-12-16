package com.shrek.backend.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao {
    public JSONObject findById(@Param("seckillId") String seckillId);

    public int updataNumById(@Param("seckillId") String seckillId);

    public List<JSONObject> select();
}
