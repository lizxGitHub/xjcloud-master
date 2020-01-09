package gov.pbc.xjcloud.provider.contract.vo.entry;

import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import lombok.Data;

@Data
public class EntryInfoVO extends EntryInfo {
    /**
     * 分类名称
     */
    private String categoryName;


}
