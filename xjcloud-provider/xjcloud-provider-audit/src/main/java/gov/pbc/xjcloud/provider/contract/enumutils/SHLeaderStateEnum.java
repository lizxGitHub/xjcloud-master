package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SHLeaderStateEnum {

    /**
     * 审核部门领导
     */
    SH_LEADER_AWAITING_AUDIT("1001", "待审核"),
    SH_LEADER_REJECT("1002", "已驳回"),
    SH_LEADER_HAVE_AUDIT("1003", "已批准"),
    SH_LEADER_RECTIFICATION("1004", "待完善"),
    SH_LEADER_RECTIFICATION_AUDIT("1005", "完善待审核"),
    SH_LEADER_AWAITING_RECTIFY("1006", "待整改"),
    SH_LEADER_AWAITING_FILE("1007", "待归档");

    private String code;
    private String name;

}
