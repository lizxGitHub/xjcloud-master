package gov.pbc.xjcloud.provider.contract.vo;

import lombok.Data;

import java.util.List;

/**
 * 树形结构实体
 */
@Data
public class TreeNodeVO {
    private int id;

    private String title;

    private List<TreeNodeVO> children;
}
