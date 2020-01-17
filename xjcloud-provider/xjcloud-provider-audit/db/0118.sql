/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : xjcloud_audit

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2020-01-18 00:50:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for audit_plan_info
-- ----------------------------
DROP TABLE IF EXISTS `audit_plan_info`;
CREATE TABLE `audit_plan_info` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `plan_id` varchar(64) DEFAULT NULL COMMENT '计划id',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `opinion` varchar(255) DEFAULT NULL COMMENT '意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计计划状态流程详情';

-- ----------------------------
-- Records of audit_plan_info
-- ----------------------------
INSERT INTO `audit_plan_info` VALUES ('1', '5f85e67b27a150ef1b932c6aa228f01c', '1003', '1', 'www ');
INSERT INTO `audit_plan_info` VALUES ('2', '6ab75214fec77f64d756c8ff0624ce5c', '1003', '1', 'w');
INSERT INTO `audit_plan_info` VALUES ('63e2367b394811ea9fd060cad1bb34d4', '5f85e67b27a150ef1b932c6aa228f01c', '1006', '2', 'www ');
INSERT INTO `audit_plan_info` VALUES ('d562e2f7394811ea9fd060cad1bb34d4', '5f85e67b27a150ef1b932c6aa228f01c', '1007', '2', 'www ');

-- ----------------------------
-- Table structure for audit_project_info
-- ----------------------------
DROP TABLE IF EXISTS `audit_project_info`;
CREATE TABLE `audit_project_info` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `plan_id` varchar(64) DEFAULT NULL COMMENT '计划id',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `opinion` varchar(255) DEFAULT NULL COMMENT '意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计项目状态流程详情';

-- ----------------------------
-- Records of audit_project_info
-- ----------------------------

-- ----------------------------
-- Table structure for entry_category
-- ----------------------------
DROP TABLE IF EXISTS `entry_category`;
CREATE TABLE `entry_category` (
  `revision` int(10) DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(10) DEFAULT NULL COMMENT '上级分类',
  `level` int(10) DEFAULT NULL COMMENT '词条分级',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `def_key` varchar(32) DEFAULT NULL COMMENT '分类代码',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '逻辑删除 逻辑删除字段（1-已删除，2-未删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='词条分类  ';

-- ----------------------------
-- Records of entry_category
-- ----------------------------
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '1', '0', '1', '实施机构', 'SSJG', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '2', '0', '1', '审计对象', 'SJDX', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '3', '0', '1', '项目类型', 'XMLX', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '4', '0', '1', '审计性质', 'SJXZ', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '5', '0', '1', '问题严重程度', 'WTYZCD', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '6', '0', '1', '整改情况', 'ZGQK', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '7', '0', '1', '审计分类', 'SJFL', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-09 10:19:45', null, null, '8', '0', '4', '问题分类', 'WTFL', '0');
INSERT INTO `entry_category` VALUES ('1', 'admin', '2020-01-14 21:57:36', null, null, '9', '0', '1', '风险评估', 'FXPG', '0');

-- ----------------------------
-- Table structure for entry_flow
-- ----------------------------
DROP TABLE IF EXISTS `entry_flow`;
CREATE TABLE `entry_flow` (
  `revision` int(10) DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` varchar(64) DEFAULT NULL COMMENT '主键',
  `type_code` varchar(128) DEFAULT NULL COMMENT '类型编码',
  `entry_fk` varchar(64) DEFAULT NULL COMMENT '词条主键',
  `name` varchar(32) DEFAULT NULL COMMENT '词条名称',
  `category_fk` varchar(64) DEFAULT NULL COMMENT '词条分类',
  `remarks` varchar(128) DEFAULT NULL COMMENT '词条说明',
  `user_opt` varchar(1) DEFAULT NULL COMMENT '用户行为 0-删除，1-新增，2-修改',
  `instance_id` varchar(32) DEFAULT NULL COMMENT '流程实例',
  `apply_user` varchar(32) DEFAULT NULL COMMENT '发起人 用户名',
  `audit_user` varchar(32) DEFAULT NULL COMMENT '审核人 用户名',
  `audit_status` varchar(1) DEFAULT NULL COMMENT '审核状态 0-待审批，1-通过，2-拒绝',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '逻辑删除 0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='词条流程实例表  ';

-- ----------------------------
-- Records of entry_flow
-- ----------------------------
INSERT INTO `entry_flow` VALUES ('1', 'user01', '2020-01-13 22:51:44', null, null, 'ff2b3a584fbdd57aa368c1157ea99fce', 'SJDX1578927103643', null, '666', '2', '33', '1', '', 'user01', null, '1', '0');

-- ----------------------------
-- Table structure for entry_info
-- ----------------------------
DROP TABLE IF EXISTS `entry_info`;
CREATE TABLE `entry_info` (
  `revision` int(10) DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(128) DEFAULT NULL COMMENT '创建人 username',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间 创建时间',
  `updated_by` varchar(128) DEFAULT NULL COMMENT '更新人 username',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间 更新时间',
  `type_code` varchar(32) DEFAULT NULL COMMENT '类型编号',
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(32) DEFAULT NULL COMMENT '词条名称 词条名称',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '上级词条',
  `parenr_ids` varchar(1024) DEFAULT NULL COMMENT '词条组',
  `category_fk` int(10) DEFAULT NULL COMMENT '所属分类',
  `remarks` varchar(128) DEFAULT NULL COMMENT '词条说明 最多128个字符',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '逻辑删除 0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='词条信息表  ';

-- ----------------------------
-- Records of entry_info
-- ----------------------------
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, '13f0864036d411ea842b54ee75d33f47', '整改2', null, null, '6', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, '16296c5b36d411ea842b54ee75d33f47', '整改1', null, null, '6', null, '0');
INSERT INTO `entry_info` VALUES ('1', 'user01', '2020-01-15 19:52:10', null, null, 'SJDX1578927103643', '1e60d4b3856e7be062964dba23deb337', '666', null, null, '2', '33', '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:11', null, null, null, '37dcb0d136ca11ea842b54ee75d33f47', '机构1', null, null, '1', '', '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, '5737980036ca11ea842b54ee75d33f47', '机构2', null, null, '1', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, '5ed9ff5636d311ea842b54ee75d33f47', '严重程度1', null, null, '5', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, '61c1a00c36d311ea842b54ee75d33f47', '严重程度2', null, null, '5', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'b55ae35436d211ea842b54ee75d33f47', '审计性质2', null, null, '4', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'b7ad469636d211ea842b54ee75d33f47', '审计性质1', null, null, '4', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'c77b001e36d511ea842b54ee75d33f47', '审计分类1', null, null, '7', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'c982b2ac36d511ea842b54ee75d33f47', '审计分类2', null, null, '7', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'f247d3f236ca11ea842b54ee75d33f47', '审计对象1', null, null, '2', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'f58d073d36d511ea842b54ee75d33f47', '风险评估2', null, null, '9', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'f670709d36ca11ea842b54ee75d33f47', '审计对象2', null, null, '2', null, '0');
INSERT INTO `entry_info` VALUES (null, null, '2020-01-14 20:35:28', null, null, null, 'f71ebf4f36d511ea842b54ee75d33f47', '风险评估1', null, null, '9', null, '0');

-- ----------------------------
-- Table structure for pdman_db_version
-- ----------------------------
DROP TABLE IF EXISTS `pdman_db_version`;
CREATE TABLE `pdman_db_version` (
  `DB_VERSION` varchar(256) DEFAULT NULL,
  `VERSION_DESC` varchar(1024) DEFAULT NULL,
  `CREATED_TIME` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of pdman_db_version
-- ----------------------------
INSERT INTO `pdman_db_version` VALUES ('v0.0.0', '默认版本，新增的版本不能低于此版本', '2020-01-13 22:48:34');
INSERT INTO `pdman_db_version` VALUES ('v1.0.0', '初始', '2020-01-13 22:48:43');

-- ----------------------------
-- Table structure for plan_check_list
-- ----------------------------
DROP TABLE IF EXISTS `plan_check_list`;
CREATE TABLE `plan_check_list` (
  `revision` int(10) DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` varchar(64) NOT NULL COMMENT '主键',
  `project_code` varchar(128) DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(128) DEFAULT NULL COMMENT '项目名称',
  `implementing_agency_id` varchar(128) DEFAULT NULL COMMENT '实施机构',
  `audit_object_id` varchar(128) DEFAULT NULL COMMENT '审计对象',
  `audit_nature_id` varchar(128) DEFAULT NULL COMMENT '审计性质',
  `audit_year` date DEFAULT NULL COMMENT '审计年度',
  `leader_status` varchar(32) DEFAULT NULL COMMENT '领导',
  `normal_status` varchar(32) DEFAULT NULL COMMENT '员工状态',
  `question_entry_id` varchar(128) DEFAULT NULL COMMENT '问题词条',
  `problem_severity_id` varchar(128) DEFAULT NULL COMMENT '问题严重程度',
  `rectify_situation_id` varchar(128) DEFAULT NULL COMMENT '整改情况',
  `problem_characterization` varchar(128) DEFAULT NULL COMMENT '问题定性',
  `problem_description` varchar(128) DEFAULT NULL COMMENT '问题描述',
  `may_affect` varchar(128) DEFAULT NULL COMMENT '可能影响',
  `rectification_suggestions` varchar(128) DEFAULT NULL COMMENT '整改建议',
  `audit_basis` varchar(128) DEFAULT NULL COMMENT '审计依据',
  `audit_classification_id` varchar(128) DEFAULT NULL COMMENT '审计分类',
  `auditing_experience` varchar(128) DEFAULT NULL COMMENT '审计经验',
  `risk_assessment_id` varchar(128) DEFAULT NULL COMMENT '风险评估',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '逻辑删除 0-未删除，1-已删除',
  `instance_id` varchar(32) DEFAULT NULL COMMENT '流程实例',
  `frequency` varchar(64) DEFAULT NULL COMMENT '出现频次',
  `rectify_man` varchar(128) DEFAULT NULL COMMENT '整改负责人',
  `rectify_way` varchar(255) DEFAULT NULL,
  `plan_time` datetime DEFAULT NULL COMMENT '计划整改时长',
  `rectify_result` varchar(255) DEFAULT NULL COMMENT '整改结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划清单列表  ';

-- ----------------------------
-- Records of plan_check_list
-- ----------------------------
INSERT INTO `plan_check_list` VALUES (null, null, null, null, null, '5f85e67b27a150ef1b932c6aa228f01c', 'PROJECT6856', '项目贯彻新增', '37dcb0d136ca11ea842b54ee75d33f47', 'f670709d36ca11ea842b54ee75d33f47', 'b55ae35436d211ea842b54ee75d33f47', '2020-01-15', null, '1001', '', '61c1a00c36d311ea842b54ee75d33f47', '13f0864036d411ea842b54ee75d33f47', '问题定性严重', '描述是很难描述', '影响有必要', '建议不太知道', '依据金钱', 'c77b001e36d511ea842b54ee75d33f47', '经验很重要', 'f58d073d36d511ea842b54ee75d33f47', '0', null, null, null, null, null, null);
INSERT INTO `plan_check_list` VALUES (null, null, null, null, null, '5fdf33d4f7d7fd62488fc61cc55f5222', 'PROJECT', '项目3', '5737980036ca11ea842b54ee75d33f47', 'f247d3f236ca11ea842b54ee75d33f47', 'b55ae35436d211ea842b54ee75d33f47', '2020-01-14', null, '1002', '', '5ed9ff5636d311ea842b54ee75d33f47', '13f0864036d411ea842b54ee75d33f47', '问题定性', '问题描述', '可能影响', '整改建议', '审计依据', 'c77b001e36d511ea842b54ee75d33f47', '审计经验', 'f58d073d36d511ea842b54ee75d33f47', '0', null, null, null, null, null, null);
INSERT INTO `plan_check_list` VALUES (null, null, null, null, null, '6ab75214fec77f64d756c8ff0624ce5c', 'PROJECT', '项目1', '5737980036ca11ea842b54ee75d33f47', 'f247d3f236ca11ea842b54ee75d33f47', 'b55ae35436d211ea842b54ee75d33f47', '2020-01-14', null, '1001', '', '5ed9ff5636d311ea842b54ee75d33f47', '', '', '', '', '', '', '', '', '', '0', null, null, null, null, null, null);
INSERT INTO `plan_check_list` VALUES (null, null, null, null, null, 'f7d91cc02888e7db536aa91dec1da87f', null, '项目3', '37dcb0d136ca11ea842b54ee75d33f47', 'f670709d36ca11ea842b54ee75d33f47', 'b55ae35436d211ea842b54ee75d33f47', '2020-01-14', null, '1003', '', '5ed9ff5636d311ea842b54ee75d33f47', null, null, null, null, null, null, null, null, null, '1', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for plan_info
-- ----------------------------
DROP TABLE IF EXISTS `plan_info`;
CREATE TABLE `plan_info` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `plan_id` varchar(64) DEFAULT NULL COMMENT '计划id',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `opinion` varchar(255) DEFAULT NULL COMMENT '意见',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目状态';

-- ----------------------------
-- Records of plan_info
-- ----------------------------

-- ----------------------------
-- Table structure for question_info
-- ----------------------------
DROP TABLE IF EXISTS `question_info`;
CREATE TABLE `question_info` (
  `revision` int(10) DEFAULT NULL COMMENT '乐观锁',
  `created_by` varchar(128) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` varchar(128) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) DEFAULT NULL COMMENT '项目编号',
  `audit_type` varchar(32) DEFAULT NULL COMMENT '审计类型',
  `audit_depart` varchar(32) DEFAULT NULL COMMENT '审计部门',
  `start_date_time` datetime DEFAULT NULL COMMENT '开始时间',
  `predict_time` int(10) DEFAULT NULL COMMENT '预计时长',
  `cost_time` int(10) DEFAULT NULL COMMENT '花费时长',
  `audit_status` varchar(1) DEFAULT NULL COMMENT '审核状态 0-待提审，1-审核通过，2-待审核，3-不通过，4-延期处理',
  `instance_id` varchar(128) DEFAULT NULL COMMENT '流程实例',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '逻辑删除字段 0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题基本配置表    ';

-- ----------------------------
-- Records of question_info
-- ----------------------------
