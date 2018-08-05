package org.wxy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import org.wxy.consts.Consts;

/**
 * @author: wu.xiaoyuan
 * @date: 2018-08-03
 **/
public class AuthorizeService {

  /**
   * 处理用户的授权请求
   * @param response_type
   * @param client_id
   * @param redirect_uri
   * @param scope
   * @param state
   * @return true：通过验证，false：验证失败
   */
  public boolean authorize(String response_type, String client_id, String redirect_uri,
      String scope,
      String state) {
     // 验证client_id来确认应用程序的身份，redirect_url等是否合法
    System.out.println("开始授权身份校验");
    System.out.print("response_type="+response_type);
    System.out.print(",client_id="+client_id);
    System.out.print(",redirect_uri="+redirect_uri+"\n");
    if ("code".equals(response_type) && Consts.client_id.equals(client_id) && Consts.redirect_uri
        .equals(redirect_uri)) {
      System.out.println("授权身份校验通过");
      return true;
    }
    System.out.println("授权身份校验未通过");
    return false;
  }

  /**
   * 获取token
   * @param content
   * @return
   */
  public Map<String,String> accessToken(String content){
    Map<String,String> res=validata(content);
    return res;
  }

  /**
   * 参数校验
   * @param content
   * @return
   */
  private Map<String, String> validata(String content) {
    Map<String, String> res = new HashMap<String, String>();
    JSONObject json = JSON.parseObject(content);
    String grant_type = json.getString("grant_type");
    String client_id = json.getString("client_id");
    String client_secret = json.getString("client_secret");
    String redirect_uri = json.getString("redirect_uri");
    String code = json.getString("code");

    System.out.println("开始token请求校验");
    System.out.print("grant_type="+grant_type);
    System.out.print(",client_id="+client_id);
    System.out.print(",client_secret="+client_secret);
    System.out.print(",redirect_uri="+redirect_uri);
    System.out.print(",code="+code+"\n");
    // 参数校验
    if ("authorization_code".equals(grant_type) && Consts.client_id.equals(client_id)
        && Consts.client_secret.equals(client_secret) && Consts.redirect_uri.equals(redirect_uri)
        && "xxx123yyy".equals(code)) {
      res.put("result", "success");
      res.put("access_token", Consts.access_token);
      res.put("token_type", "bearer");
      res.put("scope", "user");
      System.out.println("token请求校验通过");
      return res;
    }
    /**
     * 事实上应该根据错误类型，给出明确的错误信息，这里我只是模拟下处理流程。
     */
    System.out.println("token请求校验未通过");
    res.put("result", "fail");
    res.put("message", "请求参数有误！");
    return res;
  }
}
