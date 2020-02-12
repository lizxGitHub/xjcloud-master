package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import lombok.Data;

import java.util.Date;

/**
 * plan_
 */
@Data
public class PlanFile {

    private String id;

    private String fileUri;

    private Date createdTime;
    /**
     * username
     */
    private String uploadUser;

    private int bizKey;

    private int taskId;

    private String taskName;
}
