package gov.pbc.xjcloud.provider.contract.utils;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * excel 导出设置下拉选项
 *
 * @author paungmiao@163.com
 */
public final class ExcelSelectListUtil {

    /**
     * firstRow 開始行號 根据此项目，默认为2(下标0开始)
     * lastRow  根据此项目，默认为最大65535
     * firstCol 区域中第一个单元格的列号 (下标0开始)
     * lastCol 区域中最后一个单元格的列号
     * strings 下拉内容
     */
    public static void selectList(Workbook workbook, int firstCol, int lastCol, String[] strings) {


        Sheet sheet = workbook.getSheetAt(0);
        //  生成下拉列表
        //  只对(x，x)单元格有效
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(2, 65535, firstCol, lastCol);
        //  生成下拉框内容
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(strings);
        HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
        //  对sheet页生效
        sheet.addValidationData(dataValidation);

    }

}
