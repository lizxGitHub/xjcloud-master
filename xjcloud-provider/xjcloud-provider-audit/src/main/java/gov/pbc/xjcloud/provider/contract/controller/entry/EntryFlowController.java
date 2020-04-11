package gov.pbc.xjcloud.provider.contract.controller.entry;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.constants.OptConstants;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryCategory;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryFlow;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.AuditStatusEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.OptEnum;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryFlowServiceIml;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.FileUtil;
import gov.pbc.xjcloud.provider.contract.utils.IdGenUtil;
import gov.pbc.xjcloud.provider.contract.utils.PageUtil;
import gov.pbc.xjcloud.provider.contract.vo.entry.EntryFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 词条流程controller
 */
@Api("词条流程接口类")
@RestController
@RequestMapping("/audit-api/entry/flow")
@Log4j2
public class EntryFlowController {

    @Resource
    private EntryFlowServiceIml entryFlowService;

    @Resource
    private EntryServiceImpl entryService;

    @Resource
    private EntryCategoryServiceImpl categoryService;


    /**
     * 词条管理首页列表页面
     *
     * @return
     */
    @ApiOperation("词条页面信息")
    @GetMapping(value = {"page", ""})
    public R<IPage<EntryFlowVO>> index(EntryFlowVO query, IPage<EntryFlowVO> page) {
        PageUtil.initPage(page);
        try {
            //查询参数设置
            if (null != query) {
                page = entryFlowService.selectEntryInfo(page, query);
            }
            page.getRecords().stream().filter(Objects::nonNull).forEach(e -> {
                String name = e.getName();
                if (StringUtils.isNotBlank(e.getName1())) {
                    name += '-' + e.getName1();
                }
                if (StringUtils.isNotBlank(e.getName2())) {
                    name += '-' + e.getName2();
                }
                if (StringUtils.isNotBlank(e.getName3())) {
                    name += '-' + e.getName3();
                }
                e.setName(name);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(page);
    }

    /**
     * 查询参数设置
     *
     * @param query
     * @param queryWrapper
     */
    private void initQuery(EntryFlowVO query, QueryWrapper<EntryFlow> queryWrapper) {
        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.like("name", query.getName());
        }
        if (null != query.getCategoryFk()) {
            queryWrapper.eq("category_fk", query.getCategoryFk());
        }
        if (StringUtils.isNotBlank(query.getUserOpt())) {
            queryWrapper.eq("user_opt", query.getName());
        }
        if (StringUtils.isNotBlank(query.getAuditStatus())) {
            queryWrapper.eq("audit_status", query.getAuditStatus());
        }
        if (null != query.getCreatedStart() && null != query.getCreatedEnd()) {
            queryWrapper.between("created_time", query.getCreatedStart(), query.getCreatedEnd());
        } else if (null != query.getCreatedStart() && null == query.getCreatedEnd()) {
            queryWrapper.lt("created_time", query.getCreatedStart());
        } else if (null == query.getCreatedStart() && null != query.getCreatedEnd()) {
            queryWrapper.gt("created_time", query.getCreatedEnd());
        }
    }

    /**
     * 新建词条并提交审核
     *
     * @return
     */
    @ApiOperation("新建词条并提交审核")
    @PostMapping("/add")
    public R<Boolean> startAddFlow(EntryFlow entryFlow) {

        R<Boolean> r = new R<>();
        try {
            entryFlowService.validate(entryFlow, r);
            EntryCategory category = categoryService.getById(entryFlow.getCategoryFk());
            //乐观锁
            entryFlow.setRevision(1);
            //类型编码
            entryFlow.setTypeCode(category.getDefKey() + DateTime.now().toDate().getTime());
            // todo 此处需要能够访问用户服务 引入common-security包调用 SecurityUtils.getUsername() 方法;
//            entryFlow.setCreatedBy("user01");
//            entryFlow.setApplyUser("user01");
            entryFlow.setInstanceId(IdGenUtil.uuid());
            entryFlow.setEntryFk(IdGenUtil.uuid());
            //新增
            entryFlow.setAuditStatus(AuditStatusEnum.ADD.getCode() + "");
            entryFlow.setUserOpt(OptEnum.ADD.getCode().toString());
            entryFlow.setCreatedTime(DateTime.now().toDate());
            entryFlow.setDelFlag(DelConstants.EXITED);
            r.setData(entryFlowService.save(entryFlow));
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
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
                EntryFlow byId = entryFlowService.getById(id);
                if (null == byId) {
                    throw new NullPointerException("该词条信息不存在");
                }
                if (StringUtils.isNotBlank(byId.getInstanceId())) {
                    throw new IllegalAccessException("该词条正在审核中");
                }
                UpdateWrapper<EntryFlow> updateWrapper = new UpdateWrapper<>();
                EntryFlow entryFlow = new EntryFlow();
                entryFlow.setUserOpt(OptConstants.DEL);
                entryFlow.setInstanceId(IdGenUtil.uuid());
                entryFlow.setId(id);
                updateWrapper.set("user_opt", OptConstants.DEL);
                // todo 启动流程 获取流程实例ID
                updateWrapper.set("instance_id", entryFlow.getInstanceId());
                // 更改审核状态
                updateWrapper.set("audit_status", AuditStatusEnum.ADD.getCode());
                updateWrapper.eq("id", id);
                b = entryFlowService.update(entryFlow, updateWrapper);
            } catch (Exception e) {
                r.setMsg(e.getMessage());
                r.setData(false);
                e.printStackTrace();
            }
        }
        return r.setData(b);
    }

    /**
     * 创建此条信息
     *
     * @return
     */
    @ApiOperation("词条名称重复验证")
    @GetMapping("/checkName")
    public R<Boolean> checkName(EntryFlow entryFlow) {

        R<Boolean> r = new R<>();
        try {
            QueryWrapper<EntryFlow> wrapper = new QueryWrapper<>();
            QueryWrapper<EntryInfo> entryInfoQueryWrapper = new QueryWrapper<>();
            wrapper.eq("name", entryFlow.getName());
            wrapper.eq("category_fk", entryFlow.getCategoryFk());

            entryInfoQueryWrapper.eq("name", entryFlow.getName());
            entryInfoQueryWrapper.eq("category_fk", entryFlow.getCategoryFk());
            //若存在id表示是修改名称检验id重复
            if (StringUtils.isNotBlank(entryFlow.getId())) {
                wrapper.ne("id", entryFlow.getId());
                entryInfoQueryWrapper.ne("id", entryFlow.getEntryFk());
            }
            EntryFlow one = entryFlowService.getOne(wrapper);
            EntryInfo entryInfo = entryService.getOne(entryInfoQueryWrapper);
            r.setData(null != one || null != entryInfo);
        } catch (Exception e) {
            e.printStackTrace();
            r.failed(e.getMessage());
        }
        return r;
    }

    /**
     * 用户行为修改词条
     *
     * @param entryFlow
     * @return
     */
    @ApiOperation("用户修改词条并发起流程")
    @PutMapping("/update")
    public R<Boolean> updateEntryFlow(EntryFlow entryFlow) {
        R<Boolean> r = new R<>();
        try {
            entryFlowService.validate(entryFlow, r);

            EntryFlow beforeFlow = entryFlowService.getById(entryFlow.getId());
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
            if (StringUtils.isNotBlank(beforeFlow.getInstanceId())) {
                r.setMsg("该词条正在审核中,无法修改");
                r.setData(false);
                throw new NullPointerException(r.getMsg());
            }
            UpdateWrapper<EntryFlow> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("name", entryFlow.getName());
            updateWrapper.set("revision", beforeFlow.getRevision() + 1);
            updateWrapper.set("remarks", entryFlow.getRemarks());
            //用户行为更改
            updateWrapper.set("user_opt", OptConstants.update);
            updateWrapper.set("category_fk", entryFlow.getCategoryFk());
            updateWrapper.set("updated_time", new Date());
            // todo 启动修改流程
//            updateWrapper.set("updated_by", "user02");
            // 词条审核时使用该字段验证
            updateWrapper.set("instance_id", IdGenUtil.uuid());
            // 更改审核状态
            updateWrapper.set("audit_status", AuditStatusEnum.ADD.getCode());
            updateWrapper.eq("id", entryFlow.getId());
            entryFlowService.update(entryFlow, updateWrapper);
            r.setData(true);
            r.setCode(ApiErrorCode.SUCCESS.getCode());
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            r.setCode(ApiErrorCode.FAILED.getCode());
        }
        return r;
    }

    /**
     * 根据id获取词条信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取词条流程信息")
    @GetMapping("/{id}")
    public R<EntryFlow> getEntryInfo(@PathVariable String id) {
        R<EntryFlow> r = new R<>();
        try {
            if (StringUtils.isBlank(id)) {
                return r.failed("参数错误，请检查");
            }
            EntryFlow byId = entryFlowService.getById(id);
            r.setData(byId);
        } catch (Exception e) {
            r.failed(e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 根据id获取词条信息
     *
     * @param instanceId
     * @return
     */
    @ApiOperation("通过词条审核")
    @PutMapping("/pass/{instanceId}")
    public R<Boolean> passInstance(@PathVariable String instanceId) {
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(instanceId)) {
                return r.failed("参数错误，请检查");
            }
            entryFlowService.passEntryFlow(instanceId);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setData(false);
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 根据id获取词条信息
     *
     * @param instanceId
     * @return
     */
    @ApiOperation("驳回词条审核")
    @PutMapping("/reject/{instanceId}")
    public R<Boolean> rejectInstance(@PathVariable String instanceId) {
        R<Boolean> r = new R<>();
        try {
            if (StringUtils.isBlank(instanceId)) {
                return r.failed("参数错误，请检查");
            }
            entryFlowService.rejectEntryFlow(instanceId);
        } catch (Exception e) {
            r.setMsg(e.getMessage());
            r.setData(false);
            e.printStackTrace();
        }
        return r;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @GetMapping("template/download")
    public void getWordFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileNameString = "词条导入模板-bak.xlsx"; //声明要下载的文件名
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
        Sheet modelSheet = null;
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
            int cell = 6;//6列 ，指定模板,从0 开始读取
            List<EntryFlow> entryFlows = new ArrayList<>(maxRow - 1);
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
                EntryFlow entryFlow = new EntryFlow();
                entryFlow.setRemarks(remarks);
                entryFlow.setApplyUser(createdUser);
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
                    entryFlow.setName2(null==nameCell2?name2:nameCell2.getStringCellValue());
                    entryFlow.setName3(null==nameCell3?name3:nameCell3.getStringCellValue());
                }
                //乐观锁
                entryFlow.setRevision(1);
                //类型编码
                entryFlow.setTypeCode(entryMap.get(category).getDefKey() + new Date().getTime());
                entryFlow.setId(IdGenUtil.uuid());
                entryFlow.setEntryFk(IdGenUtil.uuid());
                entryFlow.setUserOpt(OptConstants.ADD);
                entryFlow.setAuditStatus(String.valueOf(AuditStatusEnum.ADD.getCode()));
                entryFlow.setInstanceId(IdGenUtil.uuid());
                entryFlow.setCreatedTime(new Date());
                entryFlow.setDelFlag(DelConstants.EXITED);
                entryFlows.add(entryFlow);
            }
            boolean result = entryFlowService.saveBatch(entryFlows, entryFlows.size());
            return new R<Boolean>().setData(result).setMsg(result ? "成功导入" + entryFlows.size() + "条数据" : "导入失败");
        } catch (IOException e) {
            e.printStackTrace();
            return new R<Boolean>(ApiErrorCode.FAILED).setMsg("请下载模板导入");
        }
    }

    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        Object val = "";
        try {
            Cell cell = row.getCell(column);
            if (cell != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    // val = cell.getNumericCellValue();
                    // 当excel 中的数据为数值或日期是需要特殊处理
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double d = cell.getNumericCellValue();
                        Date date = HSSFDateUtil.getJavaDate(d);
                        SimpleDateFormat dformat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        val = dformat.format(date);
                    } else {
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setGroupingUsed(false);// true时的格式：1,234,567,890
                        val = nf.format(cell.getNumericCellValue());// 数值类型的数据为double，所以需要转换一下
                    }
                } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    val = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    val = cell.getCellFormula();
                } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    val = cell.getBooleanCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                    val = cell.getErrorCellValue();
                }
            }
        } catch (Exception e) {
            return val;
        }
        return val;
    }

    @GetMapping("download/entry")
    public void downloadEntry(HttpServletResponse response) {
        List<EntryFlow> list = entryFlowService.list();
        Map<Integer, String> collect = categoryService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName()));
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(1000);
        sxssfWorkbook.createSheet("数据");
        SXSSFSheet sheet = sxssfWorkbook.getSheetAt(0);
        String[] strings = new String[]
                {"词条编号","词条类型","一级词条","二级词条","三级词条","四级词条","备注","审核状态"};
        //添加结果数据
        int excelRowNum = 0;
        int count = list.size();
        ListIterator<EntryFlow> rs = list.listIterator();
        SXSSFRow row = sheet.createRow(excelRowNum++);
        for (int i = 0; i <strings.length; i++) {
            row.createCell(i).setCellValue(parseText(strings[i]));
        }
        while (rs.hasNext()) {
            EntryFlow entryFlow = rs.next();
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
            row.createCell(7).setCellValue(parseText(AuditStatusEnum.getOptByCode(Integer.parseInt(entryFlow.getAuditStatus())).getTip()));
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
