package com.wuliaozhiyuan.bean.system;

import java.io.Serializable;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * 企业信息表
 * @author wuliaozhiyuan
 *
 */
@Entity
public class CompanyIntroduce implements Serializable{
	private static final long serialVersionUID = 9177005476418997432L;
	/**主键id*/
	@Id@GeneratedValue
	private Long id;
	/**外键引用的企业*/
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="companyId",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
	private Company company;
	/**企业介绍，大文本*/
	private String companyIntroduce;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getCompanyIntroduce() {
		return companyIntroduce;
	}
	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}
	
	
}
