package gov.pbc.xjcloud.provider.contract.utils;

import java.io.InputStream;

public class FileUtil {
 
    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}