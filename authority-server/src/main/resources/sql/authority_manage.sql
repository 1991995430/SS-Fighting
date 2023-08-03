/*
 Navicat Premium Data Transfer

 Source Server         : 兴化现网
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 140.246.171.29:3306
 Source Schema         : authority_manage

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 29/06/2021 18:14:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role_menu_white_list
-- ----------------------------
DROP TABLE IF EXISTS `role_menu_white_list`;
CREATE TABLE `role_menu_white_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `menu` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu_white_list
-- ----------------------------
INSERT INTO `role_menu_white_list` VALUES (1, 2, 1, 'abcdef');
INSERT INTO `role_menu_white_list` VALUES (2, 1, 1, '{\"id\":1,\"meta\":{\"title\":\"根菜单\"},\"name\":\"root\",\"path\":\"root\",\"children\":[{\"id\":3,\"path\":\"test2\",\"name\":\"test2\",\"children\":[],\"meta\":{\"title\":\"子菜单2\"}}]}');
INSERT INTO `role_menu_white_list` VALUES (3, 976, 654, '[{\"id\":1,\"meta\":{\"title\":\"根菜单\"},\"name\":\"maintain\",\"path\":\"/insideRoadOrderQuery\",\"children\":[{\"id\":4,\"path\":\"projectManage\",\"name\":\"projectManage\",\"children\":[{\"id\":5,\"path\":\"projectInfoManage\",\"name\":\"projectInfoManage\",\"children\":[],\"meta\":{\"title\":\"项目管理\"}},{\"id\":22,\"path\":\"projectInfoDetail\",\"name\":\"projectInfoDetail\",\"children\":[],\"meta\":{\"title\":\"项目详情\"}}],\"meta\":{\"title\":\"项目管理\"},\"redirect\":\"/projectManage/projectInfoManage\"},{\"id\":6,\"path\":\"enterpriseManage\",\"name\":\"enterpriseManage\",\"children\":[{\"id\":7,\"path\":\"enterpriseInfoManage\",\"name\":\"enterpriseInfoManage\",\"children\":[],\"meta\":{\"title\":\"企业信息管理\"}},{\"id\":8,\"path\":\"infoReportManage\",\"name\":\"infoReportManage\",\"children\":[],\"meta\":{\"title\":\"企业动态信息上报\"}}],\"meta\":{\"title\":\"企业管理\"},\"redirect\":\"/enterpriseManage/enterpriseInfoManage\"},{\"id\":9,\"path\":\"sysManage\",\"name\":\"sysManage\",\"children\":[{\"id\":11,\"path\":\"industryManage\",\"name\":\"industryManage\",\"children\":[],\"meta\":{\"title\":\"所属行业管理\"}},{\"id\":13,\"path\":\"projectStageManage\",\"name\":\"projectStageManage\",\"children\":[],\"meta\":{\"title\":\"项目阶段管理\"}}],\"meta\":{\"title\":\"系统管理\"},\"redirect\":\"/sysManage/industryManage\"},{\"id\":18,\"path\":\"stepManage\",\"name\":\"stepManage\",\"children\":[{\"id\":19,\"path\":\"stepCheck\",\"name\":\"stepCheck\",\"children\":[],\"meta\":{\"title\":\"步骤管理\"}}],\"meta\":{\"title\":\"步骤管理\"},\"redirect\":\"/stepManage/stepCheck\"},{\"id\":20,\"path\":\"factoryManage\",\"name\":\"factoryManage\",\"children\":[{\"id\":21,\"path\":\"factoryInfoManage\",\"name\":\"factoryInfoManage\",\"children\":[],\"meta\":{\"title\":\"厂房管理\"}}],\"meta\":{\"title\":\"厂房管理\"},\"redirect\":\"/factoryManage/factoryInfoManage\"}]}]');
INSERT INTO `role_menu_white_list` VALUES (4, 977, 654, '[{\"id\":1,\"meta\":{\"title\":\"根菜单\"},\"name\":\"maintain\",\"path\":\"/insideRoadOrderQuery\",\"children\":[{\"id\":4,\"path\":\"projectManage\",\"name\":\"projectManage\",\"children\":[{\"id\":5,\"path\":\"projectInfoManage\",\"name\":\"projectInfoManage\",\"children\":[],\"meta\":{\"title\":\"项目管理\"}}],\"meta\":{\"title\":\"项目管理\"},\"redirect\":\"/projectManage/projectInfoManage\"}]}]');

-- ----------------------------
-- Table structure for role_url_white_list
-- ----------------------------
DROP TABLE IF EXISTS `role_url_white_list`;
CREATE TABLE `role_url_white_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表id',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `service_id` int(11) NULL DEFAULT NULL COMMENT '服务id',
  `url` varchar(511) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'url地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_url_white_list
-- ----------------------------
INSERT INTO `role_url_white_list` VALUES (2, 1, 1, '/authority-server/*');
INSERT INTO `role_url_white_list` VALUES (21, 976, 654, '/authority-server/*');
INSERT INTO `role_url_white_list` VALUES (22, 976, 654, '/project-manage/*');
INSERT INTO `role_url_white_list` VALUES (24, 977, 654, '/project-manage/*');
INSERT INTO `role_url_white_list` VALUES (25, 977, 654, '/authority-server/*');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) NOT NULL,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `chinese_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_item` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `add_user_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `add_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  `status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (39, '[{\"id\":4,\"path\":\"projectManage\",\"name\":\"projectManage\",\"children\":[{\"id\":5,\"path\":\"projectInfoManage\",\"name\":\"projectInfoManage\",\"children\":[],\"meta\":{\"title\":\"项目管理\"}},{\"id\":22,\"path\":\"projectInfoDetail\",\"name\":\"projectInfoDetail\",\"children\":[],\"meta\":{\"title\":\"项目详情\"}}],\"meta\":{\"title\":\"项目管理\"}},{\"id\":6,\"path\":\"enterpriseManage\",\"name\":\"enterpriseManage\",\"children\":[{\"id\":7,\"path\":\"enterpriseInfoManage\",\"name\":\"enterpriseInfoManage\",\"children\":[],\"meta\":{\"title\":\"企业信息管理\"}},{\"id\":8,\"path\":\"infoReportManage\",\"name\":\"infoReportManage\",\"children\":[],\"meta\":{\"title\":\"企业动态信息上报\"}}],\"meta\":{\"title\":\"企业管理\"}},{\"id\":9,\"path\":\"sysManage\",\"name\":\"sysManage\",\"children\":[{\"id\":11,\"path\":\"industryManage\",\"name\":\"industryManage\",\"children\":[],\"meta\":{\"title\":\"所属行业管理\"}},{\"id\":13,\"path\":\"projectStageManage\",\"name\":\"projectStageManage\",\"children\":[],\"meta\":{\"title\":\"项目阶段管理\"}}],\"meta\":{\"title\":\"系统管理\"}},{\"id\":18,\"path\":\"stepManage\",\"name\":\"stepManage\",\"children\":[{\"id\":19,\"path\":\"stepCheck\",\"name\":\"stepCheck\",\"children\":[],\"meta\":{\"title\":\"步骤管理\"}}],\"meta\":{\"title\":\"步骤管理\"}},{\"id\":20,\"path\":\"factoryManage\",\"name\":\"factoryManage\",\"children\":[{\"id\":21,\"path\":\"factoryInfoManage\",\"name\":\"factoryInfoManage\",\"children\":[],\"meta\":{\"title\":\"厂房管理\"}}],\"meta\":{\"title\":\"厂房管理\"}}]', 893, 654, '2021-06-15 14:17:42', '2021-06-24 11:28:00', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id,主键,自动增长',
  `service_id` int(11) NOT NULL COMMENT '业务id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `admin_flag` int(255) NOT NULL COMMENT '是否超级管理员（1：是，0：不是）',
  `add_user_id` int(11) NOT NULL COMMENT '创建者id',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NOT NULL COMMENT '状态（1生效，0失效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 978 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 1, '超级管理员', 1, 0, '2020-12-02 17:47:14', '2020-12-02 17:47:17', '所有用户的根用户', 1);
INSERT INTO `sys_role` VALUES (976, 654, '超级管理员', 1, 1, '2021-06-09 15:55:42', '2021-06-29 14:16:14', '兴化食谷A区超级管理员', 1);
INSERT INTO `sys_role` VALUES (977, 654, '普通员工', 0, 893, '2021-06-09 15:57:44', '2021-06-29 14:16:28', '兴化食谷A区普通用户', 1);

-- ----------------------------
-- Table structure for sys_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_service`;
CREATE TABLE `sys_service`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '业务id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务名称',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `add_user_id` int(11) NOT NULL COMMENT '添加用户id',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `status` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 655 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_service
-- ----------------------------
INSERT INTO `sys_service` VALUES (1, '超级服务', '所有服务的根服务X', 0, '2020-12-02 17:46:34', '2021-06-05 14:13:27', 1);
INSERT INTO `sys_service` VALUES (654, '兴化园区', '兴化园区智慧展厅', 1, '2021-06-09 15:53:13', '2021-06-09 15:53:13', 1);

-- ----------------------------
-- Table structure for sys_url
-- ----------------------------
DROP TABLE IF EXISTS `sys_url`;
CREATE TABLE `sys_url`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_item` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `add_user_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `add_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL,
  `status` int(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_url
-- ----------------------------
INSERT INTO `sys_url` VALUES (17, '/authority-server/*', 1, 654, '2021-06-09 15:54:38', '2021-06-09 15:54:38', 1);
INSERT INTO `sys_url` VALUES (18, '/project-manage/*', 1, 654, '2021-06-09 15:54:50', '2021-06-09 15:54:50', 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `true_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实姓名',
  `service_id` int(11) NOT NULL COMMENT '业务id',
  `role_id` int(11) NOT NULL COMMENT '用户角色id',
  `sex` tinyint(4) NOT NULL COMMENT '性别：1男,0女',
  `mobile_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机号码',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电子邮箱',
  `add_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `status` tinyint(4) NOT NULL COMMENT '用户状态（0：已锁定  1：启用中）',
  `add_user` int(11) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `findUser`(`name`, `service_id`) USING BTREE COMMENT '通过用户名+业务ID快速查找'
) ENGINE = InnoDB AUTO_INCREMENT = 895 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'sa', '3be487b8f275d89966c3cb63326e66cd', 'super-admin', 1, 1, 1, '18252078928', '12344321@qq.com', '2020-12-07 15:02:51', '2021-06-29 14:40:49', 1, 0);
INSERT INTO `sys_user` VALUES (893, 'admin', 'f7c4aa3a5cd7bbe35d1b5637691e0bae', 'admin', 654, 976, 1, '15066239450', '1@c.n', '2021-06-09 15:57:03', '2021-06-29 17:59:37', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
