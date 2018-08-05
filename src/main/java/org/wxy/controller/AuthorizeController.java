package org.wxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wxy.consts.Consts;
import org.wxy.service.AuthorizeService;

/**
 * @author: wu.xiaoyuan
 * @date: 2018-08-03
 **/
@Controller
public class AuthorizeController {


  /**
   *  对应授权码模式中的步骤（B）<br>
   *  授权请求校验
   * @param response_type
   * @param client_id
   * @param redirect_uri
   * @param scope
   * @param state
   */
  @RequestMapping("/authorize")
  public void authorize(String response_type, String client_id, String redirect_uri, String scope,
      String state, HttpServletResponse response) {
    AuthorizeService authorizeService = new AuthorizeService();
    boolean result = authorizeService
        .authorize(response_type, client_id, redirect_uri, scope, state);
    // 授权请求校验通过，重定向到授权界面
    if (result) {
      redirectToAuthPage(response);
      return;
    }
    // 授权请求校验未通过，重定向到指定页面
    try {
      response.sendRedirect("http://localhost:8080/questions");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 返回授权页面
   */
  @RequestMapping(value = "/getAuthPage", method = RequestMethod.GET)
  public String getAuthPage() {
    return "authPage";
  }

  /**
   * 对应授权码模式中的步骤（C）<br>
   * 处理用户的授权行为
   * @param result
   * @param response
   */
  @RequestMapping("/dealAuth")
  public void dealAuth(String result, HttpServletResponse response) {
    if(true){
      try {
        StringBuilder url=new StringBuilder();
        url.append(Consts.redirect_uri);
        url.append("?code=xxx123yyy");
        // 重定向到客户端注册的url，并返回授权码code
        response.sendRedirect(url.toString());
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }

  /**
   * 对应授权码模式中的步骤（E）
   * 处理客户端的获取token请求<br>
   * @param request
   * @return
   */
  @RequestMapping("/accessToken")
  @ResponseBody
  public String accessToken(HttpServletRequest request){
    AuthorizeService authorizeService=new AuthorizeService();
    JSONObject json=new JSONObject();
    try{
      String contend=getStreamAsString(request);
      if(contend==null){
        json.put("status", "fail");
        json.put("message", "request_param is empty");
        return json.toJSONString();
      }
      Map<String,String> res=authorizeService.accessToken(contend);
      String result=res.get("result");
      // 成功则返回token
      if("success".equals(result)){
        json.put("status", "success");
        json.put("access_token", res.get("access_token"));
        json.put("token_type", res.get("token_type"));
        json.put("scope", res.get("scope"));
        return json.toJSONString();
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
    // 失败则给出错误信息
    json.put("status", "fail");
    json.put("error", "invalid_request");
    json.put("error_describe", "this is invalid_request");
    json.put("uri", "http://localhost:8080/questions");
    return  json.toJSONString();
  }

  /**
   * FAQ页面
   * @return
   */
  @RequestMapping(value = "/questions",method = RequestMethod.GET)
  public String questions(){
    return "questions";
  }

  /**
   * 用户信息
   * @return
   */
  @RequestMapping("/getUserInfo")
  @ResponseBody
  public String getUserInfo(HttpServletRequest request){
    JSONObject resp = new JSONObject();
    try {
      String contend = getStreamAsString(request);
      if(contend==null){
        resp.put("status", "fail");
        resp.put("message", "request_param is empty");
        return resp.toJSONString();
      }
      JSONObject req=JSONObject.parseObject(contend);
      String access_token=req.getString("access_token");
      if(Consts.access_token.equals(access_token)){
        resp.put("status", "success");
        resp.put("user_name", "tom");
        return resp.toJSONString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    resp.put("status", "fail");
    resp.put("message", "access_token is invalid");
    return resp.toJSONString();
  }

  /**
   * 重定向到授权界面
   */
  private void redirectToAuthPage(HttpServletResponse response) {
    try {
      response.sendRedirect("http://localhost:8080/getAuthPage");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取请求内容参数
   * @param request
   * @return
   */
  private String getStreamAsString(HttpServletRequest request){
    try{
      BufferedReader reader = request.getReader();
      char[] buf = new char[1024];
      int len = 0;
      StringBuffer contentBuffer = new StringBuffer();
      while ((len = reader.read(buf)) != -1) {
        contentBuffer.append(buf, 0, len);
      }
      String contend = contentBuffer.toString();
      if(!StringUtils.isEmpty(contend)){
//        System.out.println("contend="+contend);
        return  contend;
      }
    }catch (IOException e){
      e.printStackTrace();
    }
    return null;
  }

}
