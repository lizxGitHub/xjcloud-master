package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BSHLeaderStateEnum {

    /**
     * 被审核部门领导
     */
    BSH_LEADER_PERFECT("1014", "待完善"),
    BSH_LEADER_AWAITING_AUDIT("1015", "待审核"),
    BSH_LEADER_REJECT("1016", "被驳回"),
    BSH_LEADER_HAVE_AUDIT("1017", "已批准");

    private String code;
    private String name;

}
