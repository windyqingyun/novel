package com.jeeplus.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeeplus.modules.bus.entity.NodArchive;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

public class UploadIfyUtils {
	
	/**
	 * 文件上传 支持多文件上传
	 * @param request
	 * @param response
	 * @return NodArchive 为附件对象
	 * @throws IOException
	 */
	public List<NodArchive> upload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<NodArchive> nodList = new ArrayList<NodArchive>(); 
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//获取前台传值
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {   
			NodArchive nodArchive = new NodArchive();
			// 上传文件名      
			MultipartFile mf = entity.getValue();    
			fileName = mf.getOriginalFilename(); 
			//过滤掉html等代码
			fileName = StringEscapeUtils.escapeHtml4(fileName);
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			String currentfileName =  fileName.substring(0,fileName.lastIndexOf(".")).toLowerCase();
			//设置扩展名
			nodArchive.setExtension(fileExt);
			
			Office office = UserUtils.getOffce();
			nodArchive.setOfficeId(office == null ? null : office.getId());
			
			//设置内容
			nodArchive.setContent(mf.getBytes());
			//设置文件名
			nodArchive.setFileName(currentfileName);
			nodArchive.setRemarks(request.getParameter("remarks"));
			nodList.add(nodArchive);
		}   
		return nodList;  
    }
	
	/**
	 * 根据文件路径文件转换成附近对象
	 */
	@Deprecated
	public NodArchive fliePath2NodArchive(String fileName){
		if(StringUtils.isNotBlank(fileName)){
			NodArchive archive = new NodArchive();
			archive.setFileName(new File(fileName).getName());
			archive.setContent(file2BetyArray(fileName));
			archive.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase());
			
			Office office = UserUtils.getOffce();
			archive.setOfficeId(office == null ? null : office.getId());
			return archive;
		}
		return null;
		
	}
	
	/**
	 * 根据文件路径把文件转换成2进制数组
	 */
	@Deprecated
    public static byte[] file2BetyArray(String filename){  
    	File file = new File(filename);
        if (!file.exists()) {
        	return null;
        }
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
        	stream = new FileInputStream(file);
            out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
            	out.write(b, 0, n);
            }
            return out.toByteArray();// 此方法大文件OutOfMemory
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            try {
                stream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;  
    }  
  
}