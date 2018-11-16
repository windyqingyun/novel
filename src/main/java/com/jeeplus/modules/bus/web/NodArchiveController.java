/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.UploadIfyUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.NodArchive;
import com.jeeplus.modules.bus.service.NodArchiveService;

/**
 * 上传附件controller
 * @author zhangsc
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "${adminPath}/bus/nodArchive")
public class NodArchiveController extends BaseController {
	
	@Autowired
	private NodArchiveService nodArchiveService;
	
	/**
	   * 下载附件
	   * 
	   * @param nodArchiveId 
	   * @return
	   */
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void download(@RequestParam(required=true) String id, HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out = null;
		try {
			response.reset();
			NodArchive entity = new NodArchive();
			entity.setId(id);
			//通过验证地域的get方式获取附件对象
			NodArchive nodArchive = nodArchiveService.getByValidate(entity);
			//直接由浏览器打开的文件的扩展名列表
			List<String> showList = new ArrayList<String>();
			showList.add("jpg");
			showList.add("jpeg");
			showList.add("png");
			showList.add("gif");
			showList.add("bmp");
			if(nodArchive != null){
				//如果扩展名不是空,并且扩展在showList中
				if(StringUtils.isNotEmpty(nodArchive.getExtension()) && showList.contains(nodArchive.getExtension().toLowerCase())){
					// 设置输出的格式
					response.setContentType("image/png");
				}else{
					response.setContentType("application/OCTET-STREAM;charset=UTF-8");
					//response.setContentType("multipart/form-data");
					response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode((nodArchive.getFileName()+"."+nodArchive.getExtension()),"UTF-8"));
				}
				//获取blob字段
				byte[] contents = nodArchive.getContent();
				out = response.getOutputStream();
				//写到输出流
				out.write(contents);
				out.flush();
			}else{
				//如果获取不到
				throw new AuthenticationException("msg:您的权限不足或文件不存在!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthenticationException("msg:您的权限不足或文件不存在!");
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new AuthenticationException("msg:系统异常!");
				}
			}
			
		}
	}
	
	
	
	@ResponseBody
	@RequiresPermissions("user")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public  String upload(HttpServletRequest request, HttpServletResponse response, Model model) {
		//默认上传错误.
		String message = "{"+Global.RESULT_CODE+":"+Global.RESULT_CODE_FAIL+","+Global.RESULT_MSG+":'上传失败!读取异常!'}";
		JsonMapper mapper = new JsonMapper();  
		ObjectNode json = mapper.createObjectNode();  
		try {
			List<String> idList = new ArrayList<String>();
			//根据表单获取附件对象
			List<NodArchive> nodArchiveList = new UploadIfyUtils().upload(request, response);
			//循环保存附件
			for(NodArchive nodArchive : nodArchiveList){
				if(nodArchive.getContent().length > Long.parseLong(Global.getConfig("web.maxUploadSize"))){
					json.put(Global.RESULT_CODE, Global.RESULT_CODE_FAIL);
					json.put(Global.RESULT_MSG, "文件大小超过限制!");
					message = mapper.writeValueAsString (json);
					return message;
				}
				nodArchiveService.save(nodArchive);
				idList.add(nodArchive.getId());
				nodArchive.setContent(null);
			}
			
			json.put(Global.RESULT_CODE, Global.RESULT_CODE_SUCCESS);
			json.put(Global.RESULT_MSG, mapper.writeValueAsString(nodArchiveList));
			message = mapper.writeValueAsString (json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}
}