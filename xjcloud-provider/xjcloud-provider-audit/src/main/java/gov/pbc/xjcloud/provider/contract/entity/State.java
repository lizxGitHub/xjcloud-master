package gov.pbc.xjcloud.provider.contract.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class State implements Serializable {
    private String code;
    private String name;

}
