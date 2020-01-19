package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BSHNormalStateEnum {

    /**
     * 被审核部门一般员工
     */
    BSH_NORMAL_AWAITING_RECTIFY("1001", "整改中"),
    BSH_NORMAL_AWAITING_AUDIT("1002", "整改完成待审核"),
    BSH_NORMAL_REJECT("1003", "被驳回"),
    BSH_NORMAL_APPROVAL("1004", "已批准"),
    BSH_NORMAL_COMPLETE("1005", "已完成"),
    BSH_NORMAL_RECTIFICATION("1006", "待完善"),
    BSH_NORMAL_RECTIFY("1007", "待整改");

    private String code;
    private String name;

}
