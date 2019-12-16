package com.shrek.backend.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrek.backend.remoteservice.RemoteOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Auther: 吴署
 * @Date: 2019/12/15 15:40
 * @Description:
 */
@Component
@Slf4j
public class OrderService {
    /**
     * 请求包类
     */
    class Request{
        String seckillId;
        String userid;
        //每个请求一个线程观察者
        CompletableFuture<String> future;
    }
    //存请求的队列
    LinkedBlockingDeque<Request> queue = new LinkedBlockingDeque<>();

    public String createOrder(String seckillId,String userid) throws ExecutionException, InterruptedException {
        Request request = new Request();
        request.seckillId=seckillId;
        request.userid=userid;

        CompletableFuture<String> future = new CompletableFuture<>();
        request.future=future;
        //加入到请求队列
        queue.add(request);
        //返回等待，
        return future.get();
    }

    @Autowired
    private RemoteOrderService remoteOrderService;
    /**
     * bean初始化的时候，启动一个线程
     * 每10MS，把队列里的请求批量请求
     */
    @PostConstruct
    public void init(){
        ScheduledExecutorService scheduledExecutorService=
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = queue.size();
                if(size ==0){
                    return;
                }
                //队列里面存在数据
                JSONArray jsonArray = new JSONArray();
                ArrayList<Request> requests = new ArrayList<>();
                JSONObject jsonObject;
                Request request;
                //取出队列，组成批量数据
                for(int i=0;i<size;i++){
                    jsonObject=new JSONObject();
                    request = queue.poll();
                    requests.add(request);
                    jsonObject.put("seckillId",request.seckillId);
                    jsonObject.put("userid",request.userid);
                    jsonArray.add(jsonObject);
                }
                log.info("批量提交的大小："+jsonArray.size());
                //调用远程创建订单的批量接口
                JSONObject result = remoteOrderService.createOrder(jsonArray);

                //正确返回
                if(result.getString("returnCode").equals("100")){
                    for (Request request1 : requests) {
                        JSONObject jsonObject1 = result.getJSONObject("returnData");
                        //通过userid唯一取出结果并将请求的future完成触发之前的请求等待
                        String result1 = jsonObject1.getString(request1.userid);
                        request1.future.complete(result1);
                    }
                }
            }
        },0,10,TimeUnit.MICROSECONDS);//定时任务每10毫秒调用一次
    }
}
