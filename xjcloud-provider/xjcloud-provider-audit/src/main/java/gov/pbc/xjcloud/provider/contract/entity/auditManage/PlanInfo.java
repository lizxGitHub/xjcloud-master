package gov.pbc.xjcloud.provider.contract.entity.auditManage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name="plan_info")
public class PlanInfo {

    @TableId(value = "id", type = IdType.UUID)
    @Column(name = "id")
    private String id;

    private int planId;

    private int userId;

    private String statusUser;

    private String opinion;

    private int type; // 类型（0审计计划管理、1审计项目管理）

}
