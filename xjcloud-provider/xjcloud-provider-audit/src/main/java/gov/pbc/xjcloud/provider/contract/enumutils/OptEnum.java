package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptEnum {

    ADD(1, "新增"),
    UPDATE(2, "修改"),
    DEL(0, "删除");

    private Integer code;

    private String tip;


}
