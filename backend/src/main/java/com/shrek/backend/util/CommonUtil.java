package com.shrek.backend.util;

import com.alibaba.fastjson.JSONObject;

public class CommonUtil {

    /**
     * 返回一个returnData为空对象的成功消息的json
     *
     * @return
     */
    public static JSONObject successJson() {
        return successJson(new JSONObject());
    }

    /**
     * 返回一个返回码为100的json
     *
     * @param returnData json里的主要内容
     * @return
     */
    public static JSONObject successJson(Object returnData) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("returnCode", "100");
        resultJson.put("returnMsg", "scuess");
        resultJson.put("returnData", returnData);
        return resultJson;
    }

    /**
     * 返回错误信息JSON
     *
     * @return
     */
    public static JSONObject errorJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("returnCode", 500);
        resultJson.put("returnMsg", "操作失败");
        resultJson.put("returnData", new JSONObject());
        return resultJson;
    }
}
