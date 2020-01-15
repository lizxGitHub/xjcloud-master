package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateEnum {

    /**
     * 审核部门一般员工
     */
    SH_NORMAL_NO_PRESENTATION("1001", "未呈批"),
    SH_NORMAL_AWAITING_AUDIT("1002", "待审核"),
    SH_NORMAL_HAVE_APPROVAL("1003", "已批准"),
    SH_NORMAL_REJECT("1004", "被驳回"),

    /**
     * 审核部门领导
     */
    SH_LEADER_AWAITING_AUDIT("1005", "待审核"),
    SH_LEADER_REJECT("1006", "已驳回"),
    SH_LEADER_HAVE_AUDIT("1007", "已批准"),

    /**
     * 被审核部门一般员工
     */
    BSH_NORMAL_RECTIFICATION("1011", "待整改"),
    BSH_NORMAL_AWAITING_AUDIT("1011", "待审核"),
    BSH_NORMAL_REJECT("1012", "被驳回"),
    BSH_NORMAL_COMPLETE("1013", "已完成"),

    /**
     * 被审核部门领导
     */
    BSH_LEADER_PERFECT("1014", "待完善"),
    BSH_LEADER_AWAITING_AUDIT("1015", "待审核"),
    BSH_LEADER_REJECT("1016", "被驳回"),
    BSH_LEADER_HAVE_AUDIT("1017", "已批准")
    ;

    private String code;
    private String name;
}
