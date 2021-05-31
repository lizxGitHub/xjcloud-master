package gov.pbc.xjcloud.provider.contract.controller.auditManage;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import gov.pbc.xjcloud.provider.contract.constants.CommonConstants;
import gov.pbc.xjcloud.provider.contract.constants.DelConstants;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckListNew;
import gov.pbc.xjcloud.provider.contract.entity.PlanTimeTemp;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.AuditPlanInfo;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanInfo;
import gov.pbc.xjcloud.provider.contract.entity.entry.EntryInfo;
import gov.pbc.xjcloud.provider.contract.enumutils.ExportPlanHeaderEnum;
import gov.pbc.xjcloud.provider.contract.enumutils.PlanStatusEnum;
import gov.pbc.xjcloud.provider.contract.exceptions.AppException;
import gov.pbc.xjcloud.provider.contract.feign.activiti.AuditActivitiService;
import gov.pbc.xjcloud.provider.contract.feign.user.UserCenterService;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanCheckListServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.PlanTimeTempServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.AuditPlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanInfoServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.auditManage.PlanManagementServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryCategoryServiceImpl;
import gov.pbc.xjcloud.provider.contract.service.impl.entry.EntryServiceImpl;
import gov.pbc.xjcloud.provider.contract.utils.*;
import gov.pbc.xjcloud.provider.contract.vo.DeptVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.NestedServletException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 */
@RestController
@RequestMapping("/audit-api/planCheckList")
@Slf4j
public class PlanCheckListController {

    @Resource
    private AuditPlanInfoServiceImpl auditPlanInfoServiceImpl;

    @Resource
    private PlanCheckListServiceImpl planCheckListService;

    @Resource
    private EntryCategoryServiceImpl categoryService;

    @Resource
    private EntryServiceImpl entryService;

    @Resource
    private PlanInfoServiceImpl planInfoService;

    @Autowired
    private AuditActivitiService auditActivitiService;

    @Autowired
    private UserCenterService userCenterService;

    @Resource
    private PlanTimeTempServiceImpl planTimeTempService;

    @Autowired
    private PlanManagementServiceImpl planManagementService;

    @Autowired
    AuditActivitiService activitiService;

    @Value("${audit.flow-key:auditApply}")
    private String auditFlowDefKey;

//    final String[] planExcelHeader = new String[]
//            {"计划名称","项目类型","审计性质","问题词条","问题严重程度","整改情况","问题定性","问题描述","可能影响","整改建议","审计分类","风险评估","审计依据","审计经验"};

    /**
     * 获取审计计划
     *
     * @return
     */
    @ApiOperation("审计页面信息")
    @GetMapping(value = {"page", ""})
    public R<Page<PlanCheckListNew>> index(HttpServletRequest request, PlanCheckListNew query, Page<PlanCheckListNew> page) {
        PageUtil.initPage(page);
        int type = Integer.parseInt(request.getParameter("type"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String status = request.getParameter("status");
        try {
            page = planCheckListService.selectAll(page, query, type, userId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @GetMapping(value = {"statuses", ""})
    public R<Page<PlanCheckListNew>> statuses(PlanCheckListNew query, Page<PlanCheckListNew> page, String statuses) {
        PageUtil.initPage(page);
        String[] arry = statuses.split(",");
        try {
            page = planCheckListService.selectByStatuses(page, query, arry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(page);
    }

    @PostMapping("/saveOrEditPlan")
    public R<Boolean> saveOrEditPlan(@RequestBody PlanCheckListNew planCheckList) {
        R<Boolean> r = new R<>();
        try {
            String fileUri = planCheckList.getFileUri();
//            planCheckListService.validate(planCheckList, r);//  此处没有对字段添加约束，所以不会生效
            if (null == planCheckList.getId() || planCheckList.getId() == 0) {
                planCheckList.setProjectCode(new PlanCheckList().generateProjectCode());
                planCheckList.setDelFlag(DelConstants.EXITED);
                planCheckList.setStatus("0");
                planCheckList.setCreatedTime(new Date());
                planCheckListService.save(planCheckList);
                QueryWrapper<PlanCheckListNew> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("project_code", planCheckList.getProjectCode());
                int planId = 0;
                try {
                    planId = planCheckListService.getOne(queryWrapper).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<PlanInfo> planInfoList = new ArrayList<>();
                PlanInfo planInfo1 = new PlanInfo();
                planInfo1.setUserId(planCheckList.getImpUserId());
                planInfo1.setStatusUser("1001");
                planInfo1.setPlanId(planId);
                planInfo1.setType(0);
                planInfoList.add(planInfo1);
                planInfoService.saveBatch(planInfoList);
            } else {
//                planCheckListService.updatePlanById(planCheckList);
                planCheckListService.updateBatchById(Arrays.asList(planCheckList), 1);
            }
            if (StringUtils.isNotBlank(fileUri)) {
                PlanFile planFile = new PlanFile();
                planFile.setId(IdGenUtil.uuid());
                planFile.setTaskId(0);
                planFile.setTaskName("计划呈报");
                planFile.setFileUri(fileUri);
                planFile.setBizKey(planCheckList.getId());
                planFile.setUploadUser(planCheckList.getCreatedUsername());
                planFile.setCreatedTime(new Date());
                planManagementService.addFileLog(planFile);
            }
        } catch (Exception e) {
            r.setCode(1002);
            r.setMsg("系统错误");
            e.getMessage();
            return r;
        }
        return r;
    }

    @PostMapping("/editBatchByIds")
    public R<Boolean> editBatchByIds(
            HttpServletRequest request,
            @RequestParam(name = "ids", required = true) String ids,
            @RequestParam(name = "implementingAgencyId", required = true) String implementingAgencyId,
            @RequestParam(name = "implementingAgencyName", required = true) String implementingAgencyName,
            @RequestParam(name = "impAdmin", required = true) int impAdmin,
            @RequestParam(name = "auditObjectId", required = true) String auditObjectId,
            @RequestParam(name = "auditObjectIdNew", required = true) String auditObjectIdNew,
            @RequestParam(name = "auditAdmin", required = true) int auditAdmin) {
        R<Boolean> r = new R<>();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
            plan.setImplementingAgencyId(implementingAgencyId);
            plan.setImplementingAgencyName(implementingAgencyName);
            plan.setImpAdminId(impAdmin);
            plan.setAuditObjectId(auditObjectId);
            plan.setAuditAdminId(auditAdmin);
            plan.setAuditObjectIdNew(auditObjectIdNew);
            planCheckListService.updatePlanById(plan);
        }
        return r.setData(true);
    }

    /**
     * 更改状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/changePlanStatusByIds")
    public R<Boolean> changePlanStatusByIds(
            HttpServletRequest request,
            @RequestParam(name = "ids", required = true) String ids,
            @RequestParam(name = "statusUser", required = true) String statusUser,
            @RequestParam(name = "userId", required = true) int userId,
            @RequestParam(name = "fileUri", required = false) String fileUri,
            @RequestParam(name = "uploadUser", required = false) String uploadUser) {
        R<Boolean> r = new R<>();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                PlanCheckListNew plan = planCheckListService.selectById(Integer.valueOf(id));
                String msg = "请完善项目名称为 " + plan.getProjectName() + "，编号为" + plan.getProjectCode() + "的项目";
                if (StringUtils.isBlank(plan.getAuditYear())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProjectName())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProjectCode())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getImplementingAgencyId()) || plan.getImpAdminId() == 0) {
                    r.setCode(1002);
                    r.setMsg(msg);
//                } else if (StringUtils.isBlank(plan.getAuditObjectId())) {
//                    r.setCode(1002);
//                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getAuditNatureId())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getQuestionType())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProblemSeverityId())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProblemSeverityId())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getRectifySituationId())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getQuestionEntryId())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProblemCharacterization())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                } else if (StringUtils.isBlank(plan.getProblemDescription())) {
                    r.setCode(1002);
                    r.setMsg(msg);
                }
                if (r.getCode() == 1002) {
                    return r;
                }
                if (StringUtils.isNotBlank(fileUri)) {
                    PlanFile planFile = new PlanFile();
                    planFile.setId(IdGenUtil.uuid());
                    planFile.setTaskId(0);
                    planFile.setTaskName("驳回文件");
                    planFile.setFileUri(fileUri);
                    planFile.setBizKey(plan.getId());
                    planFile.setUploadUser(uploadUser);
                    planFile.setCreatedTime(new Date());
                    planManagementService.addFileLog(planFile);
                }
                if (userId == plan.getImpAdminId()) {
                    if (statusUser.equals("1002")) { //确认
                        if (plan.getAuditObjectId() != null && !"-1".equals(plan.getAuditObjectId())) {
                            List list = (List) userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(plan.getAuditObjectId()), "审计对象负责人员角色").getData();
                            if (list.size() < 1) {
                                return r.setData(false);
                            }
                            //审计对象管理员
                            for (int i = 0; i < list.size(); i++) {
                                Map m = (Map) list.get(i);
                                PlanInfo planInfo3 = new PlanInfo();
                                planInfo3.setUserId(Integer.valueOf(String.valueOf(m.get("userId"))));
                                planInfo3.setStatusUser("1004"); //待完善
                                planInfo3.setPlanId(plan.getId());
                                planInfo3.setType(1);
                                planInfoService.save(planInfo3);
                            }
                        }

                        planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), "1003");

                        //实施一般员工
                        PlanInfo planInfo1 = new PlanInfo();
                        planInfo1.setUserId(plan.getImpUserId());
                        planInfo1.setStatusUser("1001"); //待完善
                        planInfo1.setPlanId(plan.getId());
                        planInfo1.setType(1);
                        planInfoService.save(planInfo1);
                        //实施管理员
                        PlanInfo planInfo2 = new PlanInfo();
                        planInfo2.setUserId(plan.getImpAdminId());
                        planInfo2.setStatusUser("1003"); //已批准
                        planInfo2.setPlanId(plan.getId());
                        planInfo2.setType(1);
                        planInfoService.save(planInfo2);

                        //项目启动时间
                        plan.setStartTime(new Date());
                        plan.setStatus("1001"); //正在实施
                        plan.setAuditStatus1(String.valueOf(PlanStatusEnum.PLAN_IMP_REJECT.getCode()));
                        planCheckListService.updatePlanById(plan);

                        PlanTimeTemp planTimeTemp = planTimeTempService.getByPlanId(plan.getId());
                        if (planTimeTemp == null) {
                            planTimeTemp = new PlanTimeTemp();
                            planTimeTemp.setPlanId(plan.getId());
                            planTimeTemp.setStartTimeAll(new Date());
                            planTimeTempService.save(planTimeTemp);
                        }

                    } else if (statusUser.equals("1003")) { //驳回
                        PlanInfo planInfo = planInfoService.getPlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()));
                        if (request.getParameter("opinion") != null) {
                            planInfo.setOpinion(request.getParameter("opinion"));
                        }
                        planInfo.setStatusUser("1004");
                        planInfoService.updateById(planInfo);

                        //新驳回逻辑
                        if (request.getParameter("opinion") != null) {
                            Date time = new Date();
                            AuditPlanInfo auditPlanInfo = new AuditPlanInfo();
                            auditPlanInfo.setPlanId(plan.getId());
                            auditPlanInfo.setUserId(plan.getImpUserId());
                            auditPlanInfo.setOpinion(request.getParameter("opinion"));
                            auditPlanInfo.setNextUserId(plan.getImpAdminId());
                            auditPlanInfo.setCreateTime(time);
                            auditPlanInfo.setTaskName("项目初始审核");
                            auditPlanInfoServiceImpl.save(auditPlanInfo);
                        }

                    }
                    planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), statusUser);
                } else if (userId == plan.getImpUserId()) {
                    planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpUserId()), statusUser);
                    if (statusUser.equals("1002")) { //上报
                        PlanInfo planInfo = planInfoService.getPlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()));
                        if (planInfo == null) {
                            PlanInfo planInfo1 = new PlanInfo();
                            planInfo1.setUserId(plan.getImpAdminId());
                            planInfo1.setStatusUser("1001");
                            planInfo1.setPlanId(plan.getId());
                            planInfo1.setType(0);
                            planInfoService.save(planInfo1);
                        } else {
                            planInfoService.updatePlanByPlanUserId(String.valueOf(plan.getId()), String.valueOf(plan.getImpAdminId()), "1001");
                        }
                    }
                }

                //启动流程
                if (userId == plan.getImpAdminId() && statusUser.equals("1002")) {

                    String auditFlowDefKeyStr = auditFlowDefKey;
                    int createdBy = plan.getCreatedBy(); //创建人
                    int impUserAssignee = plan.getImpUserId(); //
                    int implLeaderAssignee = plan.getImpAdminId(); //
                    int auditUserAssignee = plan.getAuditUserId(); //
                    int auditLeaderAssignee = plan.getAuditAdminId(); //

                    List<Integer> auditLeaderAssigneeList = new ArrayList<>();
                    List<Integer> auditUserInnerList = new ArrayList<>();
                    if (plan.getAuditObjectId() != null) {
                        List list = (List) userCenterService.getUsersByRoleNameAndDept(Integer.valueOf(plan.getAuditObjectId()), "审计对象负责人员角色").getData();
                        for (int i = 0; i < list.size(); i++) {
                            Map m = (Map) list.get(i);
                            auditLeaderAssigneeList.add(Integer.valueOf(String.valueOf(m.get("userId"))));
                        }
                    }
                    JSONObject varsJSONObject = new JSONObject();
                    int auditStatus = 1004;
                    if (plan.getAuditObjectIdNew() != null && !(plan.getAuditObjectIdNew().equals(plan.getImplementingAgencyId()))) {
                        auditStatus = 1018;
                        int nsDeptId = 0;
                        List<DeptVO> deptList = deptUtil.findChildBank(Integer.valueOf(plan.getAuditObjectIdNew()), "内审科");
                        for (int i = 0; i < deptList.size(); i++) {
                            DeptVO deptVO = deptList.get(i);
                            nsDeptId = deptVO.getDeptId();
                            break;
                        }
                        List listns = (List) userCenterService.getUsersByRoleNameAndDept(nsDeptId, "内审管理员").getData();
                        for (int i = 0; i < listns.size(); i++) {
                            Map m = (Map) listns.get(i);
                            auditUserInnerList.add(Integer.valueOf(String.valueOf(m.get("userId"))));
                            //内审人员
                            PlanInfo planInfo1 = new PlanInfo();
                            planInfo1.setUserId(Integer.valueOf(String.valueOf(m.get("userId"))));
                            planInfo1.setStatusUser("1006"); //下发
                            planInfo1.setPlanId(plan.getId());
                            planInfo1.setType(1);
                            planInfoService.save(planInfo1);
                        }
                        auditFlowDefKeyStr = auditFlowDefKeyStr + "_1";
                    }
                    varsJSONObject.put("impUserAssignee", impUserAssignee);
                    varsJSONObject.put("impLeaderAssignee", implLeaderAssignee);
                    varsJSONObject.put("auditUserAssignee", auditUserAssignee);
//                    varsJSONObject.put("auditLeaderAssignee", auditLeaderAssignee);
                    varsJSONObject.put("auditLeaderAssigneeList", auditLeaderAssigneeList);
                    varsJSONObject.put("auditUserInnerList", auditUserInnerList);
                    varsJSONObject.put("passAB", true);
                    varsJSONObject.put("createdBy", createdBy);
                    varsJSONObject.put("auditStatus", auditStatus);
                    varsJSONObject.put("delayDate", null);
                    varsJSONObject.put("projectName", plan.getProjectName());
                    varsJSONObject.put("projectCode", plan.getProjectCode());
                    varsJSONObject.put("implementingAgencyId", plan.getImplementingAgencyId());
                    varsJSONObject.put("auditObjectId", plan.getAuditObjectId());
                    varsJSONObject.put("auditNatureId", plan.getAuditNatureId());
                    varsJSONObject.put("auditYear", plan.getAuditYear());
                    varsJSONObject.put("status", plan.getStatus());

                    String vars = varsJSONObject.toJSONString();
                    //启动流程
                    R2<Boolean> auditApply = auditActivitiService.start(auditFlowDefKeyStr, Integer.valueOf(id), vars);
                    if (!auditApply.getData()) {
                        return r.setMsg("流程启动失败:" + auditApply.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.setData(true);
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @GetMapping("template/download")
    @ApiOperation("计划批量导入模板")
    public void getWordFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(CommonConstants.PlanSheetName);
            Map<String, Integer> cateNameMap = categoryService.list().stream().
                    filter(e -> StringUtils.isNotBlank(e.getName())).collect(Collectors.toMap(e -> e.getName(), x -> x.getId(), (e1, e2) -> e1));
            Map<Integer, List<EntryInfo>> entryGroup = entryService.listAll().stream().
                    filter(e -> StringUtils.isNotBlank(e.getConcatName())).collect(Collectors.groupingBy(e -> e.getCategoryFk()));
            AtomicInteger rowIndex = new AtomicInteger();
            AtomicInteger colIndex = new AtomicInteger();
            XSSFRow row = sheet.createRow(rowIndex.get());
            DataValidationHelper helper = sheet.getDataValidationHelper();//设置下拉框xlsx格式
            for (ExportPlanHeaderEnum header : ExportPlanHeaderEnum.values()) {
                XSSFCell cell = row.createCell(colIndex.getAndIncrement());
                cell.setCellValue(header.getName());
                cell.setCellStyle(getCellStyle(workbook, true));
                if (cateNameMap.containsKey(header.getName())) {
                    Integer cateFk = cateNameMap.get(header.getName());
                    List<EntryInfo> entryInfos = entryGroup.get(cateFk);
                    String[] objects = null;
                    if (null == entryInfos) {
                        continue;
                    }
                    objects = entryInfos.stream().filter(Objects::nonNull).map(e -> e.getConcatName()).toArray(String[]::new);
                    creatDropDownList(sheet, helper, objects, rowIndex.get() + 1, (1 << 16) - 1, colIndex.get() - 1, colIndex.get() - 1);
                }
            }
            response.setContentType("application/binary;charset=ISO8859-1");
            String fileName = java.net.URLEncoder.encode("计划导入模板", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    public static CellStyle getCellStyle(Workbook workbook, boolean bold) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(bold);//加粗
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    //创建下拉框
    public static void creatDropDownList(Sheet sheet, DataValidationHelper helper, String[] list,
                                         Integer firstRow, Integer lastRow, Integer firstCol, Integer lastCol) {
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(list);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    @Autowired
    private DeptUtil deptUtil;

    private ConcurrentHashMap<String, Integer> deptNameValue = new ConcurrentHashMap<>();

    @PostMapping("import")
    public R<Boolean> importEntry(@RequestParam("file") MultipartFile file, @RequestParam("createdBy") Integer createdBy,
                                  @RequestParam("implementingAgencyId") String implementingAgencyId,
                                  @RequestParam("implementingAgencyNewId") String implementingAgencyNewId
    ) {
        Sheet planSheet;
        if (null == file) {
            return R.failed("上传文件不能为空");
        }
        boolean result = false;
        List<DeptVO> finalList = Lists.newArrayList();
        List<DeptVO> bank = deptUtil.findChildBank(0, "");
        bank.forEach(e -> {
            finalList.add(e);
            List<DeptVO> tempList = deptUtil.findChildBank(e.getDeptId(), "");
            finalList.addAll(tempList);
        });
        Map<String, Integer> tempMap = finalList.stream().collect(Collectors.toMap(e -> e.getName(), e -> e.getDeptId(), (e1, e2) -> e1));
        Map<String, DeptVO> deptDict = finalList.stream().collect(Collectors.toMap(e -> String.valueOf(e.getDeptId()), e -> e, (e1, e2) -> e1));
        Map<String, String> deptMap = finalList.stream().collect(Collectors.toMap(e -> String.valueOf(e.getDeptId()), e -> e.getName(), (e1, e2) -> e1));
            deptNameValue.clear();
        deptNameValue.putAll(tempMap);
        List<PlanCheckList> planList = null;
        String fileName = file.getOriginalFilename();
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(fileName)) {
                throw new RuntimeException("导入文档为空!");
            } else if (fileName.toLowerCase().endsWith("xls")) {
            } else if (fileName.toLowerCase().endsWith("xlsx")) {
            } else {
                throw new RuntimeException("文档格式不正确!");
            }

            Map<String, String> entryNameValue = entryService.listAll().stream().filter(e -> StringUtils.isNotBlank(e.getConcatName())).
                    collect(Collectors.toMap(e -> e.getConcatName(), e -> e.getId(), (e1, e2) -> e1));
            planSheet = ExcelUtil.getReader(file.getInputStream(), CommonConstants.PlanSheetName).setIgnoreEmptyRow(true).getSheet();
            checkExcelHeader(planSheet);
            if (planSheet == null) {
                throw new RuntimeException("请使用模板导入!");
            }
            if (planSheet.getLastRowNum() < 1) {
                throw new RuntimeException("工作表数据为空!");
            }
            int maxRow = planSheet.getLastRowNum();
            AtomicInteger colIndex = null;
            PlanCheckList plan = null;
            List<String> prjCode = new ArrayList<>(maxRow);
            planList = new ArrayList<>(maxRow - 1);
            StringJoiner error = new StringJoiner("\n");
            ExportPlanHeaderEnum[] headerEnums = ExportPlanHeaderEnum.values();
            Row headRow = planSheet.getRow(0);
            colIndex = new AtomicInteger(0);
            for (ExportPlanHeaderEnum headerEnum : headerEnums) {
                Cell cell = headRow.getCell(colIndex.getAndIncrement());
                if(cell==null || !headerEnum.getName().equals(cell.getStringCellValue())){
                    throw new AppException("您的模板不正确，请下载最新的模板导入数据");
                }
            }
            for (int startRow = 1; startRow <= maxRow; startRow++) {
                colIndex = new AtomicInteger();
                Row row = planSheet.getRow(startRow);
                plan = new PlanCheckList();
                // 枚举类字段反射
                initPlanProperty(plan, row, colIndex, startRow, entryNameValue, error);
                String auditObjectId = plan.getAuditObjectId();
                String auditObjectIdNew = plan.getAuditObjectIdNew();
                if (StrUtil.isBlank(auditObjectId) || !deptNameValue.containsKey(auditObjectId)) {
//                    throw new RuntimeException(String.format("不存在的审计对象中支：%s",auditObjectId));
                    plan.setAuditObjectId("");
                } else {
                    plan.setAuditObjectId(deptNameValue.get(auditObjectId).toString());
                }
                if (StrUtil.isBlank(auditObjectIdNew) || !deptNameValue.containsKey(auditObjectIdNew)) {
//                    throw new RuntimeException(String.format("不存在的审计对象部门：%s",auditObjectIdNew));
                    plan.setAuditObjectIdNew("");
                } else {
                    plan.setAuditObjectIdNew(deptNameValue.get(auditObjectIdNew).toString());
                }
                plan.setConcatQuestionEntry();
//                int code = (int) ((Math.random() * 9 + 1) * 1000);
                plan.setImplementingAgencyId(implementingAgencyId);
//                if (StrUtil.isNotBlank(implementingAgencyNewId) && implementingAgencyNewId.equals("10000")) {
//                    plan.setImplementingAgencyNewId(implementingAgencyNewId);
//                    plan.setImplementingAgencyName("乌鲁木齐中支");
//                }
                plan.setImplementingAgencyNewId(implementingAgencyNewId);
                // 看他是不是中支，中支的上级机构id是0
                int parentId = deptDict.get(implementingAgencyId).getParentId();
                if (parentId == 0) {
                    plan.setImplementingAgencyName(deptMap.get(implementingAgencyId));
                } else {
                    plan.setImplementingAgencyName(deptMap.get(parentId));
                }
                plan.setImpUserId(createdBy);
                plan.setCreatedBy(createdBy);
                plan.setStatus("0");
                plan.setCreatedTime(new Date());
                plan.setProjectCode(plan.generateProjectCode());
                prjCode.add(plan.getProjectCode());
                planList.add(plan);
            }
            if (error.length() > 0) {
                throw new AppException(error.toString());
            }
            QueryWrapper<PlanCheckList> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("project_code", prjCode);
            result = planManagementService.saveBatch(planList, planList.size());
            if (result) {
                List<PlanCheckList> queryPlan = planManagementService.list(queryWrapper);
                List<Integer> collect = queryPlan.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<PlanInfo> planInfos = new ArrayList<>(null == collect ? 0 : collect.size());
                collect.stream().forEach(e -> {
                    PlanInfo planInfo = new PlanInfo();
                    planInfo.setPlanId(e);
                    planInfo.setUserId(createdBy);
                    planInfo.setType(0);
                    planInfo.setStatusUser("1001");
                    planInfos.add(planInfo);
                });
                planInfoService.saveBatch(planInfos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new R<Boolean>(ApiErrorCode.FAILED).setMsg("请下载模板导入");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (AppException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        } catch (Exception e) {
            if (e instanceof NestedServletException) {
                return R.failed("登录信息过期，请重新登录");
            } else {
                return R.failed("上传失败，错误信息如下："+e.getMessage());
            }
        }
        return new R<Boolean>().setData(result).setMsg(result ? "成功导入" + planList.size() + "条数据" : "导入失败");
    }

    private void initPlanProperty(PlanCheckList plan, Row row, AtomicInteger colIndex, int startRow, Map<String, String> entryNameValue, StringJoiner error) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ExportPlanHeaderEnum[] headerEnums = ExportPlanHeaderEnum.values();
        for (int i = 0; i < headerEnums.length; i++) {
            ExportPlanHeaderEnum header = headerEnums[i];
            String column = header.getColumn();
            String settterMethod = plan.generateSetterMethod(column, "set");
            Method method = plan.getClass().getMethod(settterMethod, String.class);
            method.setAccessible(true);
            int i1 = colIndex.getAndIncrement();
            Cell cell = row.getCell(i1);
            if (header.isRequired() && (null == cell || StrUtil.isBlank(DrExcelUtil.getStringVal(cell)))) {
                error.add("第" + (i1 + 1) + "列" + ",第" + (startRow + 1) + "行【" + header.getName() + "】数据不能为空");
                continue;
            }
            String value = DrExcelUtil.getStringVal(cell);
            if(header.getIsEntry()){
                if (entryNameValue.containsKey(value)) {
                    value = entryNameValue.get(value);
                }else{
                    error.add("第" + (i1 + 1) + "列" + ",第" + (startRow + 1) + "行【" + header.getName() + "】使用了不存在的模板数据");
                    continue;
                }
            }

            method.invoke(plan, new Object[]{value});
        }
//        Cell cellprojectName = row.getCell(colIndex.getAndIncrement());
//        Cell cellprojectType = row.getCell(colIndex.getAndIncrement());
//        Cell cellauditNatureId = row.getCell(colIndex.getAndIncrement());
//        Cell cellquestionEntryId = row.getCell(colIndex.getAndIncrement());
//        Cell cellproblemSeverityId = row.getCell(colIndex.getAndIncrement());
//        Cell cellrectifySituationId = row.getCell(colIndex.getAndIncrement());
//        Cell cellproblemCharacterization = row.getCell(colIndex.getAndIncrement());
//        Cell cellproblemDescription = row.getCell(colIndex.getAndIncrement());
//        Cell cellmayAffect = row.getCell(colIndex.getAndIncrement());
//        Cell cellrectificationSuggestions = row.getCell(colIndex.getAndIncrement());
//        Cell cellauditClassificationId = row.getCell(colIndex.getAndIncrement());
//        Cell cellriskAssessmentId = row.getCell(colIndex.getAndIncrement());
//        Cell cellauditBasis = row.getCell(colIndex.getAndIncrement());
//        Cell cellauditingExperience = row.getCell(colIndex.getAndIncrement());
//        //----------
//        cellprojectName.setCellType(CellType.STRING);
//        cellprojectType.setCellType(CellType.STRING);
//        cellauditNatureId.setCellType(CellType.STRING);
//        cellquestionEntryId.setCellType(CellType.STRING);
//        cellproblemSeverityId.setCellType(CellType.STRING);
//        cellrectifySituationId.setCellType(CellType.STRING);
//        cellproblemCharacterization.setCellType(CellType.STRING);
//        cellproblemDescription.setCellType(CellType.STRING);
//        cellmayAffect.setCellType(CellType.STRING);
//        cellrectificationSuggestions.setCellType(CellType.STRING);
//        cellauditClassificationId.setCellType(CellType.STRING);
//        cellriskAssessmentId.setCellType(CellType.STRING);
//        cellauditBasis.setCellType(CellType.STRING);
//        cellauditingExperience.setCellType(CellType.STRING);
//
//        String projectName = cellprojectName.getStringCellValue();//计划名称
//        String projectType = cellprojectType.getStringCellValue();//项目类型
//        String auditNatureId = cellauditNatureId.getStringCellValue();//审计性质
//        String questionEntryId = cellquestionEntryId.getStringCellValue();//问题词条
//        String problemSeverityId = cellproblemSeverityId.getStringCellValue();//问题严重程度
//        String rectifySituationId = cellrectifySituationId.getStringCellValue();//整改情况
//        String problemCharacterization = cellproblemCharacterization.getStringCellValue();//问题定性
//        String problemDescription = cellproblemDescription.getStringCellValue();//问题描述
//        String mayAffect = cellmayAffect.getStringCellValue();//可能影响
//        String rectificationSuggestions = cellrectificationSuggestions.getStringCellValue();//整改建议
//        String auditClassificationId = cellauditClassificationId.getStringCellValue();//审计分类
//        String riskAssessmentId = cellriskAssessmentId.getStringCellValue();//风险评估
//        String auditBasis = cellauditBasis.getStringCellValue();//审计依据
//        String auditingExperience = cellauditingExperience.getStringCellValue();//审计经验
//        plan.setProjectName(projectName);
//        plan.setProjectType(setEntryValue(entryNameValue, projectType));
//        plan.setAuditNatureId(setEntryValue(entryNameValue, auditNatureId));
//        plan.setQuestionEntryId(setEntryValue(entryNameValue, questionEntryId));
//        plan.setProblemSeverityId(setEntryValue(entryNameValue, problemSeverityId));
//        plan.setRectifySituationId(setEntryValue(entryNameValue, rectifySituationId));
//        plan.setProblemCharacterization(setEntryValue(entryNameValue, problemCharacterization));
//        plan.setProblemDescription(problemDescription);
//        plan.setMayAffect(mayAffect);
//        plan.setRectificationSuggestions(rectificationSuggestions);
//        plan.setAuditClassificationId(setEntryValue(entryNameValue, auditClassificationId));
//        plan.setRiskAssessmentId(setEntryValue(entryNameValue, riskAssessmentId));
//        plan.setAuditBasis(auditBasis);
//        plan.setAuditingExperience(auditingExperience);
    }


    private String setEntryValue(Map<String, String> entryNameValue, String entryName) {
        if (entryNameValue.containsKey(entryName)) {
            return entryNameValue.get(entryName);
        }
        return "";
    }

    private void checkExcelHeader(Sheet planSheet) {
        Row row = planSheet.getRow(0);
        AtomicInteger colIndex = new AtomicInteger();
        for (ExportPlanHeaderEnum header : ExportPlanHeaderEnum.values()) {
            String stringCellValue = row.getCell(colIndex.getAndIncrement()).getStringCellValue();
            if (StringUtils.isBlank(stringCellValue) || !header.getName().equals(stringCellValue)) {
                throw new RuntimeException("模板出错，请使用下载模板上传文件！");
            }
        }
    }
}
