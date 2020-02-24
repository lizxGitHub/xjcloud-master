package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
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
    @Select({"<script>",
            "select pcl.audit_year,pcl.implementing_agency_id implementingAgencyId,pcl.audit_object_id auditObjectId,pcl.id,pcl.project_code,pcl.project_name,project_type_entry.name project_type,pcl.question_entry_id questionEntryId,pcl.problem_severity_id problemSeverityId,"
                    +"pcl.status,pcl.audit_basis auditBasis,pcl.audit_classification_id auditClassificationId,pcl.auditing_experience auditingExperience,"
                    + "audit_nature_entry.name audit_nature_name,problem_severity_entry.name problem_severity_name,"
                    + "rectify_situation_entry.name rectify_situation_name,risk_assessment_entry.name risk_assessment_name,"
                    + "pcl.problem_characterization,pcl.problem_description"
                    + " from plan_check_list pcl"
                    + " left join entry_info project_type_entry on pcl.project_type=project_type_entry.id and project_type_entry.del_flag=0 and project_type_entry.category_fk=3"
                    + " left join entry_info audit_nature_entry on pcl.audit_nature_id=audit_nature_entry.id and audit_nature_entry.del_flag=0 and audit_nature_entry.category_fk=4"
                    + " left join entry_info problem_severity_entry on pcl.problem_severity_id=problem_severity_entry.id and problem_severity_entry.del_flag=0 and problem_severity_entry.category_fk=5"
                    + " left join entry_info rectify_situation_entry on pcl.rectify_situation_id=rectify_situation_entry.id and rectify_situation_entry.del_flag=0 and rectify_situation_entry.category_fk=6"
                    + " left join entry_info risk_assessment_entry on pcl.risk_assessment_id=risk_assessment_entry.id and risk_assessment_entry.del_flag=0  and risk_assessment_entry.category_fk=9"
                    + " where pcl.del_flag=0 "
            + " <if test='query.projectName!=null and query.projectName!=\"\"'> and pcl.project_name like '%${query.projectName}%'</if>"
                    + " <if test='query.problemDescription!=null and query.problemDescription!=\"\"'> and (pcl.problem_characterization like '%${query.problemDescription}%' or pcl.problem_description like '%${query.problemDescription}%')</if>"
                    + " <if test='query.auditYear!=null and query.auditYear!=\"\"'> and (pcl.audit_year like '%${query.auditYear}%')</if>"
                    + " <if test='query.implementingAgencyId!=null and query.implementingAgencyId!=\"\"'> and (pcl.implementing_agency_id in ('${query.implementingAgencyId}'))</if>"
                    + " <if test='query.auditNatureId!=null and query.auditNatureId!=\"\"'> and (pcl.audit_nature_id = '${query.auditNatureId}')</if>"
                    + " <if test='query.auditObjectId!=null and query.auditObjectId!=\"\"'> and (pcl.audit_object_id = '${query.auditObjectId}')</if>"
                    + " <if test='query.problemSeverityId!=null and query.problemSeverityId!=\"\"'> and (pcl.problem_severity_id = '${query.problemSeverityId}')</if>"
                    + " <if test='query.rectifySituationId!=null and query.rectifySituationId!=\"\"'> and (pcl.rectify_situation_id = '${query.rectifySituationId}')</if>"
                    + " <if test='pageStart!=null and pageNo!=null'> limit ${pageStart},${pageNo}</if>"
                    + "",
    "</script>"})
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
    /**
     * 根据字段分组查询
     */
    @Select({"<script>",
            "select <if test='groupName!=null and groupName!=\"\"'>${groupName} name ,</if>pcl.${groupField} id, count(1) value"
                    + " from plan_check_list pcl"
                    + " left join entry_info project_type_entry on pcl.project_type=project_type_entry.id and project_type_entry.del_flag=0 and project_type_entry.category_fk=3"
                    + " left join entry_info audit_nature_entry on pcl.audit_nature_id=audit_nature_entry.id and audit_nature_entry.del_flag=0 and audit_nature_entry.category_fk=4"
                    + " left join entry_info problem_severity_entry on pcl.problem_severity_id=problem_severity_entry.id and problem_severity_entry.del_flag=0 and problem_severity_entry.category_fk=5"
                    + " left join entry_info rectify_situation_entry on pcl.rectify_situation_id=rectify_situation_entry.id and rectify_situation_entry.del_flag=0 and rectify_situation_entry.category_fk=6"
                    + " left join entry_info risk_assessment_entry on pcl.risk_assessment_id=risk_assessment_entry.id and risk_assessment_entry.del_flag=0  and risk_assessment_entry.category_fk=9"
                    + " where pcl.del_flag=0 "
                    + " <if test='query.projectName!=null and query.projectName!=\"\"'> and pcl.project_name like '%${query.projectName}%'</if>"
                    + " <if test='query.problemDescription!=null and query.problemDescription!=\"\"'> and (pcl.problem_characterization like '%${query.problemDescription}%' or pcl.problem_description like '%${query.problemDescription}%')</if>"
                    + " <if test='query.auditYear!=null and query.auditYear!=\"\"'> and (pcl.audit_year like '%${query.auditYear}%')</if>"
                    + " <if test='query.implementingAgencyId!=null and query.implementingAgencyId!=\"\"'> and (pcl.implementing_agency_id = '${query.implementingAgencyId}')</if>"
                    + " <if test='query.auditNatureId!=null and query.auditNatureId!=\"\"'> and (pcl.audit_nature_id = '${query.auditNatureId}')</if>"
                    + " <if test='query.auditObjectId!=null and query.auditObjectId!=\"\"'> and (pcl.audit_object_id = '${query.auditObjectId}')</if>"
                    + " <if test='query.problemSeverityId!=null and query.problemSeverityId!=\"\"'> and (pcl.problem_severity_id = '${query.problemSeverityId}')</if>"
                    + " <if test='query.rectifySituationId!=null and query.rectifySituationId!=\"\"'> and (pcl.rectify_situation_id = '${query.rectifySituationId}')</if>"
                    + " group by pcl.${groupField} order by count(1) desc",
            "</script>"})
    List<Map<String, Object>> groupCountEntryByQuery(@Param("query") PlanCheckList query, @Param("groupName")String groupName, @Param("groupField")String groupField);


    List<Map<String, Object>> countPlan(@Param("agencyId")String agencyId, @Param("auditYear")String auditYear);

    @Select({"<script>",
            "select pcl.implementing_agency_id implementingAgencyId,count(*) projectCount,sum(case when pcl.status='1003' then 1 else 0 end) finishCount,"
                    +"sum(case when pcl.status!='1003' then 1 else 0 end) noFinishCount,sum(case when pcl.delay_date is not null then 1 else 0 end) timeoutCount"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0'" +
                     " <if test='auditYear!=null and auditYear!=\"\"'> and pcl.audit_year like '%${auditYear}%' </if>"+
                    " group by pcl.implementing_agency_id "
                    +" <if test='pageStart!=null and pageNo!=null'> limit ${pageStart},${pageNo}</if>",
            "</script>"})
    List<Map<String, Object>> statisticPlanReport( @Param("pageStart") Long pageStart, @Param("pageNo") Long pageNo, @Param("auditYear")String auditYear);
    @Select({"<script>",
            "select count(*) count from (select pcl.implementing_agency_id id,count(*) projectCount,sum(case when pcl.status='1003' then 1 else 0 end) finishCount,"
                    +"sum(case when pcl.status!='1003' then 1 else 0 end) noFinishCount,sum(case when pcl.delay_date is not null then 1 else 0 end) timeoutCount"
                    +" from plan_check_list pcl "
                    +" where pcl.del_flag='0' group by pcl.implementing_agency_id ) a",
            "</script>"})
    List<Map<String, Object>> countStatisticPlanReport();

    /**
     * 关注列表
     * @param page
     * @param query
     * @return
     */
    Page<PlanCheckList> selectAttentionPage(@Param("page") Page<PlanCheckList> page, @Param("query") Map<String, Object> query);

    List<Map<String, Object>> getShortPlans(@Param("deptChild") List deptChild, @Param("status") String status, @Param("auditYear") String auditYear);

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
}
