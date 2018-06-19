package com.wuliaozhiyuan.bean.shiro;
   
import java.io.Serializable;
import java.util.List;

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
   
   
/** 
 * 系统角色实体类; 
 * @author Angel(QQ:412887952) 
 * @version v.0.1 
 */  
@Entity  
public class SysRole implements Serializable{  
	private static final long serialVersionUID = -5157972052352230349L;
	/**主键*/
	@Id@GeneratedValue  
    private Long id;   
	/** 角色标识程序中判断使用,如"admin",这个是唯一的*/
    private String role;  
    /**角色描述,UI界面显示使用 */
    private String name; 
    /**是否可用,如果不可用将不会添加给用户 */
    private Boolean available = Boolean.FALSE; 
     
    /**角色 -- 权限关系：多对多关系;*/  
    @ManyToMany(fetch=FetchType.LAZY)  
@JoinTable(name="SysRolePermission",
	joinColumns={@JoinColumn(name="roleId",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))},
	inverseJoinColumns={@JoinColumn(name="permissionId")})  
    private List<SysPermission> permissions;  
     
    /** 用户 - 角色关系定义;  
    * 一个角色对应多个用户*/  
    @ManyToMany  
@JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId",
	foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))},inverseJoinColumns={@JoinColumn(name="uid")})  
    private List<SysUser> userInfos;
     
    public List<SysUser> getUserInfos() {  
       return userInfos;  
    }  
    public void setUserInfos(List<SysUser> userInfos) {  
       this.userInfos = userInfos;  
    }  
    public Long getId() {  
       return id;  
    }  
    public void setId(Long id) {  
       this.id = id;  
    }  
    public String getRole() {  
       return role;  
    }  
    public void setRole(String role) {  
       this.role = role;  
    }  
    
    public Boolean getAvailable() {  
       return available;  
    }  
    public void setAvailable(Boolean available) {  
       this.available = available;  
    }  
    public List<SysPermission> getPermissions() {  
       return permissions;  
    }  
    public void setPermissions(List<SysPermission> permissions) {  
       this.permissions = permissions;  
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SysRole(Long id, String role, String name, Boolean available, List<SysPermission> permissions,
			List<SysUser> userInfos) {
		super();
		this.id = id;
		this.role = role;
		this.name = name;
		this.available = available;
		this.permissions = permissions;
		this.userInfos = userInfos;
	}  
   public SysRole(){
	   
   }
} 