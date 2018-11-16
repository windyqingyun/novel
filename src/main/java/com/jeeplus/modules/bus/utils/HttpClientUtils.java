package com.jeeplus.modules.bus.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	private CloseableHttpClient httpClient = null;
	
	public String post(String url, String json, String encoding) throws Exception{
		String result = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Accept", "*/*");
			httpPost.addHeader("Connection", "Keep-Alive");
			httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpPost.addHeader("Content-Type", "application/json");
			
			StringEntity requestEntity = new StringEntity(json, encoding); 
			httpPost.setEntity(requestEntity);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeHttpClient();
		}
		
		
		return result;
	}
	
	public String get(String url, String encoding) throws Exception{
		String result = null;
		try {
			httpClient = getHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept", "*/*");
			httpGet.addHeader("Connection", "Keep-Alive");
			httpGet.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpGet.addHeader("Content-Type", "application/json");

			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeHttpClient();
		}
		
		
		return result;
	}
	
	public CloseableHttpClient getHttpClient(){
		if(httpClient == null){
			httpClient = HttpClients.createDefault();
		}
		return httpClient;
	}
	
	public void closeHttpClient(){
		if(httpClient != null){
			try {
				httpClient.close();
				httpClient = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
