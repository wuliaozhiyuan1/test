package com.wuliaozhiyuan.bean.shiro;

   
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;  
   
/** 
 * 权限实体类; 
 * @author Angel(QQ:412887952) 
 * @version v.0.1 
 */  
@Entity  
public class SysPermission implements Serializable{  
	private static final long serialVersionUID = -4624020687183119640L;
	public static class ResouceType{
    	public static final String MENU  = "menu";
    	public static final String CRUDBUTTON  = "crudButton";
    	public static final String BUTTON  = "button";
    }
     
    @Id@GeneratedValue  
    /**主键*/
    private long id;  
    @Column(unique=true)
    /**名称*/
    private String name;  
    /**资源类型，[menu|button]*/ 
    @Column(columnDefinition="enum('menu','button', 'crudButtom')")  
    private String resourceType;
    /**资源路径*/
    @Column(unique=true)
    private String url;
    /**权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view*/
    private String permission;   
    /**如果是crud权限对象，则parentId指向的是菜单id*/
    /**父编号*/  
    private Long parentId; 
    /**父编号列表*/ 
    private String parentIds;  
    private Boolean available = Boolean.FALSE;  
    /**menuType类型：false：系统菜单。true：业务菜单*/
    private Boolean menuType;
    @ManyToMany  
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId",
    	foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))},
    	inverseJoinColumns={@JoinColumn(name="roleId")})  
    private List<SysRole> roles;  
     
    private Integer menuOrder;
    private String menuIcon;
    private Boolean haveChildren;

  //子权限,key为id，value为权限对象
    @Transient
    private Map<Long, SysPermission> subSysPermissions;
	
	
	
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public Boolean getMenuType() {
		return menuType;
	}
	public void setMenuType(Boolean menuType) {
		this.menuType = menuType;
	}
	public List<SysRole> getRoles() {
		return roles;
	}
	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}
	public Integer getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	public Boolean getHaveChildren() {
		return haveChildren;
	}
	public void setHaveChildren(Boolean haveChildren) {
		this.haveChildren = haveChildren;
	}
	public Map<Long, SysPermission> getSubSysPermissions() {
		return subSysPermissions;
	}
	public void setSubSysPermissions(Map<Long, SysPermission> subSysPermissions) {
		this.subSysPermissions = subSysPermissions;
	}
	/**如果是crud权限对象，则parentId指向的是菜单id*/
    public SysPermission(String name, String resourceType, String permission, Boolean available, Long parentId) {
		super();
		this.name = name;
		this.resourceType = resourceType;
		this.permission = permission;
		this.available = available;
		//因为，数据库字段设为不能为空，增加索引效率，所以构造crudButton权限的时候，这里都赋初值值，没有实际意义
		this.parentId = parentId;
		this.parentIds = "0/";
		this.haveChildren = false;
		this.menuType = false;
	}
	public SysPermission() {
	}
	@Override  
    public String toString() {  
       return "SysPermission [id=" + id + ", name=" + name + ", resourceType=" + resourceType + ", url=" + url  
              + ", permission=" + permission + ", parentId=" + parentId + ", parentIds=" + parentIds + ", available="  
              + available + ", roles=" + roles + "]";  
    }  
     
} 