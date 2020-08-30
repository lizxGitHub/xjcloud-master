//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MissionVO implements Serializable {
    private String basicId;
    private String basicTitle;
    private String basicDescription;
    private Integer createrId;
    private String createrName;
    private String status;
    private Date beginDate;
    private Date endDate;
    private List<MissionMemberVO> members = new ArrayList();

    public MissionVO() {
    }

    public String getBasicId() {
        return this.basicId;
    }

    public String getBasicTitle() {
        return this.basicTitle;
    }

    public String getBasicDescription() {
        return this.basicDescription;
    }

    public Integer getCreaterId() {
        return this.createrId;
    }

    public String getCreaterName() {
        return this.createrName;
    }

    public String getStatus() {
        return this.status;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public List<MissionMemberVO> getMembers() {
        return this.members;
    }

    public void setBasicId(String basicId) {
        this.basicId = basicId;
    }

    public void setBasicTitle(String basicTitle) {
        this.basicTitle = basicTitle;
    }

    public void setBasicDescription(String basicDescription) {
        this.basicDescription = basicDescription;
    }

    public void setCreaterId(Integer createrId) {
        this.createrId = createrId;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMembers(List<MissionMemberVO> members) {
        this.members = members;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MissionVO)) {
            return false;
        } else {
            MissionVO other = (MissionVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label119: {
                    Object this$basicId = this.getBasicId();
                    Object other$basicId = other.getBasicId();
                    if (this$basicId == null) {
                        if (other$basicId == null) {
                            break label119;
                        }
                    } else if (this$basicId.equals(other$basicId)) {
                        break label119;
                    }

                    return false;
                }

                Object this$basicTitle = this.getBasicTitle();
                Object other$basicTitle = other.getBasicTitle();
                if (this$basicTitle == null) {
                    if (other$basicTitle != null) {
                        return false;
                    }
                } else if (!this$basicTitle.equals(other$basicTitle)) {
                    return false;
                }

                label105: {
                    Object this$basicDescription = this.getBasicDescription();
                    Object other$basicDescription = other.getBasicDescription();
                    if (this$basicDescription == null) {
                        if (other$basicDescription == null) {
                            break label105;
                        }
                    } else if (this$basicDescription.equals(other$basicDescription)) {
                        break label105;
                    }

                    return false;
                }

                Object this$createrId = this.getCreaterId();
                Object other$createrId = other.getCreaterId();
                if (this$createrId == null) {
                    if (other$createrId != null) {
                        return false;
                    }
                } else if (!this$createrId.equals(other$createrId)) {
                    return false;
                }

                label91: {
                    Object this$createrName = this.getCreaterName();
                    Object other$createrName = other.getCreaterName();
                    if (this$createrName == null) {
                        if (other$createrName == null) {
                            break label91;
                        }
                    } else if (this$createrName.equals(other$createrName)) {
                        break label91;
                    }

                    return false;
                }

                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }

                label77: {
                    Object this$beginDate = this.getBeginDate();
                    Object other$beginDate = other.getBeginDate();
                    if (this$beginDate == null) {
                        if (other$beginDate == null) {
                            break label77;
                        }
                    } else if (this$beginDate.equals(other$beginDate)) {
                        break label77;
                    }

                    return false;
                }

                label70: {
                    Object this$endDate = this.getEndDate();
                    Object other$endDate = other.getEndDate();
                    if (this$endDate == null) {
                        if (other$endDate == null) {
                            break label70;
                        }
                    } else if (this$endDate.equals(other$endDate)) {
                        break label70;
                    }

                    return false;
                }

                Object this$members = this.getMembers();
                Object other$members = other.getMembers();
                if (this$members == null) {
                    if (other$members != null) {
                        return false;
                    }
                } else if (!this$members.equals(other$members)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MissionVO;
    }


    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $basicId = this.getBasicId();
        result = result * 59 + ($basicId == null ? 43 : $basicId.hashCode());
        Object $basicTitle = this.getBasicTitle();
        result = result * 59 + ($basicTitle == null ? 43 : $basicTitle.hashCode());
        Object $basicDescription = this.getBasicDescription();
        result = result * 59 + ($basicDescription == null ? 43 : $basicDescription.hashCode());
        Object $createrId = this.getCreaterId();
        result = result * 59 + ($createrId == null ? 43 : $createrId.hashCode());
        Object $createrName = this.getCreaterName();
        result = result * 59 + ($createrName == null ? 43 : $createrName.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $beginDate = this.getBeginDate();
        result = result * 59 + ($beginDate == null ? 43 : $beginDate.hashCode());
        Object $endDate = this.getEndDate();
        result = result * 59 + ($endDate == null ? 43 : $endDate.hashCode());
        Object $members = this.getMembers();
        result = result * 59 + ($members == null ? 43 : $members.hashCode());
        return result;
    }


    @Override
    public String toString() {
        return "MissionVO(basicId=" + this.getBasicId() + ", basicTitle=" + this.getBasicTitle() + ", basicDescription=" + this.getBasicDescription() + ", createrId=" + this.getCreaterId() + ", createrName=" + this.getCreaterName() + ", status=" + this.getStatus() + ", beginDate=" + this.getBeginDate() + ", endDate=" + this.getEndDate() + ", members=" + this.getMembers() + ")";
    }
}
