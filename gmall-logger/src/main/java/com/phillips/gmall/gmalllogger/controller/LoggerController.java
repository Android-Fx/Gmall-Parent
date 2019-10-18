package com.phillips.gmall.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phillips.gmall.common.constants.GmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // Controller + ResponseBody //log4j logback logging //lombok加上注解就有get和set的方法了
@Slf4j
public class LoggerController {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/log")
    public String log(@RequestParam("logString") String logString) {

        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts", System.currentTimeMillis());

        log.info(logString);

        // 启动日志
        if ("startup".equals(jsonObject.get("type"))) {
            kafkaTemplate.send(GmallConstant.KAFKA_STARTUP, jsonObject.toJSONString());
        } else { // 事件日志
            kafkaTemplate.send(GmallConstant.KAFKA_EVENT, jsonObject.toJSONString());
        }

        return "success";
    }
}
