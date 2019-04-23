package com.xmcc.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @RequestMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url = "http://xmccjyqs.natapp1.cc/sell/wechat/getUserInfo";
        //构造微信授权的URL 参数1：获取授权码地址 参数2：授权策略 参数3：自己携带的数据
        String authorizationUrl = wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl, "UTF-8"));
        return "redirect:" + authorizationUrl;
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(@RequestParam("code") String code,@RequestParam("state") String state) throws UnsupportedEncodingException {
        //获取令牌
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        WxMpUser wxMpUser = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        //获取用户信息
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            log.info("微信昵称:{}",wxMpUser.getNickname());
            log.info("微信openid:{}",wxMpUser.getOpenId());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "redirect:" + URLDecoder.decode(state,"UTF-8") + "?openid=" + wxMpUser.getOpenId();
    }

    //测试授权是否成功
    @RequestMapping("/testOpenid")
    public void test(@RequestParam("openid") String openid){
        log.info("用户获得的openid：{}",openid);
    }
}
