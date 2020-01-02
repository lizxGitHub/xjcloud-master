package gov.pbc.xjcloud.common.data.enums;

import lombok.Getter;

@Getter
public enum DataScopeTypeEnum {
	/**
	 * 查询全部数据
	 */
	ALL(0, "全部"),

	/**
	 * 本单位及下级单位
	 */
	SELF_AND_LOWER_LEVEL_INSTITUTION(1, "本单位及下级单位"),

	/**
	 * 本单位
	 */
	SELF_INSTITUTION(2, "本单位"),

	/**
	 * 本级
	 */
	SELF_DEPARTMENT(3, "本部门");

	/**
	 * 类型
	 */
	private final int type;
	/**
	 * 描述
	 */
	private final String description;
	
	private DataScopeTypeEnum(int type, String desc) {
		this.type = type;
		this.description = desc;
	}
	
	public static DataScopeTypeEnum parse(int type) {
		switch (type) {
			case 0:
				return ALL;
			case 1:
				return SELF_AND_LOWER_LEVEL_INSTITUTION;
			case 2:
				return SELF_INSTITUTION;
			case 3:
				return SELF_DEPARTMENT;
			default:
				return null;
		}
	}
}
