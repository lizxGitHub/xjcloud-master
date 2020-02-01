package gov.pbc.xjcloud.provider.contract.enumutils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanStatusEnum {

    PLAN_UN_SUBMIT(1001,"未批呈"),
    PLAN_TOBE_AUDITED(1002,"待审核"),
    PLAN_IMP_PASS(1003,"实施部门确认计划"),
    PLAN_IMP_REJECT(1004,"实施部门拒绝计划"),
    PLAN_AUDIT_PASS(1005,"审计对象通过计划"),
    RECTIFY_INCOMPLETE(1006,"整改信息待完善"),
    RECTIFY_REJECT(1007,"整改信息未通过"),
    RECTIFY_COMPLETE(1008,"整改信息已完善"),
    COMPLETE_TOBE_AUDIT(1009,"整改信息待审核"),
    IMP_AUDIT(10010,"整改待审核"),
    IMP_PASS(1011,"整改通过"),
    IMP_REJECT(1012,"驳回整改"),
    FILE(1013,"已归档");

    private int code;

    private String name;

}
