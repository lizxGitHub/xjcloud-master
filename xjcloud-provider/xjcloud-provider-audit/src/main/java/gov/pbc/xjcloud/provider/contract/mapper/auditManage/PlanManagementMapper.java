package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.dto.PlanCheckListDTO;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.entity.PlanOverTimeTip;
import gov.pbc.xjcloud.provider.contract.entity.auditManage.PlanFile;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
import gov.pbc.xjcloud.provider.contract.vo.PlanFileVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PlanManagementMapper extends IBaseMapper<PlanCheckList> {

    /**
     * 查询分页对象
     * @param page
     * @param query
     * @return
     */
    List<PlanCheckList> selectPlanCheckList(@Param("page") Page page, @Param("query") PlanCheckList query);

    List<PlanCheckList> selectPlanCheckListByAdmin(@Param("page") Page page, @Param("query") PlanCheckList query);

    /**
     * 根据categoryId获取词条
     * @param categoryId
     * @return
     */
    List<Map<String, Object>> selectEntryByCategoryId(@Param("categoryId") String categoryId);

    Map<String, Object> selectEntryById(@Param("id") String id);

    /**
     * 根据条件查询结果集
     * @param query
     * @return
     */
    List<Map<String, Object>> selectEntryByQuery(@Param("query") PlanCheckList query, @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo);

    /**
     * 根据条件查询结果集
     * @param query
     * @return
     */
    @Select({"<script>",
            "select count(1) count"
                    + " from plan_check_list pcl"
                    + " left join entry_info audit_nature_entry on pcl.audit_nature_id=audit_nature_entry.id and audit_nature_entry.del_flag=0 and audit_nature_entry.category_fk=4"
                    + " left join entry_info problem_severity_entry on pcl.problem_severity_id=problem_severity_entry.id and problem_severity_entry.del_flag=0 and problem_severity_entry.category_fk=5"
                    + " left join entry_info rectify_situation_entry on pcl.rectify_situation_id=rectify_situation_entry.id and rectify_situation_entry.del_flag=0 and rectify_situation_entry.category_fk=6"
                    + " left join entry_info risk_assessment_entry on pcl.risk_assessment_id=risk_assessment_entry.id and risk_assessment_entry.del_flag=0  and risk_assessment_entry.category_fk=9"
                    + " where pcl.del_flag=0 "
                    + " <if test='query.projectName!=null and query.projectName!=\"\"'> and pcl.project_name like '%${query.projectName}%'</if>"
                    + " <if test='query.problemDescription!=null and query.problemDescription!=\"\"'> and (pcl.problem_characterization like '%${query.problemDescription}%' or pcl.problem_description like '%${query.problemDescription}%')</if>"
                    + " <if test='query.auditYear!=null and query.auditYear!=\"\"'> and (pcl.audit_year like '%${query.auditYear}%')</if>"
                    + " <if test='query.implementingAgencyId!=null and query.implementingAgencyId!=\"\"'> and (pcl.implementing_agency_id  in ('${query.implementingAgencyId}'))</if>"
                    + " <if test='query.auditNatureId!=null and query.auditNatureId!=\"\"'> and (pcl.audit_nature_id = '${query.auditNatureId}')</if>"
                    + " <if test='query.auditObjectId!=null and query.auditObjectId!=\"\"'> and (pcl.audit_object_id = '${query.auditObjectId}')</if>"
                    + " <if test='query.problemSeverityId!=null and query.problemSeverityId!=\"\"'> and (pcl.problem_severity_id = '${query.problemSeverityId}')</if>"
                    + " <if test='query.rectifySituationId!=null and query.rectifySituationId!=\"\"'> and (pcl.rectify_situation_id = '${query.rectifySituationId}')</if>"
                    + "",
            "</script>"})
    List<Map<String, Object>> countEntryByQuery(@Param("query") PlanCheckList query);

    /**
     * 分页查询（ByType）
     * @param page
     * @param query
     * @return
     */
    List<PlanCheckList> selectTypePage(@Param("page") Page<PlanCheckList> page, @Param("query") Map<String, Object> query);

    void cancelCheckAttention(@Param("userId") String userId, @Param("checkArr") String[] checkArr);

    void addCheckAttention(@Param("userId") String userId, @Param("checkArr") String[] checkArr);

    void addDeptYearReport(@Param("deptId") String deptId, @Param("auditYear") String auditYear, @Param("content") String content);
    void updateDeptYearReport(@Param("deptId") String deptId, @Param("auditYear") String auditYear, @Param("content") String content);
    List<Map<String, Object>> selectDeptYearReport(@Param("deptId") String deptId, @Param("auditYear") String auditYear);
    /**
     * 根据字段分组查询
     */
    @Select({"<script>",
            "select <if test='groupName!=null and groupName!=\"\"'>ifnull(${groupName},'其他') name ,</if>pcl.${groupField} id, count(1) value"
                    + " from plan_check_list pcl"
                    + " left join entry_info project_type_entry on pcl.project_type=project_type_entry.id and project_type_entry.del_flag=0 and project_type_entry.category_fk=3"
                    + " left join entry_info audit_nature_entry on pcl.audit_nature_id=audit_nature_entry.id and audit_nature_entry.del_flag=0 and audit_nature_entry.category_fk=4"
                    + " left join entry_info problem_severity_entry on pcl.problem_severity_id=problem_severity_entry.id and problem_severity_entry.del_flag=0 and problem_severity_entry.category_fk=5"
                    + " left join entry_info rectify_situation_entry on pcl.rectify_situation_id=rectify_situation_entry.id and rectify_situation_entry.del_flag=0 and rectify_situation_entry.category_fk=6"
                    + " left join entry_info risk_assessment_entry on pcl.risk_assessment_id=risk_assessment_entry.id and risk_assessment_entry.del_flag=0  and risk_assessment_entry.category_fk=9"
                    + " where pcl.del_flag=0  and pcl.status!='0'"
                    + " <if test='query.projectName!=null and query.projectName!=\"\"'> and pcl.project_name like '%${query.projectName}%'</if>"
                    + " <if test='query.problemDescription!=null and query.problemDescription!=\"\"'> and (pcl.problem_characterization like '%${query.problemDescription}%' or pcl.problem_description like '%${query.problemDescription}%')</if>"
                    + " <if test='query.questionEntryId!=null and query.questionEntryId!=\"\"'> and (pcl.question_Entry_Id like '%#{query.questionEntryId}%')</if>"
                    + " <if test='query.auditYear!=null and query.auditYear!=\"\"'> and (pcl.audit_year like '%${query.auditYear}%')</if>"
                    + " <if test='query.implementingAgencyId!=null and query.implementingAgencyId!=\"-1\"'> and (pcl.implementing_agency_id = '${query.implementingAgencyId}')</if>"
                    + " <if test='query.auditNatureId!=null and query.auditNatureId!=\"\"'> and (pcl.audit_nature_id = '${query.auditNatureId}')</if>"
                    + " <if test='query.auditObjectId!=null and query.auditObjectId!=\"\"'> and (pcl.audit_object_id = '${query.auditObjectId}')</if>"
                    + " <if test='query.problemSeverityId!=null and query.problemSeverityId!=\"\"'> and (pcl.problem_severity_id = '${query.problemSeverityId}')</if>"
                    + " <if test='query.rectifySituationId!=null and query.rectifySituationId!=\"\"'> and (pcl.rectify_situation_id = '${query.rectifySituationId}')</if>"
                    + " group by pcl.${groupField}  order by count(1) desc",
            "</script>"})
    List<Map<String, Object>> groupCountEntryByQuery(@Param("query") PlanCheckList query, @Param("groupName")String groupName, @Param("groupField")String groupField);

    @Select({"<script>",
            "SELECT\n" +
                    "	pcl1.implementing_agency_id deptId,\n" +
                    "	COUNT( pcl1.project_name ) proNum,\n" +
                    "	SUM( pcl1.error ) errorNum \n" +
                    "FROM\n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "		pcl.implementing_agency_id,\n" +
                    "		pcl.project_name,\n" +
                    "		SUM( pcl.error ) error \n" +
                    "	FROM\n" +
                    "		(\n" +
                    "		SELECT\n" +
                    "			p1.implementing_agency_id,\n" +
                    "			p1.project_name,\n" +
                    "		CASE\n" +
                    "				\n" +
                    "				WHEN p1.`status` IN ( 1001, 1004, 1005 ) THEN\n" +
                    "				1 ELSE 0 \n" +
                    "			END AS error \n" +
                    "		FROM\n" +
                    "			plan_check_list p1 \n" +
                    "		WHERE\n" +
                    "			p1.del_flag = 0 \n" +
                    " <if test='auditYear!=null and auditYear!=\"\"'> AND p1.audit_year = '${auditYear}' </if>"+
                    "		AND p1.`status` IN ( 1001, 1002, 1003, 1004, 1005 )) pcl \n" +
                    "	GROUP BY\n" +
                    "		pcl.implementing_agency_id,\n" +
                    "		pcl.project_name \n" +
                    "	) pcl1 \n" +
                    "GROUP BY\n" +
                    "	pcl1.implementing_agency_id",
            "</script>"})
    List<Map<String, Object>> groupCountEntry(@Param("auditYear")String auditYear);

    @Select({"<script>",
            "SELECT\n" +
                    "	tb1.type,\n" +
                    "	tb1.num,\n" +
                    "	ei.`name`\n" +
                    "FROM\n" +
                    "	(\n" +
                    "		SELECT\n" +
                    "			a.project_type type,\n" +
                    "			COUNT(a.project_type) num\n" +
                    "		FROM\n" +
                    "			plan_check_list a\n" +
                    "		WHERE\n" +
                    "			a.del_flag = 0\n" +
                    " <if test='auditYear!=null and auditYear!=\"\"'> AND a.audit_year = '${auditYear}' </if>"+
                    "		AND a.implementing_agency_id = '${deptId}'\n" +
                    "		AND a.`status` IN (1001, 1002, 1003, 1004)\n" +
                    "		GROUP BY\n" +
                    "			a.project_type\n" +
                    "	) tb1\n" +
                    "LEFT JOIN entry_info ei ON tb1.type = ei.id\n" +
                    "ORDER BY\n" +
                    "	tb1.num DESC\n" +
                    "",
            "</script>"})
    List<Map<String, Object>> groupCountProType(@Param("auditYear")String auditYear, @Param("deptId")String deptId);

    @Select({"<script>",
            "SELECT\n" +
                    "	tb1.status,\n" +
                    "	tb1.`name`\n" +
                    "FROM\n" +
                    "	(\n" +
                    "		SELECT\n" +
                    "			a.status,\n" +
                    "			a.project_name name\n" +
                    "		FROM\n" +
                    "			plan_check_list a\n" +
                    "		WHERE\n" +
                    "			a.del_flag = 0\n" +
                    " <if test='auditYear!=null and auditYear!=\"\"'> AND a.audit_year = '${auditYear}' </if>"+
                    "		AND a.implementing_agency_id = '${deptId}'\n" +
                    "       and a.`status`!='0' " +
                    "	) tb1\n" +
                    "",
            "</script>"})
    List<Map<String, Object>> groupCountProName(@Param("auditYear")String auditYear, @Param("deptId")String deptId);


    List<Map<String, Object>> countPlan(@Param("agencyId")String agencyId, @Param("auditYear")String auditYear);


    @Select({"<script>",
            "SELECT\n" +
                    "	-1 implementingAgencyId,\n" +
                    "	sum( CASE WHEN pcl.STATUS != '0' THEN 1 ELSE 0 END ) projectCount,\n" +
                    "	IFNULL( sum( CASE WHEN pcl.STATUS = '1003' THEN 1 ELSE 0 END ), 0 ) finishCount,\n" +
                    "	IFNULL( sum( CASE WHEN pcl.STATUS != '1003' AND pcl.STATUS != '0' THEN 1 ELSE 0 END ), 0 ) noFinishCount,\n" +
                    "	IFNULL( sum( CASE WHEN pcl.STATUS = '1004' THEN 1 ELSE 0 END ), 0 ) timeoutCount,\n" +
                    "	IFNULL( sum( CASE WHEN pcl.STATUS = '1005' THEN 1 ELSE 0 END ), 0 ) overTimeNum \n" +
                    "FROM\n" +
                    "	plan_check_list pcl \n" +
                    "WHERE\n" +
                    "	pcl.del_flag = '0' " +
                    " <if test='auditYear!=null and auditYear!=\"\"'> and pcl.audit_year like '%${auditYear}%' </if>"
                    +" <if test='pageStart!=null and pageNo!=null'> limit ${pageStart},${pageNo}</if>",
            "</script>"})
    List<Map<String, Object>> statisticPlanReportAll( @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo, @Param("auditYear")String auditYear);
    @Select({"<script>",
            "select pcl.implementing_agency_id implementingAgencyId,sum(CASE WHEN pcl. STATUS != '0' THEN 1 ELSE 0 END) projectCount,IFNULL(sum(case when pcl.status='1003' then 1 else 0 end),0) finishCount,"
                    +"IFNULL(sum(case when pcl. STATUS != '1003' and pcl. STATUS != '0' then 1 else 0 end),0) noFinishCount,IFNULL(sum(case when pcl.status='1004' then 1 else 0 end),0) timeoutCount,IFNULL(sum(case when pcl.status='1005' then 1 else 0 end),0) overTimeNum"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0'" +
                     " <if test='auditYear!=null and auditYear!=\"\"'> and pcl.audit_year like '%${auditYear}%' </if>"+
                    " group by pcl.implementing_agency_id "
                    +" <if test='pageStart!=null and pageNo!=null'> limit ${pageStart},${pageNo}</if>",
            "</script>"})
    List<Map<String, Object>> statisticPlanReport( @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo, @Param("auditYear")String auditYear);
    @Select({"<script>",
            "select IFNULL(pcl.implementing_agency_id,'${deptId}') implementingAgencyId,IFNULL(sum(CASE WHEN pcl. STATUS != '0' THEN 1 ELSE 0 END),0) projectCount,IFNULL(sum(case when pcl.status='1003' then 1 else 0 end),0) finishCount,"
                    +"IFNULL(sum(case when (pcl. STATUS != '1004' or pcl. STATUS != '1005') and pcl. STATUS != '0' then 1 else 0 end),0) noFinishCount,IFNULL(sum(case when pcl.status='1004' then 1 else 0 end),0) timeoutCount,IFNULL(sum(case when pcl.status='1005' then 1 else 0 end),0) overTimeNum"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0'" +
                     " <if test='auditYear!=null and auditYear!=\"\"'> and pcl.audit_year like '%${auditYear}%' </if>"+
                    " and pcl.implementing_agency_id = '${deptId}' "
                    +" <if test='pageStart!=null and pageNo!=null'> limit ${pageStart},${pageNo}</if>",
            "</script>"})
    List<Map<String, Object>> statisticPlanReportByDeptId( @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo, @Param("auditYear")String auditYear, @Param("deptId")int deptId);
    @Select({"<script>",
            "select count(*) count from (select pcl.implementing_agency_id id,sum(CASE WHEN pcl. STATUS != '0' THEN 1 ELSE 0 END) projectCount,sum(case when pcl.status='1003' then 1 else 0 end) finishCount,"
                    +"sum(case when pcl. STATUS != '1003' and pcl. STATUS != '0' then 1 else 0 end) noFinishCount,sum(case when pcl.status='1004' then 1 else 0 end) timeoutCount"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0' group by pcl.implementing_agency_id ) a",
            "</script>"})
    List<Map<String, Object>> countStatisticPlanReport();
    @Select({"<script>",
            "select count(*) count from (select pcl.implementing_agency_id id,sum(CASE WHEN pcl. STATUS != '0' THEN 1 ELSE 0 END) projectCount,sum(case when pcl.status='1003' then 1 else 0 end) finishCount,"
                    +"sum(case when pcl. STATUS != '1003' and pcl. STATUS != '0' then 1 else 0 end) noFinishCount,sum(case when pcl.status='1004' then 1 else 0 end) timeoutCount"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0' and pcl.implementing_agency_id = '${deptId}' ) a",
            "</script>"})
    List<Map<String, Object>> countStatisticPlanReportByDeptId(@Param("deptId")int deptId);

    /**
     * 关注列表
     * @param page
     * @param query
     * @return
     */
    Page<PlanCheckList> selectAttentionPage(@Param("page") Page<PlanCheckList> page, @Param("query") Map<String, Object> query);

    List<Map<String, Object>> getShortPlans(@Param("deptChild") List deptChild, @Param("status") String status, @Param("auditYear") String auditYear);

    List<Map<String, Object>> getShortPlansNew(@Param("implementingAgencyId") int implementingAgencyId, @Param("status") String status, @Param("auditYear") String auditYear);

    int saveReturnPK(@Param("planCheckList") PlanCheckList planCheckList);

    /**
     * 超时统计
     * @param query
     * @return
     */
    int findDeadlinePlan(Map<String, Object> query);

    /**
     * 超时提醒分页数据
     * @param query
     * @param page
     * @return
     */
    Page<PlanCheckList> getDeadlinePlanPage( @Param("page") Page<PlanCheckList> page,@Param("query") Map<String, Object> query);

    /**
     * 插入文件记录
     * @param planFile
     */
    void addFileLog(PlanFile planFile);

    /**
     * 获取文件列表
     * @param bizKey
     * @return
     */
    List<PlanFileVO> findFilesByBizKey(String bizKey);

    /**
     * 分组统计
     * @param params
     * @return
     */
    List<Map<String, Object>> groupCount(@Param("params") Map<String, Object> params);

    List<Map<String,Object>> listAllWithDays(@Param("params") Map<String, Object> params);


    List<Map<String, Object>> selectEntryByKeyAndLevel(Map<String, String> params);

    /**
     * 查询所有已过期项目
     * @return
     */
    List<PlanCheckListDTO> findDeadlinePlanList();

    /**
     * 查找提示记录
     * @param dtoObj
     * @return
     */
    PlanOverTimeTip findCheckListTip(PlanOverTimeTip dtoObj);

    /**
     * 插入提醒记录
     * @param tip
     * @return
     */
    int insertTip(PlanOverTimeTip tip);


    /**
     * 插入提醒记录
     * @param tip
     * @return
     */
    int delTip(PlanOverTimeTip tip);

    Map<String, Object> selectProNumAndOverTime(@Param("implementingAgencyId")int implementingAgencyId, @Param("auditYear")String auditYear);

    /**
     * 删除已归档的提醒
     * @return
     */
    Integer delOverTips();
}
