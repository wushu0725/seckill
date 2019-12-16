package com.shrek.backend.remoteservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: 吴署
 * @Date: 2019/12/16 13:43
 * @Description:
 */
@Component
public class RemoteOrderService {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 这里应该是用OPENCLOUD的feign来调用，暂时通过rest调用
     * @param jsonArray
     * @return
     */
    public JSONObject createOrder(JSONArray jsonArray){
        String apiURL = "http://127.0.0.1:8888/orderservice/createmany";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONArray> request = new HttpEntity<JSONArray>(jsonArray, headers);
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(apiURL, request, JSONObject.class);

        return jsonObjectResponseEntity.getBody();
    }
}
