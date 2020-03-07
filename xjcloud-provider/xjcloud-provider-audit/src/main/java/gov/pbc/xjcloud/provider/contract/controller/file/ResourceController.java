package gov.pbc.xjcloud.provider.contract.controller.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/${audit.file.uploadPrefix}")
@Slf4j
public class ResourceController {


    @Value("${audit.file.uploadFolder}")
    private String fileRootPath;

    private int BUFFER_LENGTH=1024;

    /*
   采用SPEL方式来表达路径，以消除不能获取文件扩展名的问题
    */
    @RequestMapping("/{bizKey}/{name:.+}")
    public  void DownloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("name") String fileName, @PathVariable("bizKey") String bizKey)
    {
        log.debug("the URL  is "+request.getRequestURL());

        if (isFileValid(bizKey,fileName))
        {
            String downloadFile=fileRootPath+File.separator+bizKey+File.separator+fileName;
            response.setContentType("application/force-download");
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            FileInputStream inStream=null;
            BufferedInputStream bufferInStream=null;
            try {
                ServletOutputStream outStream=response.getOutputStream();
                inStream=new FileInputStream(downloadFile);
                bufferInStream=new BufferedInputStream(inStream);
                byte[] buffer=new byte[BUFFER_LENGTH];
                int len=-1;
                while ( (len=bufferInStream.read(buffer))>0)
                {
                    outStream.write(buffer,0,len);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            finally
            {
                if (bufferInStream!=null) {
                    try {
                        bufferInStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (inStream!=null)
                {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                response.setHeader("content-Type","text/html;charset=UTF-8");
                outputStream.write(new String("文件不存在").getBytes("utf-8"));
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    protected  boolean isFileValid(String bizKey,String fileName)
    {
        if (fileName.isEmpty() || fileName==null || fileName.compareTo("")==0)
            return  false;
        String downloadFile=fileRootPath+File.separator+bizKey+File.separator+fileName;

        File file = new File(downloadFile);
        if (file.exists())
            return  true;
        else
            return  false;


    }
}
