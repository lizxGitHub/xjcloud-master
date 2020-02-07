//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.vo;

import gov.pbc.xjcloud.provider.usercenter.api.entity.SysRole;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String password;
    private String salt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String delFlag;
    private String lockFlag;
    private String phone;
    private String avatar;
    private Integer deptId;
    private Integer tenantId;
    private String deptName;
    private List<String> permissionList;
    private List<SysRole> roleList;
    private List<Integer> roleIdList;

    public UserVO() {
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSalt() {
        return this.salt;
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

    public String getLockFlag() {
        return this.lockFlag;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public Integer getTenantId() {
        return this.tenantId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public List<String> getPermissionList() {
        return this.permissionList;
    }

    public List<SysRole> getRoleList() {
        return this.roleList;
    }

    public List<Integer> getRoleIdList() {
        return this.roleIdList;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public void setLockFlag(String lockFlag) {
        this.lockFlag = lockFlag;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    public void setRoleIdList(List<Integer> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UserVO)) {
            return false;
        } else {
            UserVO other = (UserVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label203: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label203;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label203;
                    }

                    return false;
                }

                Object this$username = this.getUsername();
                Object other$username = other.getUsername();
                if (this$username == null) {
                    if (other$username != null) {
                        return false;
                    }
                } else if (!this$username.equals(other$username)) {
                    return false;
                }

                Object this$password = this.getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }

                label182: {
                    Object this$salt = this.getSalt();
                    Object other$salt = other.getSalt();
                    if (this$salt == null) {
                        if (other$salt == null) {
                            break label182;
                        }
                    } else if (this$salt.equals(other$salt)) {
                        break label182;
                    }

                    return false;
                }

                label175: {
                    Object this$createTime = this.getCreateTime();
                    Object other$createTime = other.getCreateTime();
                    if (this$createTime == null) {
                        if (other$createTime == null) {
                            break label175;
                        }
                    } else if (this$createTime.equals(other$createTime)) {
                        break label175;
                    }

                    return false;
                }

                label168: {
                    Object this$updateTime = this.getUpdateTime();
                    Object other$updateTime = other.getUpdateTime();
                    if (this$updateTime == null) {
                        if (other$updateTime == null) {
                            break label168;
                        }
                    } else if (this$updateTime.equals(other$updateTime)) {
                        break label168;
                    }

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

                label154: {
                    Object this$lockFlag = this.getLockFlag();
                    Object other$lockFlag = other.getLockFlag();
                    if (this$lockFlag == null) {
                        if (other$lockFlag == null) {
                            break label154;
                        }
                    } else if (this$lockFlag.equals(other$lockFlag)) {
                        break label154;
                    }

                    return false;
                }

                Object this$phone = this.getPhone();
                Object other$phone = other.getPhone();
                if (this$phone == null) {
                    if (other$phone != null) {
                        return false;
                    }
                } else if (!this$phone.equals(other$phone)) {
                    return false;
                }

                label140: {
                    Object this$avatar = this.getAvatar();
                    Object other$avatar = other.getAvatar();
                    if (this$avatar == null) {
                        if (other$avatar == null) {
                            break label140;
                        }
                    } else if (this$avatar.equals(other$avatar)) {
                        break label140;
                    }

                    return false;
                }

                Object this$deptId = this.getDeptId();
                Object other$deptId = other.getDeptId();
                if (this$deptId == null) {
                    if (other$deptId != null) {
                        return false;
                    }
                } else if (!this$deptId.equals(other$deptId)) {
                    return false;
                }

                Object this$tenantId = this.getTenantId();
                Object other$tenantId = other.getTenantId();
                if (this$tenantId == null) {
                    if (other$tenantId != null) {
                        return false;
                    }
                } else if (!this$tenantId.equals(other$tenantId)) {
                    return false;
                }

                label119: {
                    Object this$deptName = this.getDeptName();
                    Object other$deptName = other.getDeptName();
                    if (this$deptName == null) {
                        if (other$deptName == null) {
                            break label119;
                        }
                    } else if (this$deptName.equals(other$deptName)) {
                        break label119;
                    }

                    return false;
                }

                label112: {
                    Object this$permissionList = this.getPermissionList();
                    Object other$permissionList = other.getPermissionList();
                    if (this$permissionList == null) {
                        if (other$permissionList == null) {
                            break label112;
                        }
                    } else if (this$permissionList.equals(other$permissionList)) {
                        break label112;
                    }

                    return false;
                }

                Object this$roleList = this.getRoleList();
                Object other$roleList = other.getRoleList();
                if (this$roleList == null) {
                    if (other$roleList != null) {
                        return false;
                    }
                } else if (!this$roleList.equals(other$roleList)) {
                    return false;
                }

                Object this$roleIdList = this.getRoleIdList();
                Object other$roleIdList = other.getRoleIdList();
                if (this$roleIdList == null) {
                    if (other$roleIdList != null) {
                        return false;
                    }
                } else if (!this$roleIdList.equals(other$roleIdList)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserVO;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        Object $salt = this.getSalt();
        result = result * 59 + ($salt == null ? 43 : $salt.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        Object $lockFlag = this.getLockFlag();
        result = result * 59 + ($lockFlag == null ? 43 : $lockFlag.hashCode());
        Object $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        Object $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        Object $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : $deptId.hashCode());
        Object $tenantId = this.getTenantId();
        result = result * 59 + ($tenantId == null ? 43 : $tenantId.hashCode());
        Object $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        Object $permissionList = this.getPermissionList();
        result = result * 59 + ($permissionList == null ? 43 : $permissionList.hashCode());
        Object $roleList = this.getRoleList();
        result = result * 59 + ($roleList == null ? 43 : $roleList.hashCode());
        Object $roleIdList = this.getRoleIdList();
        result = result * 59 + ($roleIdList == null ? 43 : $roleIdList.hashCode());
        return result;
    }

    public String toString() {
        return "UserVO(userId=" + this.getUserId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", salt=" + this.getSalt() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", delFlag=" + this.getDelFlag() + ", lockFlag=" + this.getLockFlag() + ", phone=" + this.getPhone() + ", avatar=" + this.getAvatar() + ", deptId=" + this.getDeptId() + ", tenantId=" + this.getTenantId() + ", deptName=" + this.getDeptName() + ", permissionList=" + this.getPermissionList() + ", roleList=" + this.getRoleList() + ", roleIdList=" + this.getRoleIdList() + ")";
    }
}
