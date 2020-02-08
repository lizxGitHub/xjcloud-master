//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;

public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
        value = "user_id",
        type = IdType.AUTO
    )
    private Integer userId;
    private String username;
    private String password;
    @JsonIgnore
    private String salt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private String delFlag;
    private String lockFlag;
    private String phone;
    private String avatar;
    private Integer deptId;
    private Integer tenantId;

    public SysUser() {
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SysUser)) {
            return false;
        } else {
            SysUser other = (SysUser)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label155: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label155;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label155;
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

                label134: {
                    Object this$salt = this.getSalt();
                    Object other$salt = other.getSalt();
                    if (this$salt == null) {
                        if (other$salt == null) {
                            break label134;
                        }
                    } else if (this$salt.equals(other$salt)) {
                        break label134;
                    }

                    return false;
                }

                label127: {
                    Object this$createTime = this.getCreateTime();
                    Object other$createTime = other.getCreateTime();
                    if (this$createTime == null) {
                        if (other$createTime == null) {
                            break label127;
                        }
                    } else if (this$createTime.equals(other$createTime)) {
                        break label127;
                    }

                    return false;
                }

                label120: {
                    Object this$updateTime = this.getUpdateTime();
                    Object other$updateTime = other.getUpdateTime();
                    if (this$updateTime == null) {
                        if (other$updateTime == null) {
                            break label120;
                        }
                    } else if (this$updateTime.equals(other$updateTime)) {
                        break label120;
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

                label106: {
                    Object this$lockFlag = this.getLockFlag();
                    Object other$lockFlag = other.getLockFlag();
                    if (this$lockFlag == null) {
                        if (other$lockFlag == null) {
                            break label106;
                        }
                    } else if (this$lockFlag.equals(other$lockFlag)) {
                        break label106;
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

                label92: {
                    Object this$avatar = this.getAvatar();
                    Object other$avatar = other.getAvatar();
                    if (this$avatar == null) {
                        if (other$avatar == null) {
                            break label92;
                        }
                    } else if (this$avatar.equals(other$avatar)) {
                        break label92;
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

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysUser;
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
        return result;
    }

    public String toString() {
        return "SysUser(userId=" + this.getUserId() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", salt=" + this.getSalt() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", delFlag=" + this.getDelFlag() + ", lockFlag=" + this.getLockFlag() + ", phone=" + this.getPhone() + ", avatar=" + this.getAvatar() + ", deptId=" + this.getDeptId() + ", tenantId=" + this.getTenantId() + ")";
    }
}
