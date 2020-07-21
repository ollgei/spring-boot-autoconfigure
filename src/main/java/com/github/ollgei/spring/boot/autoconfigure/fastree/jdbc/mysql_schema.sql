/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost
 Source Database       : db_warehouse

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : utf-8

 Date: 07/21/2020 14:20:59 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tb_fastree`
-- ----------------------------
DROP TABLE IF EXISTS `tb_fastree`;
CREATE TABLE `tb_fastree` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `kind` varchar(16) NOT NULL COMMENT '类型',
  `name` varchar(32) NOT NULL,
  `lftno` int(11) NOT NULL,
  `rgtno` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fastree_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
