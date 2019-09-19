/*
 Navicat Premium Data Transfer

 Source Server         : wesley
 Source Server Type    : MySQL
 Source Server Version : 50614
 Source Host           : 114.212.190.189:3306
 Source Schema         : dataset_search_2018nov

 Target Server Type    : MySQL
 Target Server Version : 50614
 File Encoding         : 65001

 Date: 19/09/2019 12:42:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for evaluation_result
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_result`;
CREATE TABLE `evaluation_result`  (
  `id` int(11) NULL DEFAULT NULL,
  `k1` double NULL DEFAULT NULL,
  `c1` double NULL DEFAULT NULL,
  `s1` double NULL DEFAULT NULL,
  `d1` double NULL DEFAULT NULL,
  `k2` double NULL DEFAULT NULL,
  `c2` double NULL DEFAULT NULL,
  `s2` double NULL DEFAULT NULL,
  `d2` double NULL DEFAULT NULL,
  `k3` double NULL DEFAULT NULL,
  `c3` double NULL DEFAULT NULL,
  `s3` double NULL DEFAULT NULL,
  `d3` double NULL DEFAULT NULL,
  `k4` double NULL DEFAULT NULL,
  `c4` double NULL DEFAULT NULL,
  `s4` double NULL DEFAULT NULL,
  `d4` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
