/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : db_code

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2019-10-30 00:53:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_box
-- ----------------------------
DROP TABLE IF EXISTS `tb_box`;
CREATE TABLE `tb_box` (
  `code` varchar(20) NOT NULL,
  `box_num` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `weight` varchar(255) DEFAULT NULL,
  `size` varchar(20) DEFAULT NULL,
  `worksheet_no` varchar(20) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_customer
-- ----------------------------
DROP TABLE IF EXISTS `tb_customer`;
CREATE TABLE `tb_customer` (
  `customer_no` varchar(20) NOT NULL COMMENT '主键',
  `company` varchar(255) NOT NULL COMMENT '公司名称',
  `name` varchar(50) NOT NULL COMMENT '客户名称',
  `phone` varchar(11) NOT NULL COMMENT '客户联系方式',
  `address` varchar(255) NOT NULL COMMENT '客户地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '客户icon',
  PRIMARY KEY (`customer_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_device_individual
-- ----------------------------
DROP TABLE IF EXISTS `tb_device_individual`;
CREATE TABLE `tb_device_individual` (
  `SN1` varchar(255) DEFAULT NULL,
  `SN2` varchar(255) DEFAULT NULL,
  `IMEI1` varchar(255) DEFAULT NULL,
  `IMEI2` varchar(255) DEFAULT NULL,
  `IMEI3` varchar(255) DEFAULT NULL,
  `IMEI4` varchar(255) DEFAULT NULL,
  `BTADDRESS` varchar(255) DEFAULT NULL,
  `WIFIADDRESS` varchar(255) DEFAULT NULL,
  `ETHERNNETMACADDRESS` varchar(255) DEFAULT NULL,
  `MEID` varchar(255) DEFAULT NULL,
  `ESN` varchar(255) DEFAULT NULL,
  `EXTRA1` varchar(255) DEFAULT NULL,
  `EXTRA2` varchar(255) DEFAULT NULL,
  `EXTRA3` varchar(255) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `worksheet_code` varchar(20) NOT NULL,
  `box_code` varchar(20) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '设备状态，0:未查询过，1:已查询'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_employee
-- ----------------------------
DROP TABLE IF EXISTS `tb_employee`;
CREATE TABLE `tb_employee` (
  `employee_no` varchar(10) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`employee_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_individual_flow
-- ----------------------------
DROP TABLE IF EXISTS `tb_individual_flow`;
CREATE TABLE `tb_individual_flow` (
  `worksheet_code` varchar(20) NOT NULL,
  `individual_sn` varchar(30) NOT NULL COMMENT '设备编号',
  `employee_no` varchar(30) NOT NULL COMMENT '员工编号',
  `oper` varchar(30) NOT NULL COMMENT '操作',
  `status` int(4) NOT NULL COMMENT '状态',
  `reset_times` int(4) NOT NULL DEFAULT '0',
  `oper_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `box_code` varchar(255) DEFAULT NULL COMMENT '所属箱子'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_worksheet
-- ----------------------------
DROP TABLE IF EXISTS `tb_worksheet`;
CREATE TABLE `tb_worksheet` (
  `code` varchar(20) NOT NULL COMMENT '工单号',
  `name` varchar(255) NOT NULL COMMENT '工单名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '工单创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '工单开始时间',
  `deadline` datetime NOT NULL COMMENT '工单截止时间',
  `status` int(4) DEFAULT NULL COMMENT '工单状态,0：未开始,1：运行,2：暂停,3：完成,4：删除',
  `customer_no` varchar(20) NOT NULL COMMENT '工单所属客户',
  `platform` varchar(255) NOT NULL COMMENT '平台',
  `device_type` varchar(255) NOT NULL COMMENT '设备类型',
  `type_code` varchar(255) DEFAULT NULL COMMENT '设备型号',
  `brand_name` varchar(255) DEFAULT NULL COMMENT '品牌名称',
  `color` varchar(255) DEFAULT NULL COMMENT '颜色',
  `full_weight` varchar(10) DEFAULT NULL COMMENT '全部重量',
  `true_weight` varchar(10) DEFAULT NULL COMMENT '净重',
  `device_size` varchar(10) DEFAULT NULL COMMENT '尺寸',
  `thickness` varchar(10) DEFAULT NULL COMMENT '厚度',
  `extend_info` varchar(255) DEFAULT NULL COMMENT '扩展信息，存成json格式的字符串',
  `file_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
