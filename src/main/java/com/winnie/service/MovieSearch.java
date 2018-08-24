package com.winnie.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.winnie.domain.Movie;
import com.winnie.domain.MovieList;
import com.winnie.repository.MovieRepository;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;





/**
* Created by彭文钰
* 2018年5月19日 上午12:03:01
*/
@Component
public class MovieSearch {
		
	/** 
     * get请求 
     * @return 
     */  
	public String doGet(String url) {  
        try {  
        	CloseableHttpClient client = HttpClients.createDefault();  
            //发送get请求  
            HttpGet request = new HttpGet(url);  
            HttpResponse response = client.execute(request);  
   
            /**请求发送成功，并得到响应**/  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                /**读取服务器返回过来的json字符串数据**/  
                String strResult = EntityUtils.toString(response.getEntity());  
                  
                return strResult;  
            }  
        }   
        catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return null;  
    } 
    
    public List<MovieList> toList(String jsonString) throws JSONException  {  
    	
    	if(jsonString == null) {
    		return null;
    	}
  	  
        JSONObject jsonObject = JSONObject.fromObject(jsonString);  
          
        List<MovieList> mList = new ArrayList<MovieList>();
        JSONArray subjects = jsonObject.getJSONArray("subjects");
        
        for(int i = 0; i < subjects.size(); i++) {
        	MovieList m = new MovieList();
        	JSONObject subObject=subjects.getJSONObject(i);
        	m.setDid(subObject.get("id").toString());
        	m.setYear(subObject.get("year").toString());
        	m.setMovieName(subObject.get("title").toString());
        	String genres = subObject.get("genres").toString();
        	genres = genres.replaceAll("\"", "");
        	genres = genres.replace("[", "");
        	genres = genres.replace("]", "");
        	if(genres.length()==0) {
        		genres="无";
        	}
        	m.setGenres(genres);
        	
        	
        	JSONArray castArray = subObject.getJSONArray("casts");
        	StringBuilder cBuilder = new StringBuilder();
        	for(int j=0 ; j< castArray.size();j++) {
        		JSONObject cast = castArray.getJSONObject(j);
        		cBuilder.append(cast.get("name").toString()+",");
        	}
        	String cline = null;
        	if(cBuilder.toString().endsWith(",")) {
        	    cline = cBuilder.substring(0, cBuilder.length()-1).toString();
        	}else {
        		cline = "无";
        	}       	
        	m.setCasts(cline);
        	
        	JSONArray direArray = subObject.getJSONArray("directors");
        	String director = null;
        	if(direArray.size()>0) {
        		director = direArray.getJSONObject(0).get("name").toString();
        	}else{
        		director="无";
        	}
        	m.setDirector(director);
        	
        	JSONObject imgObject = subObject.getJSONObject("images");
        	m.setImg(imgObject.get("small").toString());
        	
        	mList.add(m);
        }
          
        return mList;  

    } 
    
    public static Movie toInfo(String jsonString) throws JSONException  {  
    	  
    	Movie info = new Movie();
    	
    	if(jsonString == null) {
    		return info;
    	}
 
    	//System.out.print(jsonString);
        JSONObject jsonObject = JSONObject.fromObject(jsonString);  
        //豆瓣ID
    	info.setDid(jsonObject.get("id").toString());
    	//电影名
    	info.setName(jsonObject.get("title").toString());
        //简介
        String summary = jsonObject.get("summary").toString();
        if(summary.length()>0)
        	summary = summary.substring(0,summary.length()-3);
        info.setSummary(summary);
        //类型
        String genres = jsonObject.get("genres").toString();
    	genres = genres.replaceAll("\"", "");
    	genres = genres.replace("[", "");
    	genres = genres.replace("]", "");
    	if(genres.length()==0) {
    		genres="无";
    	}
    	info.setGenres(genres);
    	//年份
    	info.setYear(jsonObject.get("year").toString());
    	//演员表
    	JSONArray castArray = jsonObject.getJSONArray("casts");
    	StringBuilder cBuilder = new StringBuilder();
    	for(int j=0 ; j< castArray.size();j++) {
    		JSONObject cast = castArray.getJSONObject(j);
    		cBuilder.append(cast.get("name").toString()+"/");
    	}
    	String cline = null;
    	if(cBuilder.toString().endsWith("/")) {
    	    cline = cBuilder.substring(0, cBuilder.length()-1).toString();
    	}else {
    		cline = "无";
    	}       	
    	info.setCasts(cline);
    	//导演
    	JSONArray direArray = jsonObject.getJSONArray("directors");
    	String director = null;
    	if(direArray.size()>0) {
    		director = direArray.getJSONObject(0).get("name").toString();
    	}else{
    		director="无";
    	}
    	info.setDirector(director);
    	//img
    	JSONObject imgObject = jsonObject.getJSONObject("images");
    	info.setImg(imgObject.get("large").toString());
        //评分
        JSONObject rateObject = jsonObject.getJSONObject("rating");
        info.setRate(rateObject.get("average").toString());
        
        return info;
    }
}
