package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.wuliaozhiyuan.validate.DictionarySaveValidate;
import com.wuliaozhiyuan.validate.DictionaryUpdateValidate;

/**
 * 字典表
 * @author wuliaozhiyuan
 *
 */
@Entity
public class Dictionary implements Serializable{
	private static final long serialVersionUID = 20827204390581194L;
	
	/**主键id*/
	@Id@GeneratedValue
	@NotNull(groups={DictionaryUpdateValidate.class}, message="id字段不能为空")
	private Long id;
	/**名称*/
	@NotBlank(message="名称不能为空", groups={DictionarySaveValidate.class, DictionaryUpdateValidate.class})
	@Size(min = 1, max = 50, message="名称长度必须为1到50之间", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	@Column(nullable=false)
	private String name;
	/**英文名*/
	@NotBlank(message="英文名称不能为空", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	@Size(min = 1, max = 50, message="英文名称长度必须为1到50之间", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	private String englishName;
	/**编码*/
	@NotBlank(message="编码不能为空", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	@Size(min = 1, max = 32, message="编码长度必须为1到32之间", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	@Column(nullable=false)
	private String code;
	/**排序*/
	@NotNull(message="排序编号不能为空", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	@DecimalMax(groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class}, value="99", message="排序编号最大只能是99")
	private Integer orders;
	
	/**上级字典*/
	@NotNull(message="上级字典不能为空", groups={DictionarySaveValidate.class})
	@Digits(groups={DictionarySaveValidate.class}, integer=2, fraction=0)
	private Long parentId;
	/**备注*/
	@Column(nullable=false)
	@Size(max=255, message="备注字数最大只能是255个", groups={DictionarySaveValidate.class,DictionaryUpdateValidate.class})
	private String remark;
	/**树路径图：/0/1.方便查找子集节点*/
	@NotBlank(message="tree路径不能为空", groups={DictionarySaveValidate.class})
	@Size(min = 1, max = 255, message="tree路径长度必须为1到255之间", groups={DictionarySaveValidate.class})
	private String treePath;
	/**是否是父级节点*/
	private Boolean isParent;
	/**父字典编码*/
	private String parentCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTreePath() {
		return treePath;
	}
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	
	

}
