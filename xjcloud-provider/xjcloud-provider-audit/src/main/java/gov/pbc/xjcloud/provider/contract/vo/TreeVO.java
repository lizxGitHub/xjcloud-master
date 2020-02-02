package gov.pbc.xjcloud.provider.contract.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class TreeVO {

    private int value;

    private String label;

    private LinkedHashMap<String,Object> data;

    private List<TreeVO> children;
}
