package gov.pbc.xjcloud.provider.contract.utils;

import java.util.UUID;

public class IdGenUtil {

    public  static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
