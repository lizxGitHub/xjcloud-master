package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SHNormalStateEnum {

    /**
     * 审核部门一般员工
     */
    SH_NORMAL_RECTIFICATION("1011", "待完善"),
    SH_NORMAL_AWAITING_RECTIFY("1012", "待整改"),
    SH_NORMAL_AWAITING_AUDIT("1013", "待审核"),
    SH_NORMAL_REJECT("1014", "被驳回"),
    SH_NORMAL_COMPLETE("1015", "已完成");

    private String code;
    private String name;

}
