package gov.pbc.xjcloud.provider.contract.controller.entry;

import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.OptConstants;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.OptEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.FileUtil;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryInfoVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审计系统-词条管理
 *
 * @author paungmiao@163.com
 * @date 2020年1月6日21:53:24
 */
//audit-api 是我们模拟网关地址
@Slf4j
@RestController
@RequestMapping("/audit-api/entry")
public class EntryController {

    @Autowired
    private EntryServiceImpl entryService;

    @Autowired
    private EntryCategoryServiceImpl categoryService;

    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @ApiOperation("词条页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<EntryInfoVO>> index(EntryInfoVO query, Page<EntryInfoVO> page) {
        PageUtil.initPage(page);
        try {
            page = entryService.selectEntryInfo(page, query);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }


    /**
     * 新建词条并提交审核
     *
     * @return
     */
    @ApiOperation("新建词条并提交审核")
    @PostMapping("/add")
    public R<Boolean> startAddFlow(EntryInfo entryFlow) {

        R<Boolean> r = new R<>();
        try {
            entryService.validate(entryFlow, r);
            EntryCategory category = categoryService.getById(entryFlow.getCategoryFk());
            //乐观锁
            entryFlow.setRevision(1);
            //类型编码
            entryFlow.setTypeCode(category.getDefKey() + DateTime.now().toDate().getTime());
            entryFlow.setCreatedTime(DateTime.now().toDate());
            entryFlow.setDelFlag(DelConstants.EXITED);
            r.setData(entryService.save(entryFlow));
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }


    /**
     * 创建此条信息
     *
     * @return
     */
    @ApiOperation("词条名称重复验证")
    @GetMapping("/checkName")
    public R<Boolean> checkName(EntryInfo entryFlow) {

        R<Boolean> r = new R<>();
        try {
            QueryWrapper<EntryInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("name", entryFlow.getName());
            wrapper.eq("category_fk", entryFlow.getCategoryFk());
            //若存在id表示是修改名称检验id重复
            if (StringUtils.isNotBlank(entryFlow.getId())) {
                wrapper.ne("id", entryFlow.getId());
            }
            EntryInfo entryInfo = entryService.getOne(wrapper);
            r.setData( null != entryInfo);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 根据id获取词条信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取词条信息")
    @GetMapping("/{id}")
    public R<EntryInfo> getEntryInfo(@PathVariable String id) {
        R<EntryInfo> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            EntryInfo byId = entryService.getById(id);
            r.setData(byId);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /***
     *根据词条分类获取词条列表
     */
    @ApiOperation("根据分类获取词条列表")
    @GetMapping("/list_by_category/{cateKey}")
    public R<List<EntryInfo>> findEntryInfoByCateKey(@PathVariable String cateKey) {
        R<List<EntryInfo>> r = new R<>();
        QueryWrapper<EntryCategory> cateQuery = new QueryWrapper<>();
        QueryWrapper<EntryInfo> entryInfoQueryWrapper = new QueryWrapper<>();
        cateQuery.eq("def_key", cateKey);
        try {
            EntryCategory category = categoryService.getOne(cateQuery);
            entryInfoQueryWrapper.eq("category_fk", category.getId());
            List<EntryInfo> list = entryService.list(entryInfoQueryWrapper);
            r.setData(list);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setCode(ApiErrorCode.FAILED.getCode());
            e.printStackTrace();
        }
        return r;
    }

    @ApiOperation("用户修改词条")
    @PutMapping("/update")
    public R<Boolean> updateEntryFlow(EntryInfo entryFlow) {
        R<Boolean> r = new R<>();
        try {
            entryService.validate(entryFlow, r);

            EntryInfo beforeFlow = entryService.getById(entryFlow.getId());
            if (null == beforeFlow) {
                r.setData(false);
                r.setMsg("该数据已被删除");
                throw new NullPointerException(r.getMsg());
            }
            if (entryFlow.getRevision() != beforeFlow.getRevision()) {
                r.setData(false);
                r.setMsg("该数据已被更改，请刷新重试！");
                throw new IllegalArgumentException(r.getMsg());
            }
            UpdateWrapper<EntryInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("name", entryFlow.getName());
            updateWrapper.set("revision", beforeFlow.getRevision() + 1);
            updateWrapper.set("remarks", entryFlow.getRemarks());
            updateWrapper.set("category_fk", entryFlow.getCategoryFk());
            updateWrapper.set("updated_time", new Date());
            updateWrapper.set("updated_by", entryFlow.getUpdatedBy());
            updateWrapper.eq("id", entryFlow.getId());
            entryService.update(entryFlow, updateWrapper);
            r.setData(true);
            r.setCode(ApiErrorCode.SUCCESS.getCode());
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            r.setCode(ApiErrorCode.FAILED.getCode());
        }
        return r;
    }

    /**
     * 流程数据接口：删除
     *
     * @param id
     * @return
     */
    @ApiOperation("用户删除词条并发起流程")
    @DeleteMapping("user_delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        R<Boolean> r = new R<>();
        Boolean b = false;
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("请求参数不存在");
        } else {
            try {
                EntryInfo byId = entryService.getById(id);
                if (null == byId) {
                    throw new NullPointerException("该词条信息不存在");
                }
                b = entryService.removeById(id);
            } catch (Exception e) {
                r.setMsg(e.getMessage());
                r.setData(false);
                e.printStackTrace();
            }
        }
        return r.setData(b);
    }


    /**
     * @param request
     * @param response
     * @return
     */
    @GetMapping("template/download")
    public void getWordFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileNameString = "词条导入模板.xlsx"; //声明要下载的文件名
        ServletOutputStream out = null;
        try {
            InputStream fis = FileUtil.getResourcesFileInputStream("template/" + fileNameString);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode("词条导入模板", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    @PostMapping("import")
    public R<Boolean> importEntry(@RequestParam("file") MultipartFile file, @RequestParam("createdUser") String createdUser) {
        Sheet modelSheet;
        if (null == file) {
            return R.failed("上传文件不能为空");
        }
        String fileName = file.getOriginalFilename();
//        String[] headers = {"词条类型", "一级名称", "二级名称", "三级名称", "四级名称", "备注"};
        try {
            if (StringUtils.isBlank(fileName)) {
                throw new RuntimeException("导入文档为空!");
            } else if (fileName.toLowerCase().endsWith("xls")) {
            } else if (fileName.toLowerCase().endsWith("xlsx")) {
            } else {
                throw new RuntimeException("文档格式不正确!");
            }

            List<EntryCategory> list = categoryService.list();
            Map<String, EntryCategory> entryMap = list.stream().collect(Collectors.toMap(EntryCategory::getName, e -> e));
            modelSheet = ExcelUtil.getReader(file.getInputStream(), "词条导入").setIgnoreEmptyRow(true).getSheet();
            if (modelSheet == null) {
                throw new RuntimeException("请使用模板导入!");
            }
            if (modelSheet.getLastRowNum() < 2) {
                throw new RuntimeException("文档中没有工作表!");
            }
            int maxRow = modelSheet.getLastRowNum();
            List<EntryInfo> entryFlows = new ArrayList<>(maxRow - 1);
            for (int startRow = 2; startRow <= maxRow; startRow++) {
                Row row = modelSheet.getRow(startRow);
                String category = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                if (StringUtils.isBlank(category) || StringUtils.isBlank(name)) {
                    continue;
                }
                if (!entryMap.containsKey(category)) {
                    continue;
                }
                Cell remarksCell = row.getCell(5);
                String remarks =new String();
                if(null!= remarksCell){
                    remarks = row.getCell(5).getStringCellValue();
                }
                EntryInfo entryFlow = new EntryInfo();
                entryFlow.setRemarks(remarks);
                entryFlow.setCreatedBy(createdUser);
                entryFlow.setCategoryFk(entryMap.get(category).getId());
                entryFlow.setName(name);
                if (entryMap.get(category).getLevel() == 4) {
                    String name1 = new String();
                    String name2 = new String();
                    String name3 = new String();
                    Cell nameCell1 = row.getCell(2);
                    Cell nameCell2 = row.getCell(3);
                    Cell nameCell3 = row.getCell(4);
                    entryFlow.setName1(null==nameCell1?name1:nameCell1.getStringCellValue());
                    entryFlow.setName2(null==nameCell1?name2:nameCell2.getStringCellValue());
                    entryFlow.setName2(null==nameCell1?name3:nameCell3.getStringCellValue());
                }
                //乐观锁
                entryFlow.setRevision(1);
                //类型编码
                entryFlow.setTypeCode(entryMap.get(category).getDefKey() + new Date().getTime());
                entryFlow.setId(IdGenUtil.uuid());
                entryFlow.setCreatedTime(new Date());
                entryFlow.setDelFlag(DelConstants.EXITED);
                entryFlows.add(entryFlow);
            }
            boolean result = entryService.saveBatch(entryFlows, entryFlows.size());
            return new R<Boolean>().setData(result).setMsg(result ? "成功导入" + entryFlows.size() + "条数据" : "导入失败");
        } catch (IOException e) {
            e.printStackTrace();
            return new R<Boolean>(ApiErrorCode.FAILED).setMsg("请下载模板导入");
        }
    }

    @GetMapping("download/entry")
    public void downloadEntry(HttpServletResponse response) {
        List<EntryInfo> list = entryService.list();
        Map<Integer, String> collect = categoryService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(1000);
        sxssfWorkbook.createSheet("数据");
        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);
        String[] strings = new String[]
                {"词条编号","词条类型","一级词条","二级词条","三级词条","四级词条","备注"};
        //添加结果数据
        int excelRowNum = 0;
        int count = list.size();
        ListIterator<EntryInfo> rs = list.listIterator();
        SXSSFRow row = sheet.createRow(excelRowNum++);
        for (int i = 0; i <strings.length; i++) {
            row.createCell(i).setCellValue(parseText(strings[i]));
        }
        while (rs.hasNext()) {
            EntryInfo entryFlow = rs.next();
            if(excelRowNum>=1048576){
                break;
            }
            row = sheet.createRow(excelRowNum++);
            row.createCell(0).setCellValue(parseText(entryFlow.getTypeCode()));
            row.createCell(1).setCellValue(parseText(collect.get(entryFlow.getCategoryFk())));
            row.createCell(2).setCellValue(parseText(entryFlow.getName()));
            row.createCell(3).setCellValue(parseText(entryFlow.getName1()));
            row.createCell(4).setCellValue(parseText(entryFlow.getName2()));
            row.createCell(5).setCellValue(parseText(entryFlow.getName3()));
            row.createCell(6).setCellValue(parseText(entryFlow.getRemarks()));
        }
        OutputStream out =null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("词条导出列表.xlsx", "UTF-8"));
            out = response.getOutputStream();
            sxssfWorkbook.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理空字符串
     *
     * @param string
     * @return
     */
    private String parseText(String string) {
        if (string == null) {
            return "";
        }
        return string.trim();
    }
}







































