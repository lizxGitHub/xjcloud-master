package gov.pbc.xjcloud.provider.contract.vo.entry;

import com.baomidou.mybatisplus.annotation.TableField;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import lombok.Data;

@Data
public class EntryInfoVO extends EntryInfo {
    /**
     * 分类名称
     */
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private String createdStart;
    @TableField(exist = false)
    private String createdEnd;
}
