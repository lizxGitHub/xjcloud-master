package gov.pbc.xjcloud.provider.contract.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import gov.pbc.xjcloud.provider.contract.exceptions.AppException;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;

/**
 * 超时
 */
@TableName(value = "plan_overtime_tip")
@Data
@Slf4j
public class PlanOverTimeTip {
    private Integer id;

    private String planId;

    /**
     * @link (gov.pbc.xjcloud.provider.contract.constants.OverTimeConstants)
     */
    private Integer overType;

    private Integer tipAssignee;

    private Integer tenantId;

    @TableLogic(delval = "1", value = "0")
    private String delFlag;

    public void setTipAssignee(Integer tipAssignee) {
        if (null == tipAssignee) {
            log.error("设置代理人失败");
            throw new AppException("设置代理人失败");
        }
        this.tipAssignee = tipAssignee;
    }
}
