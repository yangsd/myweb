/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : myweb

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-01-17 21:43:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_resource
-- ----------------------------
DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `enable` bigint(1) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_resource
-- ----------------------------
INSERT INTO `auth_resource` VALUES ('1', null, '主页', 'menu', '/index', null, 'fa-home', '0', '1', '1', '2018-01-17 20:41:48', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('2', null, '系统管理', 'menu', '', null, 'fa-edit', '0', '1', '2', '2018-01-17 20:43:29', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('3', null, '用户管理', 'menu', '/user', null, null, '2', '1', '1', '2018-01-17 20:44:23', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('4', null, '角色管理', 'menu', '/role', null, null, '2', '1', '2', '2018-01-17 20:45:22', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('5', null, '资源管理', 'menu', '/resource', null, null, '2', '1', '3', '2018-01-17 20:45:49', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('6', null, '后台监控', 'menu', null, null, 'fa-desktop', '0', '1', '3', '2018-01-17 20:48:00', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('7', null, '数据库监控', 'menu', '/druid/index.html', null, null, '6', '1', '1', '2018-01-17 20:48:42', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('8', null, '登录日志', 'menu', '/loginlog', null, null, '6', '1', '2', '2018-01-17 20:49:18', null, null, null, '0');
INSERT INTO `auth_resource` VALUES ('9', null, '操作日志', 'menu', '/operatlog', null, null, '6', '1', '3', '2018-01-17 20:49:50', null, null, null, '0');

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `enable` tinyint(1) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES ('1', 'ADMIN', '管理员', null, '1', '1', '2018-01-17 20:34:53', null, null, null, '0');

-- ----------------------------
-- Table structure for auth_role_resource_rel
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_resource_rel`;
CREATE TABLE `auth_role_resource_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_role_resource_rel
-- ----------------------------
INSERT INTO `auth_role_resource_rel` VALUES ('1', '1', '1', '2018-01-17 20:35:42', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('2', '1', '2', '2018-01-17 21:18:40', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('3', '1', '3', '2018-01-17 21:18:48', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('4', '1', '4', '2018-01-17 21:18:56', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('5', '1', '5', '2018-01-17 21:19:02', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('6', '1', '6', '2018-01-17 21:19:07', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('7', '1', '7', '2018-01-17 21:19:13', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('8', '1', '8', '2018-01-17 21:19:20', null, null, null, '0');
INSERT INTO `auth_role_resource_rel` VALUES ('9', '1', '9', '2018-01-17 21:19:27', null, null, null, '0');

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `loginid` varchar(100) DEFAULT NULL COMMENT '登录帐号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `weixinid` varchar(50) DEFAULT NULL COMMENT '微信id',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `salt` varchar(50) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '状态：启用1，锁定0，停用-1',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_user
-- ----------------------------
INSERT INTO `auth_user` VALUES ('1', 'admin', '09ee1e6656470507545cb713d73617a8', '管理员', null, null, null, '04d3a2f8f32914a61b43dfaaf405606e', '1', '2018-01-16 17:47:27', null, null, null, '0');

-- ----------------------------
-- Table structure for auth_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role_rel`;
CREATE TABLE `auth_user_role_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `modifier` varchar(50) DEFAULT NULL,
  `delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth_user_role_rel
-- ----------------------------
INSERT INTO `auth_user_role_rel` VALUES ('1', '1', '1', '2018-01-17 20:35:16', null, null, null, '0');
