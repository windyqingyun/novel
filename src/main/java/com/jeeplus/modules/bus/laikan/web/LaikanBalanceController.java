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
import com.jeeplus.modules.bus.laikan.entity.LaikanBalance;
import com.jeeplus.modules.bus.laikan.service.LaikanBalanceService;

/**
 * 来看用户余额Controller
 * @author zhangsc
 * @version 2018-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/laikan/laikanBalance")
public class LaikanBalanceController extends BaseController {

	@Autowired
	private LaikanBalanceService laikanBalanceService;
	
	@ModelAttribute
	public LaikanBalance get(@RequestParam(required=false) String id) {
		LaikanBalance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = laikanBalanceService.get(id);
		}
		if (entity == null){
			entity = new LaikanBalance();
		}
		return entity;
	}
	
	/**
	 * 来看用户余额列表页面
	 */
	@RequiresPermissions("laikan:laikanBalance:list")
	@RequestMapping(value = {"list", ""})
	public String list(LaikanBalance laikanBalance, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LaikanBalance> page = laikanBalanceService.findPage(new Page<LaikanBalance>(request, response), laikanBalance); 
		model.addAttribute("page", page);
		return "bus/laikan/laikanBalanceList";
	}

	/**
	 * 查看，增加，编辑来看用户余额表单页面
	 */
	@RequiresPermissions(value={"laikan:laikanBalance:view","laikan:laikanBalance:add","laikan:laikanBalance:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(LaikanBalance laikanBalance, Model model) {
		model.addAttribute("laikanBalance", laikanBalance);
		return "bus/laikan/laikanBalanceForm";
	}

	/**
	 * 保存来看用户余额
	 */
	@RequiresPermissions(value={"laikan:laikanBalance:add","laikan:laikanBalance:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(LaikanBalance laikanBalance, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, laikanBalance)){
			return form(laikanBalance, model);
		}
		if(!laikanBalance.getIsNewRecord()){//编辑表单保存
			LaikanBalance t = laikanBalanceService.get(laikanBalance.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(laikanBalance, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			laikanBalanceService.save(t);//保存
		}else{//新增表单保存
			laikanBalanceService.save(laikanBalance);//保存
		}
		addMessage(redirectAttributes, "保存来看用户余额成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
	}
	
	/**
	 * 删除来看用户余额
	 */
	@RequiresPermissions("laikan:laikanBalance:del")
	@RequestMapping(value = "delete")
	public String delete(LaikanBalance laikanBalance, RedirectAttributes redirectAttributes) {
		laikanBalanceService.delete(laikanBalance);
		addMessage(redirectAttributes, "删除来看用户余额成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
	}
	
	/**
	 * 批量删除来看用户余额
	 */
	@RequiresPermissions("laikan:laikanBalance:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			laikanBalanceService.delete(laikanBalanceService.get(id));
		}
		addMessage(redirectAttributes, "删除来看用户余额成功");
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("laikan:laikanBalance:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(LaikanBalance laikanBalance, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来看用户余额"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LaikanBalance> page = laikanBalanceService.findPage(new Page<LaikanBalance>(request, response, -1), laikanBalance);
    		new ExportExcel("来看用户余额", LaikanBalance.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出来看用户余额记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("laikan:laikanBalance:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LaikanBalance> list = ei.getDataList(LaikanBalance.class);
			for (LaikanBalance laikanBalance : list){
				try{
					laikanBalanceService.save(laikanBalance);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条来看用户余额记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条来看用户余额记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入来看用户余额失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
    }
	
	/**
	 * 下载导入来看用户余额数据模板
	 */
	@RequiresPermissions("laikan:laikanBalance:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "来看用户余额数据导入模板.xlsx";
    		List<LaikanBalance> list = Lists.newArrayList(); 
    		new ExportExcel("来看用户余额数据", LaikanBalance.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/laikan/laikanBalance/?repage";
    }
	
	
	

}