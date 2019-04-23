package com.xmcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @RequestMapping("/getCode")
    public void getCode(@RequestParam("code") String code){
        log.info("微信授权码code:{}",code);
    }
}
