/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.laikan.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.bus.laikan.entity.LaikanChapterHis;
import com.jeeplus.modules.bus.laikan.service.LaikanChapterHisService;

/**
 * 来看用户购买记录Controller
 * @author zhangsc
 * @version 2018-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/laikan/laikanChapterHis")
public class LaikanChapterHisController extends BaseController {

	@Autowired
	private LaikanChapterHisService laikanChapterHisService;
	
	@ModelAttribute
	public LaikanChapterHis get(@RequestParam(required=false) String id) {
		LaikanChapterHis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = laikanChapterHisService.get(id);
		}
		if (entity == null){
			entity = new LaikanChapterHis();
		}
		return entity;
	}
	
	/**
	 * 来看用户购买记录列表页面
	 */
	@RequiresPermissions("laikan:laikanChapterHis:list")
	@RequestMapping(value = {"list", ""})
	public String list(LaikanChapterHis laikanChapterHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LaikanChapterHis> page = laikanChapterHisService.findPage(new Page<LaikanChapterHis>(request, response), laikanChapterHis); 
		model.addAttribute("page", page);
		return "bus/laikan/laikanChapterHisList";
	}

	/**
	 * 查看，增加，编辑来看用户购买记录表单页面
	 */
	@RequiresPermissions(value={"laikan:laikanChapterHis:view","laikan:laikanChapterHis:add","laikan:laikanChapterHis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(LaikanChapterHis laikanChapterHis, Model model) {
		model.addAttribute("laikanChapterHis", laikanChapterHis);
		return "bus/laikan/laikanChapterHisForm";
	}

	/**
	 * 保存来看用户购买记录
	 */
	@RequiresPermissions(value={"laikan:laikanChapterHis:add","laikan:laikanChapterHis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(LaikanChapterHis laikanChapterHis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, laikanChapterHis)){
			return form(laikanChapterHis, model);
		}
		if(!laikanChapterHis.getIsNewRecord()){//编辑表单保存
			LaikanChapterHis t = laikanChapterHisService.get(laikanChapterHis.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(laikanChapterHis, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			laikanChapterHisService.save(t);//保存
		}else{//新增表单保存
			laikanChapterHisService.save(laikanChapterHis);//保存
		}
		addMessage(redirectAttributes, "保存来看用户购买记录成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
	}
	
	/**
	 * 删除来看用户购买记录
	 */
	@RequiresPermissions("laikan:laikanChapterHis:del")
	@RequestMapping(value = "delete")
	public String delete(LaikanChapterHis laikanChapterHis, RedirectAttributes redirectAttributes) {
		laikanChapterHisService.delete(laikanChapterHis);
		addMessage(redirectAttributes, "删除来看用户购买记录成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
	}
	
	/**
	 * 批量删除来看用户购买记录
	 */
	@RequiresPermissions("laikan:laikanChapterHis:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			laikanChapterHisService.delete(laikanChapterHisService.get(id));
		}
		addMessage(redirectAttributes, "删除来看用户购买记录成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("laikan:laikanChapterHis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(LaikanChapterHis laikanChapterHis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来看用户购买记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LaikanChapterHis> page = laikanChapterHisService.findPage(new Page<LaikanChapterHis>(request, response, -1), laikanChapterHis);
    		new ExportExcel("来看用户购买记录", LaikanChapterHis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出来看用户购买记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("laikan:laikanChapterHis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LaikanChapterHis> list = ei.getDataList(LaikanChapterHis.class);
			for (LaikanChapterHis laikanChapterHis : list){
				try{
					laikanChapterHisService.save(laikanChapterHis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条来看用户购买记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条来看用户购买记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入来看用户购买记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
    }
	
	/**
	 * 下载导入来看用户购买记录数据模板
	 */
	@RequiresPermissions("laikan:laikanChapterHis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来看用户购买记录数据导入模板.xlsx";
    		List<LaikanChapterHis> list = Lists.newArrayList(); 
    		new ExportExcel("来看用户购买记录数据", LaikanChapterHis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanChapterHis/?repage";
    }
	
	
	

}