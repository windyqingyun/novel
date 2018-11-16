package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.modules.bus.utils.HttpClientUtils;


public class Test {
	public static void main(String[] args) throws Exception {
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		String payParam = httpClientUtils.post("http://localhost:8080/nrfx_intertem/service/interface/book/add", readFileToString("C:\\Users\\Administrator\\Desktop\\3435"), "utf-8");
		System.out.println(payParam);


		ArrayList array=new ArrayList();
		array.add("1");
		array.add("2");
		System.out.println(array);
	}
	
//	public static void main(String[] args) {
//	
//	}

	public static String readFileToString(String filePath) throws IOException{
		StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
	}
	
	public static String parseAddOrUpdateParam(String para) throws UnsupportedEncodingException{
		if(para.startsWith("null")){
			para = para.replace("null", "");
		}
		if(para.startsWith("para=")){
			para = para.replace("para=", "");
		}
		
		return JSONUtils.parse(para).toString();
	}
	
}
