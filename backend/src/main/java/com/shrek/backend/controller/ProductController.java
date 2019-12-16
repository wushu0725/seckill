package com.shrek.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.services.ProductService;
import com.shrek.backend.services.SecKillService;
import com.shrek.backend.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: 吴署
 * @Date: 2019/12/11 14:09
 * @Description:
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SecKillService secKillService;

    @GetMapping("/pruduct")
    public JSONObject productById(@Param("secKillId") String secKillId){
        return CommonUtil.successJson(productService.findById(secKillId));
    }

    @GetMapping("/scrKill")
    public JSONObject scrKill(@Param("secKillId") String secKillId,@Param("userid")String userid){
        return CommonUtil.successJson(secKillService.secKill(secKillId,userid));
    }
}
