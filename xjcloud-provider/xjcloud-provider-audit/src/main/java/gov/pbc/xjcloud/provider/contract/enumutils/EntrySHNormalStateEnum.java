package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntrySHNormalStateEnum {

    /**
     * 词条审核部门一般员工
     */
    SH_NORMAL_NO_PRESENTATION("1001", "未呈批"),
    SH_NORMAL_AWAITING_AUDIT("1002", "待审核"),
    SH_NORMAL_HAVE_APPROVAL("1003", "已批准"),
    SH_NORMAL_REJECT("1004", "被驳回");

    private String code;
    private String name;

}
