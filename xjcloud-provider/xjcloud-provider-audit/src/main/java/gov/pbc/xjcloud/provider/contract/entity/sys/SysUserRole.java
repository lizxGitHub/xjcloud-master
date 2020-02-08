//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.entity.sys;

import com.baomidou.mybatisplus.extension.activerecord.Model;

public class SysUserRole extends Model<SysUserRole> {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private Integer roleId;

    public SysUserRole() {
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String toString() {
        return "SysUserRole(userId=" + this.getUserId() + ", roleId=" + this.getRoleId() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SysUserRole)) {
            return false;
        } else {
            SysUserRole other = (SysUserRole)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$userId = this.getUserId();
                Object other$userId = other.getUserId();
                if (this$userId == null) {
                    if (other$userId != null) {
                        return false;
                    }
                } else if (!this$userId.equals(other$userId)) {
                    return false;
                }

                Object this$roleId = this.getRoleId();
                Object other$roleId = other.getRoleId();
                if (this$roleId == null) {
                    if (other$roleId != null) {
                        return false;
                    }
                } else if (!this$roleId.equals(other$roleId)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysUserRole;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : $roleId.hashCode());
        return result;
    }
}
