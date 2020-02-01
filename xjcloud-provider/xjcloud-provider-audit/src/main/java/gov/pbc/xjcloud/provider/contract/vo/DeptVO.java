package gov.pbc.xjcloud.provider.contract.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DeptVO {
    private String area;
    private Date createTime;
    private String delFlag;
    private int deptId;
    private int level;
    private String name;
    private int parentId;
    private String region;
    private int sort;
    private Date updateTime;
}
