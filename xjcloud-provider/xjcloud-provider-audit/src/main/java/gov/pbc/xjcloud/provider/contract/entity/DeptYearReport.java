package gov.pbc.xjcloud.provider.contract.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name="dept_year_report")
public class DeptYearReport implements Serializable,Cloneable{
    /** 机构id */
    private String deptId ;
    /** 年度 */
    private String auditYear ;
    /** 报告内容 */
    private String content ;
}
