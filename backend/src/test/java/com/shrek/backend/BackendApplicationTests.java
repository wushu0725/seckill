package com.shrek.backend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private ProductService productService;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Test
	public void test(){
		JSONObject jsonObject = productService.findById("1000");

		Timestamp starttime = jsonObject.getTimestamp("starttime");
		Date date = new Date();
		System.out.println(starttime.getTime()>date.getTime());
	}

	@Test
	public void test2(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("aa","bbb");
		rabbitTemplate.convertAndSend("test-exchanges","test.aaa",jsonObject);
	}

	@Autowired
	RestTemplate restTemplate;

	@Test
	public void test3(){
		String apiURL = "http://127.0.0.1:8888/orderservice/createmany";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("seckillId","1000");
		jsonObject.put("userid","12346");
		jsonArray.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("seckillId","1000");
		jsonObject.put("userid","12348");
		jsonArray.add(jsonObject);

		jsonObject = new JSONObject();
		jsonObject.put("seckillId","1000");
		jsonObject.put("userid","12347");
		jsonArray.add(jsonObject);

		HttpEntity<JSONArray> request = new HttpEntity<JSONArray>(jsonArray, headers);

		ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(apiURL, request, JSONObject.class);

		System.out.println(jsonObjectResponseEntity.getBody().toJSONString());
	}

}
