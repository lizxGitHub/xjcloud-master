package gov.pbc.xjcloud.provider.contract.controller.file;

import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.R;
import gov.pbc.xjcloud.provider.contract.vo.PlanFileVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private PlanManagementServiceImpl service;


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R upload(HttpServletRequest req,
                         @RequestParam("file") MultipartFile file,
                         @RequestParam("bizKey") String bizKey) {
        MultipartRequest req1 = (MultipartRequest) req;
        if (file.isEmpty()) {
            return new R().setCode((int)ApiErrorCode.FAILED.getCode()).setMsg("文件为空");
        }
        if (StringUtils.isBlank(bizKey)) {
           bizKey="other";
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));

        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(fileName).append(sdf.format(new Date())).append(suffixName);
        String newFileName = tempName.toString();
        try {
            //返回给前端的访问地址
            String filePath = appUrl + "/" + uploadPrefix + "/" + bizKey + "/" + newFileName;
            String fileUri = uploadPrefix + "/" + bizKey + "/" + newFileName;
            File upload = new File(uploadFolder + File.separator + bizKey + File.separator + newFileName);
            if (!upload.exists()) {
                upload.getParentFile().mkdirs();
            }
            file.transferTo(upload);
            return new R().setData(fileUri);
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

    /**
     * 获取业务资源文件
     *
     * @param bizKey
     * @return
     */
    @GetMapping("biz/files")
    public R getBizFiles(@RequestParam String bizKey) {
//        File dir = new File(uploadFolder + bizKey);
//        List<File> files = (List<File>) FileUtils.listFiles(dir, null, false);
        List<PlanFileVO> list = service.findFilesByBizKey(bizKey);
        //        files.stream().forEach(e -> {
//            JSONObject jsonObject = new JSONObject();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(e.lastModified());
//            String time = sdf.format(cal.getTime());
//            jsonObject.put("lastTime", time);
//            jsonObject.put("fileNae", appUrl + "/" + uploadPrefix + "/" + bizKey + "/" + e.getName());
//            jsonObject.put("size", getFileSize(e.length()));
//            list.add(jsonObject);
//        });
        list.stream().filter(Objects::nonNull).filter(e->StringUtils.isNotBlank(e.getFileUri())).forEach(e->{
            e.setFileUrl(appUrl+'/'+e.getFileUri());
            e.setFileName(e.getFileUri().substring(e.getFileUri().lastIndexOf("/")+1));
        });
        return new R().setData(list);
    }

    private String getFileSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
}

