package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SHLeaderStateEnum {

    /**
     * 审核部门领导
     */
    LEADER_1001("1001", "待整改"),
    LEADER_1002("1002", "已批准"),
    LEADER_1003("1003", "已驳回");

    private String code;
    private String name;

}
