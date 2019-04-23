package com.xmcc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WeixinProperties {

    private String appid;
    private String secret;
    private String mchId;
    private String mchKey;
    private String keyPath;
    private String notifyUrl;
}
