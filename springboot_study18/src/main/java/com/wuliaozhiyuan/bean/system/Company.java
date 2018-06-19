package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import com.wuliaozhiyuan.validate.CompanySaveValidate;
import com.wuliaozhiyuan.validate.DictionarySaveValidate;
import com.wuliaozhiyuan.validate.DictionaryUpdateValidate;
import com.wuliaozhiyuan.validate.SysUserSaveValidate;

/**
 * 企业表
 * @author wuliaozhiyuan
 *
 */
@Entity
public class Company implements Serializable{
	private static final long serialVersionUID = 2851813469137161092L;
	/**主键id*/
	@Id@GeneratedValue
	@NotNull(message="企业id不能为空", groups={SysUserSaveValidate.class})
	@Digits(groups={SysUserSaveValidate.class}, integer=2, fraction=0)
	private Long id;
	/**企业名*/
	@NotBlank(message="企业名称不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 50, message="企业名称长度必须为1到50之间", groups={CompanySaveValidate.class})
	@Column(nullable=false,unique=true)
	private String companyName;
	/**企业名简称*/
	@NotBlank(message="企业简称不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 50, message="企业简称长度必须为1到50之间", groups={CompanySaveValidate.class})
	@Column(nullable=false,unique=true)
	private String shortName;
	/**企业类型*/
	@Column(nullable=false)
	@NotBlank(message="企业类型不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 20, message="企业类型长度必须为1到20之间", groups={CompanySaveValidate.class})
	private String companyTypeCode;
	/**企业类型名称*/
	private String companyTypeName;
	/**企业社会信用编码*/
	@Size(min = 1, max = 255, message="企业社会信用编码长度必须为1到255之间", groups={CompanySaveValidate.class})
	private String socialCreditCode;
	/**所属地区编码*/
	@Column(nullable=false)
	private String areaCode;
	/**所属地区名称*/
	@NotBlank(message="所属地区名称不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 10, message="所属地区名称必须为1到10之间", groups={CompanySaveValidate.class})
	@Column(nullable=false)
	private String areaName;
	/**地址*/
	@Column(nullable=false)
	@NotBlank(message="地址不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 255, message="地址长度必须为1到255	之间", groups={CompanySaveValidate.class})
	private String address;
	/**邮编*/
	@NotNull(message="邮编不能为空", groups={CompanySaveValidate.class})
	@Pattern(regexp="^[1-9][0-9]{5}$", groups={CompanySaveValidate.class}, message="邮编格式不正确")
	private String postCode;
	/**email*/
	@Email(message="邮箱格式不正确", groups={CompanySaveValidate.class})
	@NotNull(message="邮箱不能为空", groups={CompanySaveValidate.class})
	private String email;
	/**传真*/
	@Pattern(regexp="^(\\d{3,4}-)?\\d{7,8}$", groups={CompanySaveValidate.class}, message="传真号格式不正确")
	private String fax;
	/**公司网址*/
	@Size(min = 1, max = 255, message="公司网址长度必须为1到255	之间", groups={CompanySaveValidate.class})
	private String webSite;
	/**企业logo*/
	 @Transient
	private MultipartFile logo;
	/**公司Logo地址*/
	private String logoUrl;
	/**联系人*/
	@NotBlank(message="联系人不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 50, message="联系人长度必须为1到20之间", groups={CompanySaveValidate.class})
	@Column(nullable=false)
	private String contactMan;
	/**联系人手机号*/
	@Column(nullable=false)
	@NotNull(message="联系人手机号不能为空", groups={CompanySaveValidate.class})
	@Pattern(regexp="^0?1[3|4|5|7|8][0-9]\\d{8}$", groups={CompanySaveValidate.class}, message="联系人手机号格式不正确")
	private String contactPhone;
	/**创建人账号*/
	@Column(nullable=false)
	private String createUser;
	/**创建时间*/
	@Column(nullable=false)
	private Date createDate;
	/**上级企业*/
	@Column(nullable=false)
	private Long parentId;
	/**上级所有企业路径：例如：/0/1/ */
	private String parentIds;
	/**纬度*/
	@NotNull(message="经度不能为空", groups={CompanySaveValidate.class})
	private Float latitude;
	/**经度*/
	@NotNull(message="纬度不能为空", groups={CompanySaveValidate.class})
	private Float longitude;
	/**排序编码*/
	/**排序*/
	@NotNull(message="排序不能为空", groups={CompanySaveValidate.class})
	@DecimalMax(groups={CompanySaveValidate.class}, value="99", message="排序最大只能是99")
	@Column(nullable=false)
	private Integer sortCode;
	/**状态*/
	@Column(nullable=false)
	@NotBlank(message="状态不能为空", groups={CompanySaveValidate.class})
	@Size(min = 1, max = 3, message="状态长度必须为1到20之间", groups={CompanySaveValidate.class})
	private String status;
	
	@Column(nullable=false)
	private Boolean isParent;
	/**企业树路径，例：/0/1/*/
	@Size(min = 0, max = 255, message="公司树路径长度必须为1到255之间", groups={CompanySaveValidate.class})
	private String treePath;
	/**企业简介*/
	@Size(min = 1, max = 2000, message="企业简介长度必须为1到2000之间", groups={CompanySaveValidate.class})
	private String companyInfo;
	
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getCompanyTypeCode() {
		return companyTypeCode;
	}
	public void setCompanyTypeCode(String companyTypeCode) {
		this.companyTypeCode = companyTypeCode;
	}
	public String getCompanyTypeName() {
		return companyTypeName;
	}
	public void setCompanyTypeName(String companyTypeName) {
		this.companyTypeName = companyTypeName;
	}
	public String getSocialCreditCode() {
		return socialCreditCode;
	}
	public void setSocialCreditCode(String socialCreditCode) {
		this.socialCreditCode = socialCreditCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getContactMan() {
		return contactMan;
	}
	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public Integer getSortCode() {
		return sortCode;
	}
	public void setSortCode(Integer sortCode) {
		this.sortCode = sortCode;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompanyInfo() {
		return companyInfo;
	}
	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getTreePath() {
		return treePath;
	}
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public MultipartFile getLogo() {
		return logo;
	}
	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}
	
	
}
