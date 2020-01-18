package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BSHLeaderStateEnum {

    /**
     * 被审核部门领导
     */
    BSH_LEADER_AWAITING_AUDIT("1001", "待审核"),
    BSH_LEADER_REJECT("1002", "已驳回"),
    BSH_LEADER_HAVE_AUDIT("1003", "已完成"),
    BSH_LEADER_RECTIFICATION("1004", "待完善"),
    BSH_LEADER_ARRAIGNMENT("1005", "批准待整改"),
    BSH_LEADER_AWAITING_FILE("1006", "待归档");

    private String code;
    private String name;

}
