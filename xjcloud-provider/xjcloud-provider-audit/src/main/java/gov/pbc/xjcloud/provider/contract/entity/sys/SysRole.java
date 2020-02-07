//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SysRole extends Model<SysRole> {
    private static final long serialVersionUID = 1L;
    @TableId(
        value = "role_id",
        type = IdType.AUTO
    )
    private Integer roleId;
    @NotBlank(
        message = "角色名称 不能为空"
    )
    private String roleName;
    @NotBlank(
        message = "角色标识 不能为空"
    )
    private String roleCode;
    @NotBlank(
        message = "角色描述 不能为空"
    )
    private String roleDesc;
    @NotNull(
        message = "数据权限类型 不能为空"
    )
    private Integer dsType;
    private String dsScope;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private String delFlag;

    protected Serializable pkVal() {
        return this.roleId;
    }

    public SysRole() {
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleDesc() {
        return this.roleDesc;
    }

    public Integer getDsType() {
        return this.dsType;
    }

    public String getDsScope() {
        return this.dsScope;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public void setDsType(Integer dsType) {
        this.dsType = dsType;
    }

    public void setDsScope(String dsScope) {
        this.dsScope = dsScope;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String toString() {
        return "SysRole(roleId=" + this.getRoleId() + ", roleName=" + this.getRoleName() + ", roleCode=" + this.getRoleCode() + ", roleDesc=" + this.getRoleDesc() + ", dsType=" + this.getDsType() + ", dsScope=" + this.getDsScope() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", delFlag=" + this.getDelFlag() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SysRole)) {
            return false;
        } else {
            SysRole other = (SysRole)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label121: {
                    Object this$roleId = this.getRoleId();
                    Object other$roleId = other.getRoleId();
                    if (this$roleId == null) {
                        if (other$roleId == null) {
                            break label121;
                        }
                    } else if (this$roleId.equals(other$roleId)) {
                        break label121;
                    }

                    return false;
                }

                Object this$roleName = this.getRoleName();
                Object other$roleName = other.getRoleName();
                if (this$roleName == null) {
                    if (other$roleName != null) {
                        return false;
                    }
                } else if (!this$roleName.equals(other$roleName)) {
                    return false;
                }

                label107: {
                    Object this$roleCode = this.getRoleCode();
                    Object other$roleCode = other.getRoleCode();
                    if (this$roleCode == null) {
                        if (other$roleCode == null) {
                            break label107;
                        }
                    } else if (this$roleCode.equals(other$roleCode)) {
                        break label107;
                    }

                    return false;
                }

                Object this$roleDesc = this.getRoleDesc();
                Object other$roleDesc = other.getRoleDesc();
                if (this$roleDesc == null) {
                    if (other$roleDesc != null) {
                        return false;
                    }
                } else if (!this$roleDesc.equals(other$roleDesc)) {
                    return false;
                }

                Object this$dsType = this.getDsType();
                Object other$dsType = other.getDsType();
                if (this$dsType == null) {
                    if (other$dsType != null) {
                        return false;
                    }
                } else if (!this$dsType.equals(other$dsType)) {
                    return false;
                }

                label86: {
                    Object this$dsScope = this.getDsScope();
                    Object other$dsScope = other.getDsScope();
                    if (this$dsScope == null) {
                        if (other$dsScope == null) {
                            break label86;
                        }
                    } else if (this$dsScope.equals(other$dsScope)) {
                        break label86;
                    }

                    return false;
                }

                label79: {
                    Object this$createTime = this.getCreateTime();
                    Object other$createTime = other.getCreateTime();
                    if (this$createTime == null) {
                        if (other$createTime == null) {
                            break label79;
                        }
                    } else if (this$createTime.equals(other$createTime)) {
                        break label79;
                    }

                    return false;
                }

                Object this$updateTime = this.getUpdateTime();
                Object other$updateTime = other.getUpdateTime();
                if (this$updateTime == null) {
                    if (other$updateTime != null) {
                        return false;
                    }
                } else if (!this$updateTime.equals(other$updateTime)) {
                    return false;
                }

                Object this$delFlag = this.getDelFlag();
                Object other$delFlag = other.getDelFlag();
                if (this$delFlag == null) {
                    if (other$delFlag != null) {
                        return false;
                    }
                } else if (!this$delFlag.equals(other$delFlag)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysRole;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : $roleId.hashCode());
        Object $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        Object $roleCode = this.getRoleCode();
        result = result * 59 + ($roleCode == null ? 43 : $roleCode.hashCode());
        Object $roleDesc = this.getRoleDesc();
        result = result * 59 + ($roleDesc == null ? 43 : $roleDesc.hashCode());
        Object $dsType = this.getDsType();
        result = result * 59 + ($dsType == null ? 43 : $dsType.hashCode());
        Object $dsScope = this.getDsScope();
        result = result * 59 + ($dsScope == null ? 43 : $dsScope.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }
}
