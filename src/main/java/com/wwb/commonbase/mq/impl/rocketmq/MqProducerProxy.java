package com.wwb.commonbase.mq.impl.rocketmq;

import com.wwb.commonbase.utils.SpringContextUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * @author weibo
 */
public class MqProducerProxy {

    public static Boolean sendMq(RocketMqConfig mqConfig, byte[] msg, String tag, Integer delayLevel) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("groupName", mqConfig.getGroupName());
        map.put("instanceName", mqConfig.getInstanceName());
        map.put("topic", mqConfig.getTopic());
        map.put("tag", tag);
        map.put("delayLevel", delayLevel);
        map.put("msg", new String(msg));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = null;
        try {
            restTemplate = SpringContextUtil.getBean(RestTemplate.class);
        } catch (Exception e) {
            throw new Exception("请设置RestTemplateConfig");

        }

        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(mqConfig.getNamesrvAddr(), request, Boolean.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        throw new Exception("发送消息失败" + request.getBody());
    }
}
