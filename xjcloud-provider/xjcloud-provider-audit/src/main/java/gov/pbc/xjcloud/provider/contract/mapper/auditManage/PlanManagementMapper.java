package gov.pbc.xjcloud.provider.contract.mapper.auditManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gov.pbc.xjcloud.provider.contract.entity.PlanCheckList;
import gov.pbc.xjcloud.provider.contract.mapper.IBaseMapper;
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

    /**
     * 根据categoryId获取词条
     * @param categoryId
     * @return
     */
    List<Map<String, Object>> selectEntryByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 根据条件查询结果集
     * @param query
     * @return
     */
    @Select({"<script>",
            "select audit_year,agency_entry.name agency_name,audit_entry.name audit_object_name,pcl.project_name,"
                    + " audit_nature_entry.name audit_nature_name,problem_severity_entry.name problem_severity_name,"
                    + "rectify_situation_entry.name rectify_situation_name,risk_assessment_entry.name risk_assessment_name,"
                    + "pcl.problem_characterization,pcl.problem_description"
                    + " from plan_check_list pcl"
                    + " left join entry_info agency_entry on pcl.implementing_agency_id=agency_entry.id and agency_entry.del_flag=0 and agency_entry.category_fk=1"
                    + " left join entry_info audit_entry on pcl.audit_object_id=audit_entry.id and audit_entry.del_flag=0 and audit_entry.category_fk=2"
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
                    + " left join entry_info agency_entry on pcl.implementing_agency_id=agency_entry.id and agency_entry.del_flag=0 and agency_entry.category_fk=1"
                    + " left join entry_info audit_entry on pcl.audit_object_id=audit_entry.id and audit_entry.del_flag=0 and audit_entry.category_fk=2"
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
                    + "",
            "</script>"})
    List<Map<String, Object>> countEntryByQuery(@Param("query") PlanCheckList query);
}
