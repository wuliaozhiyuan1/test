package com.wuliaozhiyuan.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuliaozhiyuan.annotation.LogAnnotation;
import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.bean.system.vo.DictionaryVo;
import com.wuliaozhiyuan.service.system.DictionaryService;
import com.wuliaozhiyuan.util.Page;
import com.wuliaozhiyuan.util.PageData;
import com.wuliaozhiyuan.util.Result;
import com.wuliaozhiyuan.util.Tools;
import com.wuliaozhiyuan.validate.DictionarySaveValidate;
import com.wuliaozhiyuan.validate.DictionaryUpdateValidate;

/**
 * 字典Controller
 * @author wuliaozhiyuan
 *
 */
@Controller
@RequestMapping("dictionary")
public class DictionaryController {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	/**
	 * 访问字典表树页面
	 * @author wuliaozhiyuan
	 * @return
	 */
	@RequestMapping("/toDictionaryTree")
	@RequiresPermissions("dictionaryManager:view")
	@LogAnnotation("访问字典表树页面")
	public String toDictionaryTree(){
		String parentIdParam = "parentId";
		Long  parentId = Tools.getPageData().getLong(parentIdParam);
		if(parentId == null){
			Tools.getPageData().put(parentIdParam, 0);
		}
		return "system/dictionary/dictionary_ztree";
	}
	/**
	 * 获取子字典，通过父类字典
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSubDictionary")
	@ResponseBody
	@RequiresPermissions("dictionaryManager:view")
	@LogAnnotation("获取子字典")
	public Object getSubDictionary() throws Exception{
		PageData pageData = Tools.getPageData();
		return dictionaryService.listDictionaryByParentId(pageData);
	}
	/**
	 * 查询字典列表
	 * @author wuliaozhiyuan
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listDictionary")
	@RequiresPermissions("dictionaryManager:view")
	@LogAnnotation("获取字典列表")
	public String listDictionary(Page page) throws Exception{
		List<Dictionary> listDictionary = dictionaryService.listDictionary(page);
		PageData pd = page.getPd();
		String dictionaryListParam = "dictionaryList";
		pd.put(dictionaryListParam, listDictionary);
		String pageParam = "page";
		pd.put(pageParam, page);
		String parentIdParam = "parentId";
		//获取父类的父类id，用于页面点击返回用
		if(pd.getLong(parentIdParam) != null && pd.getLong(parentIdParam) != 0){
			Dictionary dictionary = dictionaryService.getDictionaryById(pd.getLong(parentIdParam));
			pd.put("grandpaId", dictionary.getParentId());
		}
		return "system/dictionary/dictionary_list";
	}
	
	/**
	 * 访问新增页面
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@LogAnnotation("访问新增字典页面")
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("dictionaryManager:add")
	public String goAdd()throws Exception{
		PageData pd = Tools.getPageData();
		String parentIdParam = "parentId";
		if(pd.getLong(parentIdParam) != null && pd.getLong(parentIdParam) != 0){
			//获取上级字典的信息，用于新增页面展示上级字典名， 保存时赋予上级字典id，上级字典code，
			Dictionary dictionary = dictionaryService.getDictionaryById(pd.getLong(parentIdParam));
			String parentDictionaryParam = "parentDictionary";
			pd.put(parentDictionaryParam, dictionary);
		}
		return "system/dictionary/dictionary_add";
	}	
	/**
	 * code是否已存在
	 * @return Result
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@RequestMapping("/hasCode")
	@LogAnnotation("查询code是否已经存在")
	@ResponseBody
	public Result hasCode(){
		Integer num = dictionaryService.countCodeNum(Tools.getPageData());
		if(num == 0){
			return Result.success();
		}
		return Result.build("code已经存在", null, false);
	}
	/**
	 * 保存数据字典
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@RequestMapping("/save")
	@LogAnnotation("保存数据字典")
	@RequiresPermissions("dictionaryManager:add")
	public String save(@Validated({DictionarySaveValidate.class}) DictionaryVo dictionaryVo,  BindingResult result) throws Exception{
		dictionaryService.save(dictionaryVo);
		return "save_result";
	}
	
	/**
	 * 访问修改页面
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@LogAnnotation("访问修改字典页面")
	@RequestMapping(value="/goUpdate")
	@RequiresPermissions("dictionaryManager:update")
	public String goUpdate()throws Exception{
		PageData pd = Tools.getPageData();
		String idParam = "id";
		Dictionary dictionary = dictionaryService.getDictionaryById(pd.getLong(idParam));
		String dictionaryParam = "dictionary";
		pd.put(dictionaryParam, dictionary);
		return "system/dictionary/dictionary_update";
	}	
	/**
	 * 保存修改数据字典
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@RequestMapping("/update")
	@LogAnnotation("保存修改数据字典")
	@RequiresPermissions("dictionaryManager:update")
	public String update(@Validated({DictionaryUpdateValidate.class}) Dictionary dictionary,  BindingResult result) throws Exception{
		dictionaryService.update(dictionary);
		return "save_result";
	}
	
	
	/**
	 * 删除数据字典
	 * @return
	 * @throws Exception
	 * @author shuyy
	 * @date 2017年11月25日
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@LogAnnotation("删除数据字典")
	@RequiresPermissions("dictionaryManager:delete")
	public Object delete() throws Exception{
		PageData pageData = Tools.getPageData();
		Long id = pageData.getLong("id");
		Long parentId = pageData.getLong("parentId");
		if(dictionaryService.delete(id, parentId)){
			return Result.success();
		}
		return Result.build("有子数据字典，不能删除", null, false);
	}
	
	/**
	 * 查询数据字典，通过parentCode，用于填充下拉框
	 * @return 格式：[{code:code, name:name}]
	 * @author shuyy
	 * @date 2017年11月26日
	 */
	@RequestMapping("/listDictionaryByParentCode")
	@ResponseBody
	@LogAnnotation("查询字典表，通过父字典code")
	public Object listDictionaryByParentCode(){
		String parentCode = Tools.getPageData().getString("parentCode");
		return Result.success(dictionaryService.listDictionaryByParentCode(parentCode));
	}
	
	
}
