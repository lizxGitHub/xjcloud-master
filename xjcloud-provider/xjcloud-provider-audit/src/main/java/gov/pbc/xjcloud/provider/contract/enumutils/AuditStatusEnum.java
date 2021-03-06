package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    //  0-新建（待审核），1-审核通过，2-不通过

    ADD(0, "新建(待审核)"),

    PASS(1, "审核通过"),

    REJECT(2, "不通过");

    private int code;

    private String tip;

    /**
     *
     * @param code
     * @return
     */
    public static AuditStatusEnum getOptByCode(Integer code) {
        AuditStatusEnum[] values = AuditStatusEnum.values();
        for (AuditStatusEnum value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return ADD;
    }

}
