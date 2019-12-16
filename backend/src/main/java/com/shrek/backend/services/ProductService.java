package com.shrek.backend.services;

import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 吴署
 * @Date: 2019/12/11 14:36
 * @Description:
 */
@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Cacheable(value = "productcache", key = "#p0")
    public JSONObject findById(String seckillId){
        return  productDao.findById(seckillId);
    }

    public List<JSONObject> select(){
        return  productDao.select();
    }
}
