CREATE DATABASE IF NOT EXISTS `common-admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;;

USE `common-admin`;

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `key` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置键',
  `value` VARCHAR(2048) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置值',
  `remark` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '配置备注',
  `order_num` INT(11) DEFAULT NULL COMMENT '显示顺序',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`key`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统配置表';
INSERT  INTO `sys_config`(`id`,`key`,`value`,`remark`,`order_num`,`create_time`) VALUES 
(1,'CLOUD_STORAGE_CONFIG','{\"type\":1,\"bucket\":\"common-admin-1256496288\",\"region\":\"oss-cn-beijing.aliyuncs.com\",\"domain\":\"http://common-admin-1256496288.oss-cn-beijing.aliyuncs.com\",\"prefix\":\"aaa/bbb\",\"accessKey\":\"XXXX\",\"secretKey\":\"XXXX\"}','云存储配置',0,'2021-08-01 18:27:37'),
(2,'WEBSITE_NAME','common-admin','网站名称',1,'2021-08-04 10:29:52'),
(3,'WEBSITE_AUTHOR','caochenlei','网站作者',2,'2021-08-04 10:31:07'),
(4,'WEBSITE_DESC','A Common Admin!','网站描述',3,'2021-08-04 10:32:26');

DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门名称',
  `manager` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门经理',
  `phone` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门电话',
  `email` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门邮箱',
  `parent_id` BIGINT(20) DEFAULT NULL COMMENT '上级部门',
  `order_num` INT(11) DEFAULT NULL COMMENT '显示顺序',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统部门表';
INSERT  INTO `sys_department`(`id`,`name`,`manager`,`phone`,`email`,`parent_id`,`order_num`,`create_time`) VALUES 
(1,'XX集团北京公司','曹晨磊','15633029014','774908833@qq.com',0,1,'2021-07-26 13:36:13'),
(2,'开发部','曹晨磊','15633029014','774908833@qq.com',1,1,'2021-07-26 15:11:45'),
(3,'财务部','曹晨磊','15633029014','774908833@qq.com',1,2,'2021-07-21 15:27:36'),
(4,'运营部','曹晨磊','15633029014','774908833@qq.com',1,3,'2021-07-21 17:48:16'),
(5,'市场部','曹晨磊','15633029014','774908833@qq.com',1,4,'2021-07-21 17:56:04'),
(6,'XX集团南京公司','曹晨磊','15633029014','774908833@qq.com',0,2,'2021-07-25 13:21:09'),
(7,'开发部','曹晨磊','15633029014','774908833@qq.com',6,1,'2021-07-26 17:37:11'),
(8,'财务部','曹晨磊','15633029014','774908833@qq.com',6,2,'2021-07-25 13:22:09'),
(9,'运营部','曹晨磊','15633029014','774908833@qq.com',6,3,'2021-07-25 13:22:10'),
(10,'市场部','曹晨磊','15633029014','774908833@qq.com',6,4,'2021-07-25 13:22:12');

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `type` INT(11) DEFAULT NULL COMMENT '日志类型',
  `title` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '日志标题',
  `mapping` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求映射',
  `mode` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求方式',
  `method` VARCHAR(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求方法',
  `params` TEXT CHARACTER SET utf8 COMMENT '请求参数',
  `result` TEXT CHARACTER SET utf8 COMMENT '请求结果',
  `exception` TEXT CHARACTER SET utf8 COMMENT '异常信息',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `remote_ip` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求地址',
  `remote_region` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '请求区域',
  `create_by` BIGINT(20) DEFAULT NULL COMMENT '操作用户',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统日志表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单名称',
  `code` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单标识',
  `type` INT(11) DEFAULT '0' COMMENT '菜单类型（0：目录、1：菜单、2：按钮）',
  `icon` VARCHAR(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单图标',
  `path` VARCHAR(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '路由地址',
  `component` VARCHAR(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '路由组件',
  `hidden` INT(1) DEFAULT '0' COMMENT '侧边栏显示（0：显示、1：隐藏）',
  `parent_id` BIGINT(20) DEFAULT NULL COMMENT '上级菜单',
  `order_num` INT(11) DEFAULT NULL COMMENT '显示顺序',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统菜单表';
INSERT  INTO `sys_menu`(`id`,`name`,`code`,`type`,`icon`,`path`,`component`,`hidden`,`parent_id`,`order_num`,`create_time`) VALUES 
(1,'系统管理','sys:manage',0,'el-icon-s-tools','/system/manage',NULL,0,0,1,'2021-07-21 19:33:02'),
(2,'部门管理','sys:department',1,'el-icon-menu','/system/department','/system/department',0,1,1,'2021-07-21 19:41:50'),
(3,'部门新增','sys:department:add',2,NULL,NULL,NULL,0,2,1,'2021-07-21 19:44:38'),
(4,'部门删除','sys:department:delete',2,NULL,NULL,NULL,0,2,2,'2021-07-21 19:49:27'),
(5,'部门更新','sys:department:update',2,NULL,NULL,NULL,0,2,3,'2021-07-21 19:50:17'),
(6,'部门查询','sys:department:search',2,NULL,NULL,NULL,0,2,4,'2021-07-21 19:56:06'),
(7,'用户管理','sys:user',1,'el-icon-menu','/system/user','/system/user',0,1,2,'2021-07-21 20:00:54'),
(8,'用户新增','sys:user:add',2,NULL,NULL,NULL,0,7,1,'2021-07-21 20:02:27'),
(9,'用户删除','sys:user:delete',2,NULL,NULL,NULL,0,7,2,'2021-07-21 20:02:28'),
(10,'用户更新','sys:user:update',2,NULL,NULL,NULL,0,7,3,'2021-07-21 20:02:30'),
(11,'用户查询','sys:user:search',2,NULL,NULL,NULL,0,7,4,'2021-07-21 20:02:33'),
(12,'分配角色','sys:user:assign',2,NULL,NULL,NULL,0,7,5,'2021-07-21 20:08:27'),
(13,'角色管理','sys:role',1,'el-icon-menu','/system/role','/system/role',0,1,3,'2021-07-21 20:10:24'),
(14,'角色新增','sys:role:add',2,NULL,NULL,NULL,0,13,1,'2021-07-21 20:10:28'),
(15,'角色删除','sys:role:delete',2,NULL,NULL,NULL,0,13,2,'2021-07-21 20:10:30'),
(16,'角色更新','sys:role:update',2,NULL,NULL,NULL,0,13,3,'2021-07-21 20:10:32'),
(17,'角色查询','sys:role:search',2,NULL,NULL,NULL,0,13,4,'2021-07-21 20:10:36'),
(18,'分配菜单','sys:role:assign',2,NULL,NULL,NULL,0,13,5,'2021-07-21 20:10:38'),
(19,'菜单管理','sys:menu',1,'el-icon-menu','/system/menu','/system/menu',0,1,4,'2021-07-21 20:19:57'),
(20,'菜单新增','sys:menu:add',2,NULL,NULL,NULL,0,19,1,'2021-07-21 20:19:59'),
(21,'菜单删除','sys:menu:delete',2,NULL,NULL,NULL,0,19,2,'2021-07-21 20:20:01'),
(22,'菜单更新','sys:menu:update',2,NULL,NULL,NULL,0,19,3,'2021-07-21 20:20:03'),
(23,'菜单查询','sys:menu:search',2,NULL,NULL,NULL,0,19,4,'2021-07-21 20:20:12'),
(24,'日志管理','sys:log',1,'el-icon-menu','/system/log','/system/log',0,1,5,'2021-07-25 09:37:59'),
(25,'日志删除','sys:log:delete',2,NULL,NULL,NULL,0,24,1,'2021-07-25 09:38:01'),
(26,'日志查看','sys:log:view',2,NULL,NULL,NULL,0,24,2,'2021-07-25 09:38:03'),
(27,'日志查询','sys:log:search',2,NULL,NULL,NULL,0,24,3,'2021-07-25 09:38:06'),
(28,'日志清空','sys:log:clear',2,NULL,NULL,NULL,0,24,4,'2021-07-30 20:42:52'),
(29,'系统配置','sys:config',1,'el-icon-menu','/system/config','/system/config',0,1,6,'2021-08-03 19:37:50'),
(30,'配置新增','sys:config:add',2,NULL,NULL,NULL,0,29,1,'2021-08-03 19:41:04'),
(31,'配置删除','sys:config:delete',2,NULL,NULL,NULL,0,29,2,'2021-08-03 19:41:05'),
(32,'配置更新','sys:config:update',2,NULL,NULL,NULL,0,29,3,'2021-08-03 19:41:08'),
(33,'配置查询','sys:config:search',2,NULL,NULL,NULL,0,29,4,'2021-08-03 19:41:10');

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '角色名称',
  `order_num` INT(11) DEFAULT NULL COMMENT '显示顺序',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统角色表';
INSERT  INTO `sys_role`(`id`,`name`,`order_num`,`create_time`) VALUES 
(1,'超级管理员',1,'2021-07-30 21:08:35'),
(2,'开发人员',2,'2021-07-21 19:22:32'),
(3,'财务人员',3,'2021-07-21 19:22:34'),
(4,'运营人员',4,'2021-07-21 19:22:35'),
(5,'市场人员',5,'2021-07-21 19:22:37');

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `rid` BIGINT(20) NOT NULL COMMENT '角色id',
  `mid` BIGINT(20) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`rid`,`mid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;
INSERT  INTO `sys_role_menu`(`rid`,`mid`) VALUES 
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),
(1,7),
(1,8),
(1,9),
(1,10),
(1,11),
(1,12),
(1,13),
(1,14),
(1,15),
(1,16),
(1,17),
(1,18),
(1,19),
(1,20),
(1,21),
(1,22),
(1,23),
(1,24),
(1,25),
(1,26),
(1,27),
(1,28),
(1,29),
(1,30),
(1,31),
(1,32),
(1,33),
(2,1),
(2,2),
(2,3),
(2,4),
(2,5),
(2,6),
(3,1),
(3,7),
(3,8),
(3,9),
(3,10),
(3,11),
(3,12),
(4,1),
(4,13),
(4,14),
(4,15),
(4,16),
(4,17),
(4,18),
(5,1),
(5,19),
(5,20),
(5,21),
(5,22),
(5,23);

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户名称',
  `password` VARCHAR(128) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户密码',
  `avatar` VARCHAR(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户头像',
  `sex` INT(11) DEFAULT '0' COMMENT '用户性别（0：男、1：女）',
  `phone` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户手机',
  `email` VARCHAR(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户邮箱',
  `state` INT(1) DEFAULT '0' COMMENT '用户状态（0：正常、1：禁用）',
  `remark` VARCHAR(128) CHARACTER SET utf8 DEFAULT '' COMMENT '用户备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `dept_id` BIGINT(20) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`username`)
) ENGINE=INNODB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci COMMENT='系统用户表';
INSERT  INTO `sys_user`(`id`,`username`,`password`,`avatar`,`sex`,`phone`,`email`,`state`,`remark`,`create_time`,`dept_id`) VALUES 
(1,'zhangsan','$2a$10$/aPQXoHajpsw.71Ah6DRa.Jad47/wyuX/0Tc82M8780W92xMbntfK','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',0,'15633029014','774908833@qq.com',0,'','2021-07-21 18:10:02',2),
(2,'lisi','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',0,'15633029014','774908833@qq.com',1,'','2021-07-21 18:10:27',3),
(3,'wangwu','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',0,'15633029014','774908833@qq.com',2,'','2021-07-21 18:10:49',4),
(4,'zhaoliu','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',0,'15633029014','774908833@qq.com',3,'','2021-07-21 18:11:09',5),
(5,'xiaoqi','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',0,'15633029014','774908833@qq.com',4,'','2021-07-21 18:11:49',5),
(6,'张三','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',1,'15633029014','774908833@qq.com',0,'','2021-07-28 13:04:38',7),
(7,'李四','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',1,'15633029014','774908833@qq.com',1,'','2021-07-28 15:17:32',8),
(8,'王五','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',1,'15633029014','774908833@qq.com',2,'','2021-07-28 15:22:12',9),
(9,'赵六','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',1,'15633029014','774908833@qq.com',3,'','2021-07-28 15:32:29',10),
(10,'小七','$2a$10$M7fmKpMZEkkzrTBiKie.EeAKZhQDrWAltpCA1y/py5AU/8lyiNB8y','https://img-blog.csdnimg.cn/7bb73a6a05d64a02b8c4f50722862e6e.gif',1,'15633029014','774908833@qq.com',4,'','2021-07-28 17:55:08',10);

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `uid` BIGINT(20) NOT NULL COMMENT '用户id',
  `rid` BIGINT(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`uid`,`rid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;
INSERT  INTO `sys_user_role`(`uid`,`rid`) VALUES 
(1,1),
(1,2),
(2,3),
(3,4),
(4,5),
(5,5),
(6,1),
(6,2),
(7,3),
(8,4),
(9,5),
(10,5);