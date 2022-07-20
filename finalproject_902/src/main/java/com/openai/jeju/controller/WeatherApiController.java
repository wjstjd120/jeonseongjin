package com.openai.jeju.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class WeatherApiController {
   
   @GetMapping("/weather")
   public String restApiGetWeather() throws Exception{
      
      String url="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
            + "?serviceKey=0Zuawyrow%2F4qeLUShUPd5muqBG2r3p1%2FS%2BceedTouf5ECHFApcd20JKiea5VBGwlivuc6dImsG91OQWVXv%2FJ%2FA%3D%3D"
            + "&dataType=JSON"
            + "&numOfRows=10"
            + "&pageNo=1"
            + "&base_date=20220520"
            + "&base_time=0800"
            + "&nx=60"
            + "&ny=127";
      
      HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
            
      System.out.println("#RESULT  : " + resultMap);
      
      JSONObject jsonObj = new JSONObject();
      
      jsonObj.put("result", resultMap);
      
      return jsonObj.toString();
      }
   
   public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr) throws Exception {
      boolean isPost = false;
      
      if("post".equals(type)) {
         isPost = true;
      } else {
         url = "".equals(jsonStr)?url : url + "?request=" + jsonStr;
      }
      return getStringFromURL(url, encoding, isPost, jsonStr,"application/json");
   }
   public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,
         String contentType) throws Exception {
      URL apiURL = new URL(url);
      
      HttpURLConnection conn = null;
      BufferedReader br = null;
      BufferedWriter bw = null;
      
      HashMap<String, Object> resultMap = new HashMap<String, Object>();
      
      try {
         conn = (HttpURLConnection) apiURL.openConnection();
         conn.setConnectTimeout(5000);
         conn.setReadTimeout(5000);
         conn.setDoOutput(true);
         
         if(isPost) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Accept", "*/*");
         } else {
            conn.setRequestMethod("GET");
         }
         conn.connect();
         
         if(isPost) {
            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
            bw.write(parameter);
            bw.flush();
            bw = null;
         }
         
         br = new BufferedReader(new InputStreamReader(conn.getInputStream(),encoding));
         
         String line = null;
         StringBuffer result = new StringBuffer();
         
         while((line=br.readLine()) != null) result.append(line);
         
         ObjectMapper mapper = new ObjectMapper();
         
         resultMap = mapper.readValue(result.toString(), HashMap.class);
      } catch (Exception e) {
         e.printStackTrace();
         throw new Exception(url + "interface failed" + e.toString());
      } finally {
         if (conn != null) conn.disconnect();
         if (br != null) br.close();
         if (bw != null) bw.close();
      }
      
      return resultMap;
   }
}