package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * plan_
 */
@Data
public class PlanFile {

    private String id;

    private String fileUri;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date createdTime;
    /**
     * username
     */
    private String uploadUser;

    private int bizKey;

    private int taskId;

    private String taskName;
}
