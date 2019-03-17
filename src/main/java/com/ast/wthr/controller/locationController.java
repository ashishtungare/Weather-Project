package com.ast.wthr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class locationController {
    private static final String AccuKey = "hoArfRosT1215";
    
    @RequestMapping("/locationinfo")
    @ResponseBody
    public  String locationinfo(@RequestParam String zipcode) { 
        Map retMap = getAccuWeatherData(zipcode, AccuKey);
        if(retMap == null || retMap.isEmpty()){
            return "";
        }     
        JSONObject retjson = new JSONObject(retMap); 
        return retjson.toString();
    }
	
    @RequestMapping("/")
    public String index( ) {
	return "index";
    }
        
    private Map getAccuWeatherData(String zipcode, String appkey){
        Map retMap = new HashMap<String, String>();
        
        // form URL to execute
        String s1 = "http://apidev.accuweather.com/locations/v1/postalcodes/search?q=";
        String s2 = "&apiKey=";
        String surl = s1 + zipcode + s2 + appkey;

        try{
            // execute URL
            String sresp = "";
            RestTemplate restTemplate = new RestTemplate();
            sresp = restTemplate.getForObject(surl, String.class);
        
            // form map from json
            sresp = sresp.substring(1).substring(0, sresp.length() - 1);
        
            JSONObject jsonret = new JSONObject(sresp);
            Map<String, Object> map = new HashMap<String, Object>();
            if(jsonret != JSONObject.NULL) {
                map = toMap(jsonret);
            }else{
                return null;
            }
        
            // return if country is not US
            Map tmpMap = (Map)map.get("AdministrativeArea");
            String strCountry = (String)tmpMap.get("CountryID");
            if(!strCountry.equals("US")){
                return null;
            }
        
            // form return map
            String strKey = (String)map.get("Key");
            String strCity = (String)map.get("LocalizedName");
            String strState = (String)tmpMap.get("ID");
            String strTemperature = getAccuWeatherTemperature(strKey, appkey);      
            retMap.put("CITY", strCity);
            retMap.put("STATE", strState);
            retMap.put("ZIP", zipcode);
            retMap.put("COUNTRY", strCountry);
            retMap.put("TEMPERATURE", strTemperature);
        }catch(Exception e){
            return null;
        }
        
        return retMap;
    }
    
    private String getAccuWeatherTemperature(String skey, String appkey){
        String sret = "";
        
        // form URL to execute
        String s1 = "http://apidev.accuweather.com/currentconditions/v1/";
        String s2 = ".json?language=en";
        String s3 = "&apiKey=";
        String surl = s1 + skey + s2 + s3 + appkey;

        try{
            // execute URL
            String sresp = "";
            RestTemplate restTemplate = new RestTemplate();
            try {
                sresp = restTemplate.getForObject(surl, String.class);
            } catch (Exception e) {
                return sret;
            }

            // form maps
            sresp = sresp.substring(1).substring(0, sresp.length() - 1);
            JSONObject jsonret = new JSONObject(sresp);
            Map<String, Object> map = new HashMap<String, Object>();
            if (jsonret != JSONObject.NULL) {
                map = toMap(jsonret);
            } else {
                return null;
            }

            // get temperature
            Map tmpMap = (Map) map.get("Temperature");
            tmpMap = (Map) tmpMap.get("Imperial");
            sret = tmpMap.get("Value").toString();
            sret += " " + (String) tmpMap.get("Unit");
        }catch(Exception e){
            return null;
        }    
        
        // return
        return sret;
    }
    
    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }  
    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
