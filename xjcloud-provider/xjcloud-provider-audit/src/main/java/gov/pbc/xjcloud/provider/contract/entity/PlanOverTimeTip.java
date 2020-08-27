package gov.pbc.xjcloud.provider.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Maps;
import gov.pbc.xjcloud.provider.contract.exceptions.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 超时
 */
@TableName(value = "plan_overtime_tip")
@Data
@Slf4j
public class PlanOverTimeTip {
    @TableId(value = "id")
    private Integer id;

    private String planId;

    /**
     * @link (gov.pbc.xjcloud.provider.contract.constants.OverTimeConstants)
     */
    private Integer overType;

    private Integer tipAssignee;

    /**
     * 流程启动参数
     */
    @TableField(exist = false)
    private Map<String,Object> processParam= Maps.newHashMap();

    private Integer tenantId;

    @TableLogic(delval = "1", value = "0")
    private String delFlag;

    public void setTipAssignee(Integer tipAssignee) throws AppException {
        if (null == tipAssignee) {
            log.error("设置代理人失败，未找到相关代理人");
            throw new AppException("设置代理人失败，未找到相关代理人");
        }
        this.tipAssignee = tipAssignee;
    }
}
