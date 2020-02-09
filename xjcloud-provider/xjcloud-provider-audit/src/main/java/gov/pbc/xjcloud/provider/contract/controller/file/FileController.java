package gov.pbc.xjcloud.provider.contract.controller.file;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/audit-api/file")
/**
 * 文件上传控制器
 * @author zhengkai.blog.csdn.net
 **/
public class FileController {

    @Value("${audit.file.uploadFolder}")
    private String uploadFolder;

    @Value("${audit.file.uploadPrefix}")
    private String uploadPrefix;

    @Value("${audit.file.appUrl}")
    private String appUrl;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest req,@RequestParam("file") MultipartFile file, @RequestParam("bizKey") String bizKey) {
        MultipartRequest req1 = (MultipartRequest) req;
        if (file.isEmpty()) {
            return "上传失败";
        }
        if (StringUtils.isBlank(bizKey)) {
            return "业务分类为空";
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0,originalFilename.lastIndexOf("."));
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(fileName).append(sdf.format(new Date())).append(suffixName);
        String newFileName = tempName.toString();
        try {
            //返回给前端的访问地址
            String filePath = appUrl +"/"+uploadPrefix+"/"+bizKey+"/"+newFileName;
            File upload = new File(uploadFolder+File.separator+bizKey+File.separator+newFileName);
            if(!upload.exists()){
                upload.getParentFile().mkdirs();
            }
            file.transferTo(upload);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean checkFileSize(Long len, int size, String unit) {
//        long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;

    }
}

