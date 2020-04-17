/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 14/04/2020 17:25:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_behavior_log
-- ----------------------------
DROP TABLE IF EXISTS `user_behavior_log`;
CREATE TABLE `user_behavior_log`  (
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `cat_id` int(11) NULL DEFAULT NULL COMMENT '商品品类id',
  `merchant_id` int(11) NULL DEFAULT NULL COMMENT '卖家id',
  `brand_id` int(11) NULL DEFAULT NULL COMMENT '品牌id',
  `action` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户行为, 包括(\"pv\", \"buy\", \"cart\", \"fav\")',
  `gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `timestamp` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间戳'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_behavior_log
-- ----------------------------
INSERT INTO `user_behavior_log` VALUES (1, 227493, 1616, 938, 7617, 'pv', '男', '1573449214');
INSERT INTO `user_behavior_log` VALUES (1, 227493, 1616, 938, 7617, 'buy', '男', '1573439243');
INSERT INTO `user_behavior_log` VALUES (1, 414508, 1467, 3518, 4805, 'pv', '男', '1573412303');
INSERT INTO `user_behavior_log` VALUES (2, 616522, 1441, 395, 6342, 'pv', '女', '1573441798');
INSERT INTO `user_behavior_log` VALUES (2, 592775, 283, 938, 7617, 'buy', '女', '1573472747');
INSERT INTO `user_behavior_log` VALUES (2, 158505, 1467, 3518, 4805, 'pv', '女', '1573407729');
INSERT INTO `user_behavior_log` VALUES (3, 592775, 283, 938, 7617, 'cart', '女', '1573475705');
INSERT INTO `user_behavior_log` VALUES (3, 592775, 283, 938, 7617, 'pv', '女', '1573420098');
INSERT INTO `user_behavior_log` VALUES (3, 592775, 283, 938, 7617, 'cart', '女', '1573450287');
INSERT INTO `user_behavior_log` VALUES (3, 2983, 898, 3716, 5508, 'fav', '女', '1573464705');
INSERT INTO `user_behavior_log` VALUES (3, 592775, 283, 938, 7617, 'pv', '女', '1573430749');
INSERT INTO `user_behavior_log` VALUES (4, 249799, 602, 2823, 1128, 'pv', '男', '1573455450');
INSERT INTO `user_behavior_log` VALUES (4, 883556, 1157, 1485, 7452, 'pv', '男', '1573402326');
INSERT INTO `user_behavior_log` VALUES (4, 305550, 602, 1485, 7452, 'pv', '男', '1573449552');
INSERT INTO `user_behavior_log` VALUES (4, 305550, 602, 1485, 7452, 'pv', '男', '1573452126');
INSERT INTO `user_behavior_log` VALUES (5, 11784, 1577, 1769, 2132, 'buy', '女', '1573424166');
INSERT INTO `user_behavior_log` VALUES (5, 977465, 387, 3213, 2651, 'buy', '女', '1573445981');
INSERT INTO `user_behavior_log` VALUES (5, 1022751, 389, 3213, 2651, 'pv', '女', '1573424323');
INSERT INTO `user_behavior_log` VALUES (5, 889848, 389, 3213, 2651, 'pv', '女', '1573442888');
INSERT INTO `user_behavior_log` VALUES (5, 762039, 1397, 3213, 2651, 'fav', '女', '1573408230');

SET FOREIGN_KEY_CHECKS = 1;
