package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntrySHLeaderStateEnum {

    /**
     * 词条审核部门领导
     */
    SH_LEADER_AWAITING_AUDIT("1005", "待审核"),
    SH_LEADER_REJECT("1006", "已驳回"),
    SH_LEADER_HAVE_AUDIT("1007", "已批准");

    private String code;
    private String name;

}
