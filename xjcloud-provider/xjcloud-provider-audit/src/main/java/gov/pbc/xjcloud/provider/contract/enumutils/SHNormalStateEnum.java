package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SHNormalStateEnum {

    /**
     * 审核部门一般员工
     */
    NORMAL_1001("1001", "未呈报"),
    NORMAL_1002("1002", "待整改"),
    NORMAL_1003("1003", "已批准"),
    NORMAL_1004("1004", "被驳回");

    private String code;
    private String name;

}
