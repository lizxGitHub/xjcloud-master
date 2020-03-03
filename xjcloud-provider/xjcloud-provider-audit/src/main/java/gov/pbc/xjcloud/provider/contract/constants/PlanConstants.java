package gov.pbc.xjcloud.provider.contract.constants;

import lombok.Data;

@Data
public class PlanConstants{
		private String projectName="项目名称";
		private String problemDescription="问题清单";
		private String auditYearStart="起始年度";
		private String auditYearEnd="末尾年度";
		private String problemSeverityId="严重程度";
		private String projectType="项目类型";
		private String auditNatureId="审计性质";
		private String auditObjectId="审计对象";
		private String questionEntryId="问题词条";
		private String rectifySituationId="整改情况";
		private String costTime="整改时长";
		private String overTime="超时情况";
		private String implementingAgencyId="实施机构";
	}