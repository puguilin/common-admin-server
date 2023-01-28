package com.github.guilin.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * 获取客户端请求真实地址
 *
 * @author CaoChenLei
 */
@Slf4j
public class RegionUtils {
    private static RestTemplate restTemplate = new RestTemplate();
    private static String requestUrl = "http://apis.juhe.cn/ip/ipNew?ip=IP&key=7210b4e0c1d7e8aa033dac20620ecae6";

    public static String getRemoteRegion(String ip) {
        if ("127.0.0.1".equals(ip)) {
            return "XX XX XX XX 内网IP";
        }
        String address = "获取地理信息异常";
        try {
            String result = restTemplate.getForObject(requestUrl.replace("IP", ip), String.class);
            JSONObject obj = JSONObject.parseObject(result);
            JSONObject cityData = JSONObject.parseObject(obj.getString("result"));
            String country = cityData.getString("Country");
            String region = cityData.getString("Province");
            String city = cityData.getString("City");
            String isp = cityData.getString("Isp");
            return String.format("%s %s %s %s", country, region, city, isp);
        } catch (Exception e) {
            log.error("获取地理信息异常：{}", e.getMessage());
        }
        return address;
    }
}
