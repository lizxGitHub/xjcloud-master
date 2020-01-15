package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BSHNormalStateEnum {

    /**
     * 被审核部门一般员工
     */
    BSH_NORMAL_RECTIFICATION("1011", "待整改"),
    BSH_NORMAL_AWAITING_AUDIT("1011", "待审核"),
    BSH_NORMAL_REJECT("1012", "被驳回"),
    BSH_NORMAL_COMPLETE("1013", "已完成");

    private String code;
    private String name;

}
