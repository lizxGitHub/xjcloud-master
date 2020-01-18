package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SHNormalStateEnum {

    /**
     * 审核部门一般员工
     */
    SH_NORMAL_RECTIFICATION("1001", "待完善"),
    SH_NORMAL_AWAITING_RECTIFY("1002", "待整改"),
    SH_NORMAL_AWAITING_AUDIT("1003", "待审核"),
    SH_NORMAL_BE_REJECT("1004", "被驳回"),
    SH_NORMAL_COMPLETE("1005", "已完成"),
    SH_NORMAL_REJECT("1006", "已驳回"),
    SH_NORMAL_HAVE_AUDIT("1007", "已批准"),
    SH_NORMAL_WAIT_AUDIT("1008", "整改完成待审核");

    private String code;
    private String name;

}
