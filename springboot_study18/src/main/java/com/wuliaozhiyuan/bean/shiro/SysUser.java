package com.wuliaozhiyuan.bean.shiro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.wuliaozhiyuan.bean.system.Company;
import com.wuliaozhiyuan.validate.SysUserSaveValidate;  
   
/** 
 * 用户信息. 
 * @author Angel(QQ:412887952) 
 * @version v.0.1 
 */  
@Entity  
public class SysUser implements Serializable{  
	private static final long serialVersionUID = 4637397409072673438L;
	/**用户id;*/  
	@Id@GeneratedValue  
    private long uid;
	/**账号*/ 
	@NotBlank(message="用户名不能为空", groups={SysUserSaveValidate.class})
	@Size(min = 1, max = 32, message="用户名称长度必须为1到32之间", groups={SysUserSaveValidate.class})
    @Column(unique=true)  
    private String username;  
    /**名称（昵称或者真实姓名，不同系统不同定义）*/ 
	@NotBlank(message="姓名不能为空", groups={SysUserSaveValidate.class})
	@Size(min = 1, max = 32, message="姓名长度必须为1到32之间", groups={SysUserSaveValidate.class})
    private String name;  
    /**密码;*/ 
    @NotBlank(message="密码不能为空", groups={SysUserSaveValidate.class})
	@Size(min = 1, max = 32, message="用户名称长度必须为1到32之间", groups={SysUserSaveValidate.class})
    private String password;  
    /**加密密码的盐*/  
    private String salt;
    /**用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.*/
    private byte state;  
    
    /**最后一次登录时间*/
	private Date lastLogin;
	/**最后一次登录ip*/
	private String ip;
	/**备注*/
	@Size(min = 1, max = 64, message="备注长度必须为1到32之间", groups={SysUserSaveValidate.class})
	private String remark;
	/**电子邮件*/
	@Email(message="邮箱格式不正确", groups={SysUserSaveValidate.class})
	@NotNull(message="邮箱不能为空", groups={SysUserSaveValidate.class})
	private String email;
	/**手机号*/
	@NotNull(message="手机号不能为空", groups={SysUserSaveValidate.class})
	@Pattern(regexp="^0?1[3|4|5|7|8][0-9]\\d{8}$", groups={SysUserSaveValidate.class}, message="手机号格式不正确")
	private String phone;
	/**创建时间*/
	private Date createDate;
    
    /**立即从数据库中进行加载数据; 
                 一个用户具有多个角色  
     */
    @ManyToMany(fetch=FetchType.LAZY) 
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid",
    foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT)) }, inverseJoinColumns ={@JoinColumn(name = "roleId") })  
    private List<SysRole> roleList;
    /**用户的菜单权限*/
    @Transient
    private Map<String, SysPermission> menuPermissions;
    /**用户的增删改查权限*/
    @Transient
    private Map<String, SysPermission> crudButtonPermissions;
    /**用户的按钮权限*/
    @Transient
    private Map<String, SysPermission> buttonPermissions;
    /**用户企业*/
    @Transient
    @Valid
    private Company company;
    
    
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Map<String, SysPermission> getMenuPermissions() {
		return menuPermissions;
	}

	public void setMenuPermissions(Map<String, SysPermission> menuPermissions) {
		this.menuPermissions = menuPermissions;
	}

	

	public Map<String, SysPermission> getCrudButtonPermissions() {
		return crudButtonPermissions;
	}

	public void setCrudButtonPermissions(Map<String, SysPermission> crudButtonPermissions) {
		this.crudButtonPermissions = crudButtonPermissions;
	}

	public Map<String, SysPermission> getButtonPermissions() {
		return buttonPermissions;
	}

	public void setButtonPermissions(Map<String, SysPermission> buttonPermissions) {
		this.buttonPermissions = buttonPermissions;
	}

	public List<SysRole> getRoleList() {  
       return roleList;  
    }  
   
    public void setRoleList(List<SysRole> roleList) {  
       this.roleList = roleList;  
    }  
   
    public long getUid() {  
       return uid;  
    }  
   
    public void setUid(long uid) {  
       this.uid = uid;  
    }  
   
    public String getUsername() {  
       return username;  
    }  
   
    public void setUsername(String username) {  
       this.username = username;  
    }  
   
    public String getName() {  
       return name;  
    }  
   
    public void setName(String name) {  
       this.name = name;  
    }  
   
    public String getPassword() {  
       return password;  
    }  
   
    public void setPassword(String password) {  
       this.password = password;  
    }  
   
    public String getSalt() {  
       return salt;  
    }  
   
    public void setSalt(String salt) {  
       this.salt = salt;  
    }  
   
    public byte getState() {  
       return state;  
    }  
   
    public void setState(byte state) {  
       this.state = state;  
    }  
   
    public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/** 
     * 密码盐. 
     * @return 
     */  
    public String getCredentialsSalt(){  
       return this.username+this.salt;  
    }  
   
    @Override  
    public String toString() {  
       return "UserInfo [uid=" + uid + ", username=" + username + ", name=" + name + ", password=" + password  
              + ", salt=" + salt + ", state=" + state + "]";  
    }  
   
     
}