package com.jeeplus.modules.sys.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping(value = "/service/interface/sys/uploadFile")
public class WebUploadFileController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 用户岗位关系列表页面
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"testUpload"})
	public String testUpload(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/testUpload";
	}
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping("upload")
    public String upload(HttpServletRequest request,HttpServletResponse response){
        MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest)request;
        Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象
    	String basePath = Global.getConfig("userfiles.basedir")+"/userfiles/";
    	String path = DateUtils.getDate();
        File pathFile = new File(basePath+path);
        if(!pathFile.exists() && !pathFile.isDirectory()){
        	pathFile.mkdirs();
        }
        String uploadUrl = "";
        String fileName = "";
        for (String  key : files.keySet()) {  
        	MultipartFile file = files.get(key);
        	fileName = file.getOriginalFilename();
            File newFile=new File(basePath+path+File.separatorChar+fileName);
            try {
				file.transferTo(newFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
        try {
        	uploadUrl =  "/sqsw/userfiles/" +path+"/"+java.net.URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return uploadUrl;
     }
	@ResponseBody
	@RequestMapping("download")
    public void download(HttpServletRequest request,HttpServletResponse response){
       String fileUrl = request.getParameter("url");
       if (StringUtils.isBlank(fileUrl)) {
    	   logger.warn("文件找不到:"+fileUrl);
    	   return;
       }
       try {
    	   String basePath = Global.getConfig("userfiles.basedir");
			String contextPath = basePath+fileUrl.substring(6,fileUrl.length());
			// path是指欲下载的文件的路径。
			File file = new File(contextPath);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(contextPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header00000000000
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			logger.warn("文件找不到:"+fileUrl);
		}
   }
	
}
