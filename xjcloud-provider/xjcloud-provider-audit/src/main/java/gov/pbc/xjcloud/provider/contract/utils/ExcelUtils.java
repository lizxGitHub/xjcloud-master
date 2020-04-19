package gov.pbc.xjcloud.provider.contract.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import gov.pbc.xjcloud.provider.contract.config.SpringContextHolder;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel导出工具类
 * @author duanj
 * @since JDK 1.8
 */
public class ExcelUtils {
    /**
     * 带头部导出
     * @author duanj
     * @param header
     * @param fileName
     * @param list
     * @param columnNames
     * @param keys
     * @param response
     * @throws IOException
     */
    public static void getEXCELFileWithHeader(String header, String fileName,
                                              List<Map<String, Object>> list, String[] columnNames,
                                              String[] keys,
                                              HttpServletResponse response,List<String> filters) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (HSSFWorkbook workbook = createWorkBookWithHeader(header, list, keys, columnNames,filters)) {
            workbook.write(os);
        }
        byte[] content = os.toByteArray();
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
            "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
        try {
            bis = new BufferedInputStream(new ByteArrayInputStream(content));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                os.close();
            }
            if (null != bis) {
                bis.close();
            }
            if (null != bos) {
                bos.close();
            }

        }
    }

    /**
     * excel导出加表头
     * @author duanj
     * @param list
     * @param keys
     * @param columnNames
     * @return
     */
    public static HSSFWorkbook createWorkBookWithHeader(String header,
                                                        List<Map<String, Object>> list,
                                                        String[] keys, String[] columnNames,List<String> filters) {
        // 创建excel工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        HSSFSheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }
        if (0 != keys.length) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, keys.length - 1));//合并单元格
        }
        //创建第一行，标题头部
        Row rowHeader = sheet.createRow((short) 0);
        org.apache.poi.ss.usermodel.Cell headerCell = rowHeader.createCell(0);
        headerCell.setCellValue(header);
        //字体
        HSSFFont headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 20);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        //样式
        CellStyle headerCss = wb.createCellStyle();
        headerCss.setFont(headerFont);
        headerCss.setBorderLeft(BorderStyle.THIN);
        headerCss.setBorderRight(BorderStyle.THIN);
        headerCss.setBorderTop(BorderStyle.THIN);
        headerCss.setBorderBottom(BorderStyle.THIN);
        headerCss.setAlignment(HorizontalAlignment.CENTER);
        headerCell.setCellStyle(headerCss);

        CellStyle qCss = wb.createCellStyle();
        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        HSSFFont f = wb.createFont();
        HSSFFont f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // Font f3=wb.createFont();
        // f3.setFontHeightInPoints((short) 10);
        // f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.THIN);
        cs2.setBorderRight(BorderStyle.THIN);
        cs2.setBorderTop(BorderStyle.THIN);
        cs2.setBorderBottom(BorderStyle.THIN);
        cs2.setAlignment(HorizontalAlignment.CENTER);
        int startRow = 1;
        filters.clear();
        if(filters!=null&&filters.size()>0) {
            Row row = sheet.createRow((short) startRow);
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(0);
            cell.setCellValue("查询条件");
            //字体
            HSSFFont qFont = wb.createFont();
            qFont.setFontHeightInPoints((short) 10);
            qFont.setColor(IndexedColors.BLACK.getIndex());
            //样式
            qCss.setFont(qFont);
            qCss.setBorderLeft(BorderStyle.THIN);
            qCss.setBorderRight(BorderStyle.THIN);
            qCss.setBorderTop(BorderStyle.THIN);
            qCss.setBorderBottom(BorderStyle.THIN);
            qCss.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(qCss);
            startRow++;
        }
        for (String filter : filters){
            Row row = sheet.createRow((short) startRow);
            org.apache.poi.ss.usermodel.Cell cellname = row.createCell(0);
            cellname.setCellValue(StringUtils.split(filter,"-")[0]);
            cellname.setCellStyle(cs);
            org.apache.poi.ss.usermodel.Cell cellval = row.createCell(1);
            cellval.setCellValue(StringUtils.split(filter,"-")[1]);
            cellval.setCellStyle(cs);
            startRow++;
        }
        // 创建列头
        Row row = sheet.createRow((short) startRow);
        // 设置列名
        for (int i = 0; i < columnNames.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        // 设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(startRow + 1);
            // 在row行上创建一个方格
            for (int j = 0; j < keys.length; j++) {
                org.apache.poi.ss.usermodel.Cell cell = row1.createCell(j);
                Object obj = list.get(i).get(keys[j]);
                if(keys[j].equals("implementing_agency_id")||keys[j].equals("audit_object_id")){
                    if(null!=deptMap.get(obj)){
                        obj=deptMap.get(obj);
                    }else  if(StringUtils.isNotBlank((String) obj)){
                        R rdept = userCenterService.dept(Integer.parseInt(obj.toString()));
                        JSONObject deptJSON = (JSONObject) JSONObject.toJSON(rdept);
                        if (null != deptJSON && "0".equals(deptJSON.get("code").toString())) {
                            JSONObject deptData = (JSONObject) JSONObject.toJSON(deptJSON.get("data"));
                            if(null!=deptData && null!= deptData.get("name")){
                                obj= deptData.get("name");
                            }
                        }
                    }
                }
                String cv = "";
                if (null != obj) {
                    String typeName = obj.getClass().getTypeName();
                    if ("java.sql.Timestamp".equals(typeName)) {
                        cv = DateUtils.dateToString((Date) obj, DateUtils.YYYY_MM_DD_HH_MM_SS);
                    }else {
                        if(keys[i].equals("days")||keys[i].equals("over_days")){
                            obj=Integer.parseInt(obj.toString())<0?0:Integer.parseInt(obj.toString());
                        }
                        cv=obj.toString();
                    }
                }
                cell.setCellValue(cv);
                cell.setCellStyle(cs2);
            }
            startRow++;
        }
        return wb;
    }

    private static UserCenterService userCenterService= SpringContextHolder.getBean(UserCenterService.class);

    private static Map<String,String> deptMap=new HashMap<>();
}
