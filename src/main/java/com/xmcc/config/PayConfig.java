package com.xmcc.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {

    @Autowired
    private WeixinProperties weixinProperties;

    @Bean
    //进行微信支付的配置
    public BestPayService bestPayService(){
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        //设置公众号appid
        wxPayH5Config.setAppId(weixinProperties.getAppid());
        //设置公众号密钥
        wxPayH5Config.setAppSecret(weixinProperties.getSecret());
        //设置商户号
        wxPayH5Config.setMchId(weixinProperties.getMchId());
        //设置商户密钥
        wxPayH5Config.setMchKey(weixinProperties.getMchKey());
        //设置商户证书路径
        wxPayH5Config.setKeyPath(weixinProperties.getKeyPath());
        //设置异步通知路径
        wxPayH5Config.setNotifyUrl(weixinProperties.getNotifyUrl());
        //支付类，所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config);
        return bestPayService;
    }
}
