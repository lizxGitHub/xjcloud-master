//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.vo;

import java.io.Serializable;
import java.util.Date;

public class MissionMemberVO implements Serializable {
    private Integer id;
    private String basicId;
    private Integer userId;
    private String userName;
    private Date assignTime;

    public MissionMemberVO() {
    }

    public Integer getId() {
        return this.id;
    }

    public String getBasicId() {
        return this.basicId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public Date getAssignTime() {
        return this.assignTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBasicId(String basicId) {
        this.basicId = basicId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MissionMemberVO)) {
            return false;
        } else {
            MissionMemberVO other = (MissionMemberVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label71;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label71;
                    }

                    return false;
                }

                Object this$basicId = this.getBasicId();
                Object other$basicId = other.getBasicId();
                if (this$basicId == null) {
                    if (other$basicId != null) {
                        return false;
                    }
                } else if (!this$basicId.equals(other$basicId)) {
                    return false;
                }

                label57: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label57;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label57;
                    }

                    return false;
                }

                Object this$userName = this.getUserName();
                Object other$userName = other.getUserName();
                if (this$userName == null) {
                    if (other$userName != null) {
                        return false;
                    }
                } else if (!this$userName.equals(other$userName)) {
                    return false;
                }

                Object this$assignTime = this.getAssignTime();
                Object other$assignTime = other.getAssignTime();
                if (this$assignTime == null) {
                    if (other$assignTime == null) {
                        return true;
                    }
                } else if (this$assignTime.equals(other$assignTime)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MissionMemberVO;
    }

    @Override
    public int hashCode() {
        Boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $basicId = this.getBasicId();
        result = result * 59 + ($basicId == null ? 43 : $basicId.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        Object $assignTime = this.getAssignTime();
        result = result * 59 + ($assignTime == null ? 43 : $assignTime.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "MissionMemberVO(id=" + this.getId() + ", basicId=" + this.getBasicId() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", assignTime=" + this.getAssignTime() + ")";
    }
}
