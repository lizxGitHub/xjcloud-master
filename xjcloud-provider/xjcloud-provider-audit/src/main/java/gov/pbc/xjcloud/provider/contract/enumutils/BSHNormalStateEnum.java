package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BSHNormalStateEnum {

    /**
     * 被审核部门一般员工
     */
    BSH_NORMAL_RECTIFICATION("1021", "待整改"),
    BSH_NORMAL_AWAITING_AUDIT("1022", "待审批"),
    BSH_NORMAL_REJECT("1023", "被驳回"),
    BSH_NORMAL_APPROVAL("1024", "已批准"),
    BSH_NORMAL_COMPLETE("1025", "已完成");

    private String code;
    private String name;

}
