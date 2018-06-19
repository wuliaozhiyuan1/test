package com.wuliaozhiyuan.bean.system.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.wuliaozhiyuan.bean.system.Dictionary;
import com.wuliaozhiyuan.validate.DictionarySaveValidate;
/**
 * Dictionary包装类
 * @author shuyy
 * @date 2017年11月25日
 */
public class DictionaryVo {
	/**字典对象*/
	@Valid
	private Dictionary dictionary;
	/**父字典是否是有字典类，用于保存字典的时候，根据这个字段判断是否需要更新父字典的isParent字段*/
	@NotNull(groups={DictionarySaveValidate.class}, message="父字典是否有子字典字段不能为空")
	private Boolean parentDictionaryIsParent;
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	public Boolean getParentDictionaryIsParent() {
		return parentDictionaryIsParent;
	}
	public void setParentDictionaryIsParent(Boolean parentDictionaryIsParent) {
		this.parentDictionaryIsParent = parentDictionaryIsParent;
	}
	
	
}
