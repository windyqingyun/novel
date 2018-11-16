/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.UserBookshelf;
import com.jeeplus.modules.bus.service.UserBookshelfService;

/**
 * 用户书架Controller
 * @author lzp
 * @version 2018-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/userBookshelf")
public class UserBookshelfController extends BaseController {

	@Autowired
	private UserBookshelfService userBookshelfService;
	
	@ModelAttribute
	public UserBookshelf get(@RequestParam(required=false) String id) {
		UserBookshelf entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userBookshelfService.get(id);
		}
		if (entity == null){
			entity = new UserBookshelf();
		}
		return entity;
	}
	
	/**
	 * 书架列表页面
	 */
	@RequiresPermissions("bus:userBookshelf:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserBookshelf userBookshelf, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserBookshelf> page = userBookshelfService.findPage(new Page<UserBookshelf>(request, response), userBookshelf); 
		model.addAttribute("page", page);
		return "modules/bus/userBookshelfList";
	}

	/**
	 * 查看，增加，编辑书架表单页面
	 */
	@RequiresPermissions(value={"bus:userBookshelf:view","bus:userBookshelf:add","bus:userBookshelf:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserBookshelf userBookshelf, Model model) {
		model.addAttribute("userBookshelf", userBookshelf);
		return "modules/bus/userBookshelfForm";
	}

	/**
	 * 保存书架
	 */
	@RequiresPermissions(value={"bus:userBookshelf:add","bus:userBookshelf:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserBookshelf userBookshelf, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userBookshelf)){
			return form(userBookshelf, model);
		}
		if(!userBookshelf.getIsNewRecord()){//编辑表单保存
			UserBookshelf t = userBookshelfService.get(userBookshelf.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userBookshelf, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userBookshelfService.save(t);//保存
		}else{//新增表单保存
			userBookshelfService.save(userBookshelf);//保存
		}
		addMessage(redirectAttributes, "保存书架成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
	}
	
	/**
	 * 删除书架
	 */
	@RequiresPermissions("bus:userBookshelf:del")
	@RequestMapping(value = "delete")
	public String delete(UserBookshelf userBookshelf, RedirectAttributes redirectAttributes) {
		userBookshelfService.delete(userBookshelf);
		addMessage(redirectAttributes, "删除书架成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
	}
	
	/**
	 * 批量删除书架
	 */
	@RequiresPermissions("bus:userBookshelf:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userBookshelfService.delete(userBookshelfService.get(id));
		}
		addMessage(redirectAttributes, "删除书架成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:userBookshelf:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserBookshelf userBookshelf, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "书架"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserBookshelf> page = userBookshelfService.findPage(new Page<UserBookshelf>(request, response, -1), userBookshelf);
    		new ExportExcel("书架", UserBookshelf.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出书架记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:userBookshelf:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserBookshelf> list = ei.getDataList(UserBookshelf.class);
			for (UserBookshelf userBookshelf : list){
				try{
					userBookshelfService.save(userBookshelf);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条书架记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条书架记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入书架失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
    }
	
	/**
	 * 下载导入书架数据模板
	 */
	@RequiresPermissions("bus:userBookshelf:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "书架数据导入模板.xlsx";
    		List<UserBookshelf> list = Lists.newArrayList(); 
    		new ExportExcel("书架数据", UserBookshelf.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userBookshelf/?repage";
    }
	
	
	

}