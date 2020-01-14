package gov.pbc.xjcloud.provider.contract.vo.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class EntryFlowVO extends EntryFlow implements Serializable, Cloneable {
    /**
     * 词条分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private String createdStart;
    @TableField(exist = false)
    private String createdEnd;

}