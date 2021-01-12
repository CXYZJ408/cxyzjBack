/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : cxyzj

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 19/09/2019 08:09:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `head_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号登录名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号密码',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号角色',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` int(255) NULL DEFAULT NULL COMMENT '性别\n0:男\n1:女\n',
  `department_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属部门id',
  `create_date` bigint(20) NULL DEFAULT NULL COMMENT '创建日期',
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `admin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `status_id` int(45) NULL DEFAULT NULL COMMENT '账号状态id',
  `need_reset_password` tinyint(1) NULL DEFAULT 0 COMMENT '是否需要重置密码',
  PRIMARY KEY (`admin_id`) USING BTREE,
  INDEX `fk_role_id`(`role_id`) USING BTREE,
  INDEX `admin_id`(`admin_id`) USING BTREE,
  INDEX `login_name`(`login_name`) USING BTREE,
  INDEX `name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('/img/admin.jpeg', 'administrator', '6929375969c6ef7ea50411adceb3e234', 'administrator', '3', NULL, NULL, NULL, NULL, NULL, '00000000', 104, 0);

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `article_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章表的ID',
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章作者的用户名',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章的标题',
  `update_time` bigint(100) NULL DEFAULT NULL COMMENT '最近更新时间',
  `article_sum` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章简介\n',
  `label_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `views` int(11) NULL DEFAULT NULL COMMENT '浏览量',
  `comments` int(11) NULL DEFAULT NULL COMMENT '评论数',
  `collections` int(11) NULL DEFAULT NULL COMMENT '收藏数',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章markdown元数据',
  `thumbnail` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `status_id` int(5) NULL DEFAULT NULL COMMENT '表示文章状态\n0表示草稿\n1表示已发布\n2表示待审核\n3表示封禁',
  `levels` int(10) NULL DEFAULT NULL COMMENT '文章楼层数',
  PRIMARY KEY (`article_id`) USING BTREE,
  INDEX `fk_user_article_user1_idx`(`user_id`) USING BTREE,
  INDEX `fk_label`(`label_id`) USING BTREE,
  INDEX `article_id`(`article_id`) USING BTREE,
  INDEX `fk_article_status`(`status_id`) USING BTREE,
  CONSTRAINT `fk_article_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_label` FOREIGN KEY (`label_id`) REFERENCES `article_label` (`label_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('526157939635912704', '495292298867769344', '程序员之家平台网站草稿', 1561603986685, '程序员之家平台网站是一个面向有志成为程序员或相关从业人员的大学生群体而开发的，同时它也是一个大学社团即程序员之家社团的唯一官方平台，该平台的核心开发及维护成员也均为该社团成员，当然我们也欢迎非社团成员前来协助。概况平台网站主要面向大学生群体，其主要包含\n有四大模块，分为用户模块，文章模块，讨论模块以', '5', 2, 0, 0, '> 程序员之家平台网站是一==个面向有志成为程==序员或相关从业人员的大学生群体而开发的，同时它也是一个大学社团即程序员之家社团的唯一官方平台，该平台的核心开发及维护成员也均为该社团成员，当然我们也欢迎非社团成员前来协助。\n\n## 概况\n\n平台网站主要面向大学生群体，其主要包含\n有四大模块，分为用户模块，文章模块，讨论模块以及学堂模块。\n\n### 用户模块\n\n包括用户的登录注册、用户个人资料的编辑、用户站内信，用户\n中心的个性化修改、用户对自己文章、收藏、讨论及评论的管理，对其他用户的关注，以及后台管理员对用户的权限管理等。\n\n### 文章模块\n\n用户文章的撰写与浏览，文章评论的发表、个人文章的收藏，文章个性化推荐系统，管理员对文章及其评论的管理。\n\n### 讨论模块\n\n小型社交网络，用户讨论的实时发布和讨论分享。\n\n### 学堂模块\n\n成体系的学习路线规划以及用户自定义学习路线规划。\n\n## 技术实现细节\n\n平台整体使用MVVM设计模式，前端负责页面渲染、数据展示、路由的导航等，后端负责各类API接口的实现\n\n### 前端技术栈\n\n语言：HTML、CSS、JavaScript\n\n服务器：Node.js\n\n框架：Nuxt、Vue.js\n\nUI框架：Element UI、Vuetify\n\n开发工具：Webstorm、Cmder\n\n版本管理工具：git\n\n第三方库：axios、lodash、qs、zxcvbn（太多就不列了）...\n\n### 后端技术栈\n\n语言：Java、SQL、JavaScript\n\n服务器：Tomcat、Node.js、Elasticsearch\n\n项目自动化建构工具：Gradle\n\n框架：Spring Boot、Spring Security、Spring Data、Koa\n\n数据库：MySql、Redis、MongoDB\n\n开发工具：IDEA、Webstorm\n\n版本管理工具：git\n\n第三方库：JJWT、socket.io、lombok（暂时）\n\n### 说明\n\n前端部分为了解决SEO问题同时保证首屏渲染的效率使用了SSR，而Nuxt作为Vue官方推荐的SSR框架也是一个不二的选择，UI框架方面选择了Vuetify与Element，但整体还是以Vuetify为主，Element作为一个补充，虽然一开始想使用Quasar，但由于其对于Nuxt的支持还在进行中，所以只能舍弃，最后前端项目页面的渲染服务器使用Node，同时其也作为与后端服务器交互的中间层。\n\n后端分为两部分，一部分是以Java为核心的主服务器，另一个是以JS为核心的辅助服务器。\n\n主服务器用于运行平台网站主要的业务逻辑，框架上使用Spring Boot+Spring Security+Spring Data，Spring Boot用于实现RESTful API服务，Spring Security配合JWT协议用于服务器的访问权限控制，Spring Data用于对MySql与Redis的访问，使用Elasticsearch作为文档检索服务器。\n\n辅助服务器用于运行平台网站的一个子服务，即用户站内信功能，因为考虑到Node.js对于web-socket的完美支持，而且JavaScript对callback同步模式具有原生态支持，所以与java相比，JavaScript十分适合用于该功能的实现，且不存在性能上的问题。具体框架搭配使用Koa+Sockt.io+MongoDB来进行相关的实现。\n\n## 项目进度\n\n符号说明：\n\n`*：开发中`\n\n`#：未开始`\n\n`-`：前后端对接或DEBUG状态\n\n1. 用户模块\n   - [x] 用户登录、注册、忘记密码\n   - [x] 用户信息修改及个人中心的个性化修改\n   - [ ] 用户关注功能以及相关列表信息的获取`-`\n   - [ ] 用户个人中心`*`\n   - [ ] 第三方登录`#`\n   - [ ] 后台管理`#`\n2. 文章模块\n   - [ ] 文章列表的获取`-`\n   - [ ] 文章标签系统`*`\n   - [ ] 文章撰写功能`-`\n   - [ ] 文章评论系统`*`\n   - [ ] 后台管理`#`\n\n3. 讨论模块`#`\n4. 学堂模块`#`\n\n## 相关设计文档\n\n1. [API设计文档](https://www.eolinker.com/#/share/index?shareCode=d9dbhT)（有些地方可能会因为更新不及时与项目有冲突）\n2. [UI设计（前台·新版）](https://free.modao.cc/app/66d954fac42a1e3c0e4ae7d616b9609531e382cc#screen=s25615085e0dc00ad25bcc2)（仅供参考，以项目实例为主）\n3. [UI设计（前台·旧版）](https://free.modao.cc/app/J8er6v7j4fjhaTsbw1SowJHz7c41WoQ#screen=sEEEF5346B51510381810655)\n4. [UI设计（后台）](https://free.modao.cc/app/jQOWsoXVG4VUZ2gWAKvvI31dZ77QOh3)(项目后台还未开始做，可能做的时候，该UI界面变动会较大))\n5. [数据库](https://github.com/CXYZJ408/-/blob/master/cxyzj.sql)(包含了一些测试数据)\n6. [测试说明文档](https://github.com/CXYZJ408/-/blob/master/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E/%E6%B5%8B%E8%AF%95%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3.md)\n7. [代码规范文档](https://github.com/CXYZJ408/-/blob/master/%E4%BB%A3%E7%A0%81%E8%A7%84%E8%8C%83.md)\n8. [用户安全验证系统说明](https://github.com/CXYZJ408/-/blob/master/%E7%94%A8%E6%88%B7%E5%AE%89%E5%85%A8%E9%AA%8C%E8%AF%81%E7%B3%BB%E7%BB%9F.md)\n9. [接口编写说明文档](https://github.com/CXYZJ408/-/blob/master/%E6%8E%A5%E5%8F%A3%E7%BC%96%E5%86%99%E8%AF%B4%E6%98%8E.md)\n\n\n## 项目地址\n\n1. [前端](https://github.com/CXYZJ408/cxyzjFront)\n2. [后端](https://github.com/CXYZJ408/cxyzjback)![3.jpg](/img/Article/7439ad5c-d981-46dc-927e-9e05b62e4b85.jpg)', 'http://localhost:3000/img/Article/7439ad5c-d981-46dc-927e-9e05b62e4b85.jpg', 101, 0);
INSERT INTO `article` VALUES ('530097142904127488', '495292298867769344', '你好！', 1546426607590, '大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多', '1', 3, 1, 0, '大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多大声道撒多', '', 101, 1);
INSERT INTO `article` VALUES ('530108145960222720', '530104365562724352', 'Token', 1546429230920, 'eyJhbGci,OiJIUzUxMiJ9 .eyJ1c,2VySWQiOiI1MjcxNzA4ODIzNjg0Mzg yNzIiLCJyb 2xlIjoiUk9MRV9VU0VSIiwiZXhwIj oyMzAyNzgwMDYzLCJpYXQ iOjE1NDU5MTY wNjMsImlzcy I6', '2', 4, 0, 0, 'eyJhbGci,OiJIUzUxMiJ9 .eyJ1c,2VySWQiOiI1MjcxNzA4ODIzNjg0Mzg yNzIiLCJyb 2xlIjoiUk9MRV9VU0VSIiwiZXhwIj oyMzAyNzgwMDYzLCJpYXQ iOjE1NDU5MTY wNjMsImlzcy I6ImN4eXpqIiwic3Vi Ijoi,VG 9rZW4iLCJ hdWQiOiJVc2V yIiwianRpIjoiODI2ZDQzNDItYTQwMy0 adf ads 水电费水电费0ZGJiLTg0MDMtO TJmZWQ4YjM5OGZhIn0.rIvtfLZKr1RgvuMhtwVaaGIhsnuHiMl3vY7gRotjd3_7jB kYzs6arIffh6LYuf2LyQYv4z2Vqs8zBk_sSu2GlQ![TIM截图20190101000231.png](/img/Article/cb8046f9-9347-444d-920e-bec1cef1955f.png)', 'http://localhost:3000/img/Article/cb8046f9-9347-444d-920e-bec1cef1955f.png', 101, 0);
INSERT INTO `article` VALUES ('560035853536067584', '495292298867769344', 'asdasd ', 1553564552427, 'asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asd', '5', 2, 0, 0, 'asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad asdsad ', '', 101, 0);
INSERT INTO `article` VALUES ('560164950689972224', '560054676439957504', '我的世界', 1553595331595, '我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界', '3', 1, 0, 0, '我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界我的世界', '', 101, 0);
INSERT INTO `article` VALUES ('568111931076902912', '495292298867769344', '网站开发安排', 1555490039354, '程序员之家平台网站开发安排表目录程序员之家平台网站开发安排表总体功能需求后台管理界面前台界面时间安排总体功能需求后台管理界面与后端数据库链接起来\n首页的数据可视化\n轮播图管理\n用户管理\n权限系统的设计与开发\n部门管理\n文章管理\n讨论管理前台界面BUG的修复，动画效果的优化\n轮播图\n通知系统的设计与开', '3', 1, 0, 0, '# 程序员之家平台网站开发安排表\n\n@[TOC]\n\n## 总体功能需求\n\n### 后台管理界面\n\n1. 与后端数据库链接起来\n2. 首页的数据可视化\n3. 轮播图管理\n4. 用户管理\n5. 权限系统的设计与开发\n6. 部门管理\n7. 文章管理\n8. 讨论管理\n\n### 前台界面\n\n1. BUG的修复，动画效果的优化\n2. 轮播图\n3. 通知系统的设计与开发\n4. 讨论模块的设计与开发\n5. 全局搜索\n\n## 时间安排\n\n| 时间安排 | 第一周（3.18~3.24） | 第二周（3.25~3.31） | 第三周（4.1~4.7） | 第四周（4.8~4.14） | 第五周（4.15~4.21） | 第六周（4.22~4.28） |\n| :------: | :-----------------: | :-----------------: | :---------------: | :----------------: | :-----------------: | :-----------------: |\n|   前台   |         1,2         |          -          |         -         |         3          |         ...         |         ...         |\n|   后台   |        1,2,3        |         4,5         |        5,7        |         6          |         ...         |         ...         |\n|   备注   |                     |                     |                   |                    |                     |                     |\n\n', '', 101, 0);
INSERT INTO `article` VALUES ('593756110238777344', '495292298867769344', '网站开发安排', 1561604088130, '程序员之家平台网站开发安排表总体功能需求后台管理界面与后端数据库链接起来\n首页的数据可视化\n轮播图管理\n用户管理\n权限系统的设计与开发\n部门管理\n文章管理\n讨论管理前台界面BUG的修复，动画效果的优化\n轮播图\n通知系统的设计与开发\n讨论模块的设计与开发\n全局搜索时间安排时间安排\n第一周（3.18~3', '5', 1, 0, 0, '# 程序员之家平台网站开发安排表\n\n\n## 总体功能需求\n\n### 后台管理界面\n\n1. 与后端数据库链接起来\n2. 首页的数据可视化\n3. 轮播图管理\n4. 用户管理\n5. 权限系统的设计与开发\n6. 部门管理\n7. 文章管理\n8. 讨论管理\n\n### 前台界面\n\n1. BUG的修复，动画效果的优化\n2. 轮播图\n3. 通知系统的设计与开发\n4. 讨论模块的设计与开发\n5. 全局搜索\n\n## 时间安排\n\n| 时间安排 | 第一周（3.18~3.24） | 第二周（3.25~3.31） | 第三周（4.1~4.7） | 第四周（4.8~4.14） | 第五周（4.15~4.21） | 第六周（4.22~4.28） |\n| :------: | :-----------------: | :-----------------: | :---------------: | :----------------: | :-----------------: | :-----------------: |\n|   前台   |         1,2         |          -          |         -         |         3          |         ...         |         ...         |\n|   后台   |        1,2,3        |         4,5         |        5,7        |         6          |         ...         |         ...         |\n|   备注   |                     |                     |                   |                    |                     |                     |\n\n', '', 101, 0);
INSERT INTO `article` VALUES ('593756231215087616', '495292298867769344', '网站开发安排', 1561604116962, '程序员之家平台网站开发安排表[TOC]总体功能需求后台管理界面与后端数据库链接起来\n首页的数据可视化\n轮播图管理\n用户管理\n权限系统的设计与开发\n部门管理\n文章管理\n讨论管理前台界面BUG的修复，动画效果的优化\n轮播图\n通知系统的设计与开发\n讨论模块的设计与开发\n全局搜索时间安排时间安排\n第一周（3', '5', 1, 0, 0, '# 程序员之家平台网站开发安排表\r\n\r\n[TOC]\r\n\r\n## 总体功能需求\r\n\r\n### 后台管理界面\r\n\r\n1. 与后端数据库链接起来\r\n2. 首页的数据可视化\r\n3. 轮播图管理\r\n4. 用户管理\r\n5. 权限系统的设计与开发\r\n6. 部门管理\r\n7. 文章管理\r\n8. 讨论管理\r\n\r\n### 前台界面\r\n\r\n1. BUG的修复，动画效果的优化\r\n2. 轮播图\r\n3. 通知系统的设计与开发\r\n4. 讨论模块的设计与开发\r\n5. 全局搜索\r\n\r\n## 时间安排\r\n\r\n| 时间安排 | 第一周（3.18~3.24） | 第二周（3.25~3.31） | 第三周（4.1~4.7） | 第四周（4.8~4.14） | 第五周（4.15~4.21） | 第六周（4.22~4.28） |\r\n| :------: | :-----------------: | :-----------------: | :---------------: | :----------------: | :-----------------: | :-----------------: |\r\n|   前台   |         1,2         |          -          |         -         |         3          |         ...         |         ...         |\r\n|   后台   |        1,2,3        |         4,5         |        5,7        |         6          |         ...         |         ...         |\r\n|   备注   |                     |                     |                   |                    |                     |                     |\r\n\r\n', '', 101, 0);

-- ----------------------------
-- Table structure for article_collection
-- ----------------------------
DROP TABLE IF EXISTS `article_collection`;
CREATE TABLE `article_collection`  (
  `id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '索引ID',
  `article_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章id',
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_collect_article1_idx`(`article_id`) USING BTREE,
  INDEX `fk_userid`(`user_id`) USING BTREE,
  CONSTRAINT `fk_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_draft
-- ----------------------------
DROP TABLE IF EXISTS `article_draft`;
CREATE TABLE `article_draft`  (
  `draft_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '草稿表的ID',
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文章作者的用户名',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章的标题',
  `update_time` bigint(100) NULL DEFAULT NULL COMMENT '最近更新时间',
  `label_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章markdown元数据',
  `article_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章id',
  PRIMARY KEY (`draft_id`) USING BTREE,
  INDEX `fk_user_article_user1_idx`(`user_id`) USING BTREE,
  INDEX `fk_label`(`label_id`) USING BTREE,
  INDEX `article_draft_id`(`draft_id`) USING BTREE,
  INDEX `article_draft_ibfk_2`(`article_id`) USING BTREE,
  CONSTRAINT `article_draft_ibfk_1` FOREIGN KEY (`label_id`) REFERENCES `article_label` (`label_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `article_draft_ibfk_2` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `article_draft_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_draft
-- ----------------------------
INSERT INTO `article_draft` VALUES ('567831514109181952', '495292298867769344', 'temp', 1555423177275, '-1', 'http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;http://www.wheelsfactory.cn/#/search?searchtype=bySearch;https://github.com/scrollreveal/scrollreveal;', NULL);
INSERT INTO `article_draft` VALUES ('567831634976440320', '495292298867769344', '今日安排', 1555423185677, '-1', '两个缓存，一个存放在redis中，一个需要自己手写后放在内存中维护\r\n\r\n\r\n\r\n\r\n\r\n', NULL);
INSERT INTO `article_draft` VALUES ('568111931282423809', '495292298867769344', '未命名...', 1555489900509, '-1', 'asdasd', NULL);
INSERT INTO `article_draft` VALUES ('568111931282423810', '495292298867769344', '今日安排', 1555489905309, '-1', '两个缓存，一个存放在redis中，一个需要自己手写后放在内存中维护\r\n\r\n\r\n\r\n\r\n\r\n', NULL);

-- ----------------------------
-- Table structure for article_label
-- ----------------------------
DROP TABLE IF EXISTS `article_label`;
CREATE TABLE `article_label`  (
  `label_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签ID',
  `label_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `quantity` int(11) NULL DEFAULT NULL COMMENT '该标签下文章的数量',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签图标',
  `collections` int(11) NULL DEFAULT NULL COMMENT '有多少个用户选择了该标签',
  `introduce` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签介绍',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article_label
-- ----------------------------
INSERT INTO `article_label` VALUES ('-1', '无', 3, '0', 0, '0');
INSERT INTO `article_label` VALUES ('1', '前端', 16, '#icon-front', 4, '前端即网站前台部分，运行在PC端，移动端等浏览器上展现给用户浏览的网页。随着互联网技术的发展，HTML5，CSS3，前端框架的应用，跨平台响应式网页设计能够适应各种屏幕分辨率，完美的动效设计，给用户带来极高的用户体验。');
INSERT INTO `article_label` VALUES ('2', '操作系统', 30, '#icon-os', 5, '操作系统（英语：operating system，缩写作OS）是管理计算机硬件与软件资源的计算机程序，同时也是计算机系统的内核与基石。操作系统需要处理如管理与配置内存、决定系统资源供需的优先次序、控制输入与输出设备、操作网络与管理文件系统等基本事务。操作系统也提供一个让用户与系统交互的操作界面。');
INSERT INTO `article_label` VALUES ('3', 'Android', 8, '#icon-android', 3, 'Android是一种基于Linux的自由及开放源代码的操作系统，主要使用于移动设备，如智能手机和平板电脑，由Google公司和开放手机联盟领导及开发。尚未有统一中文名称，中国大陆地区较多人使用“安卓”或“安致”。Android操作系统最初由AndyRubin开发，主要支持手机。2005年8月由Google收购注资。2007年11月，Google与84家硬件制造商、软件开发商及电信营运商组建开放手机联盟共同研发改良Android系统。随后Google以Apache开源许可证的授权方式，发布了Android的源代码。第一部Android智能手机发布于2008年10月。Android逐渐扩展到平板电脑及其他领域上，如电视、数码相机、游戏机、智能手表等。2011年第一季度，Android在全球的市场份额首次超过塞班系统，跃居全球第一。 2013年的第四季度，Android平台手机的全球市场份额已经达到78.1%。2013年09月24日谷歌开发的操作系统Android在迎来了5岁生日，全世界采用这款系统的设备数量已经达到10亿台。');
INSERT INTO `article_label` VALUES ('4', '人工智能', 8, '#icon-AI', 2, '人工智能（Artificial Intelligence），英文缩写为AI。它是研究、开发用于模拟、延伸和扩展人的智能的理论、方法、技术及应用系统的一门新的技术科学。 人工智能是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器，该领域的研究包括机器人、语言识别、图像识别、自然语言处理和专家系统等。人工智能从诞生以来，理论和技术日益成熟，应用领域也不断扩大，可以设想，未来人工智能带来的科技产品，将会是人类智慧的“容器”。人工智能可以对人的意识、思维的信息过程的模拟。 人工智能是一门极富挑战性的科学，从事这项工作的人必须懂得计算机知识，心理学和哲学。人工智能是包括十分广泛的科学，它由不同的领域组成，如机器学习，计算机视觉等等，总的说来，人工智能研究的一个主要目标是使机器能够胜任一些通常需要人类智能才能完成的复杂工作。2017年12月，人工智能入选“2017年度中国媒体十大流行语”。');
INSERT INTO `article_label` VALUES ('5', '算法', 112, '#icon-arithmetic', 4, '算法（Algorithm）是指解题方案的准确而完整的描述，是一系列解决问题的清晰指令，算法代表着用系统的方法描述解决问题的策略机制。也就是说，能够对一定规范的输入，在有限时间内获得所要求的输出。如果一个算法有缺陷，或不适合于某个问题，执行这个算法将不会解决这个问题。不同的算法可能用不同的时间、空间或效率来完成同样的任务。一个算法的优劣可以用空间复杂度与时间复杂度来衡量。 形式化算法的概念部分源自尝试解决希尔伯特提出的判定问题，并在其后尝试定义有效计算性或者有效方法中成形。这些尝试包括库尔特·哥德尔、Jacques Herbrand和斯蒂芬·科尔·克莱尼分别于1930年、1934年和1935年提出的递归函数，阿隆佐·邱奇于1936年提出的λ演算，1936年Emil Leon Post的Formulation 1和艾伦·图灵1937年提出的图灵机。即使在当前，依然常有直觉想法难以定义为形式化算法的情况。');

-- ----------------------------
-- Table structure for attention
-- ----------------------------
DROP TABLE IF EXISTS `attention`;
CREATE TABLE `attention`  (
  `attention_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关注表的编号',
  `user_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户',
  `target_user` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标用户',
  `status_id` int(5) NULL DEFAULT NULL COMMENT '状态信息:\n\n当目标对象为关注者，标示为1；\n\n当目标对象为被关注者，标示为2；\n\n当双方互相关注，标示为3；\n\n也就是说，当status为1时user为被关注的，status为2时，user时关注着，status为3时，user既是关注者也是被关注者\n',
  PRIMARY KEY (`attention_id`) USING BTREE,
  INDEX `fk_attention_user1_idx`(`user_id`) USING BTREE,
  INDEX `fk_attention_user2_idx`(`target_user`) USING BTREE,
  INDEX `fk_att_status`(`status_id`) USING BTREE,
  CONSTRAINT `fk_att_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_att_target` FOREIGN KEY (`target_user`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_att_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attention
-- ----------------------------
INSERT INTO `attention` VALUES ('582939452033204224', '495292298867769344', '530104365562724352', 201);
INSERT INTO `attention` VALUES ('582939452062564352', '530104365562724352', '495292298867769344', 202);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论编号',
  `discusser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发出评论的用户名',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论正文500字',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '评论的创建时间',
  `support` int(11) NULL DEFAULT NULL COMMENT '支持数',
  `object` int(11) NULL DEFAULT NULL COMMENT '反对数',
  `target_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标对象id，目标对象可以是文章也可是教程',
  `mode` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论类型，值分为article和course',
  `level` int(10) NULL DEFAULT NULL COMMENT '楼层',
  `children` int(10) NULL DEFAULT NULL COMMENT '子评论数',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `fk_com_id`(`comment_id`) USING BTREE,
  INDEX `fk_comm_discusser`(`discusser`) USING BTREE,
  INDEX `fk_comm_target`(`target_id`) USING BTREE,
  CONSTRAINT `fk_comm_discusser` FOREIGN KEY (`discusser`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('530097210726023168', '495292298867769344', '打算打算多打算打算多打算打算多打算打算多', 1546426623763, 0, 0, '530097142904127488', 'articles', 1, 0);
INSERT INTO `comment` VALUES ('560165053869850624', '560054676439957504', '你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊', 1553595356202, 0, 0, '526157939635912704', 'articles', 1, 2);

-- ----------------------------
-- Table structure for comment_vote
-- ----------------------------
DROP TABLE IF EXISTS `comment_vote`;
CREATE TABLE `comment_vote`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `target_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标对象id，可以为文章或讨论的ID',
  `status_id` int(5) NULL DEFAULT NULL COMMENT '状态id',
  `comment_vote_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `comment_reply_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论或回复ID',
  PRIMARY KEY (`comment_vote_id`) USING BTREE,
  INDEX `vote_target_id`(`target_id`) USING BTREE,
  INDEX `vote_comment_reply_id`(`comment_reply_id`) USING BTREE,
  INDEX `fk_vote_user`(`user_id`) USING BTREE,
  INDEX `fk_vote_status`(`status_id`) USING BTREE,
  CONSTRAINT `fk_vote_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_vote_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_vote
-- ----------------------------
INSERT INTO `comment_vote` VALUES ('495292298867769344', '526157939635912704', 1, '582938963757498368', '560165131422531584');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `department_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名',
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `permission_id` varbinary(255) NOT NULL COMMENT '权限ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `reply_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '	回复的编号',
  `comment_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复所依附的主评论id',
  `replier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发出回复的用户',
  `discusser` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被回复的用户',
  `create_time` bigint(45) NULL DEFAULT NULL COMMENT '回复时间',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '回复内容500字',
  `support` int(11) NULL DEFAULT NULL COMMENT '支持数',
  `object` int(11) NULL DEFAULT NULL COMMENT '反对数',
  `target_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标id，可以是文章也可以是讨论或教程',
  `mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标类型',
  PRIMARY KEY (`reply_id`) USING BTREE,
  INDEX `fk_reply_comment1_idx`(`comment_id`) USING BTREE,
  INDEX `reply_id`(`reply_id`) USING BTREE,
  INDEX `reply_target_id`(`target_id`) USING BTREE,
  INDEX `fk_comm_replier`(`replier`) USING BTREE,
  INDEX `fk_comm_disscusser`(`discusser`) USING BTREE,
  CONSTRAINT `fk_comm` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comm_disscusser` FOREIGN KEY (`discusser`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comm_replier` FOREIGN KEY (`replier`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES ('560165131422531584', '560165053869850624', '560054676439957504', '560054676439957504', 1553595374694, '事实上事实上', 1, 0, '526157939635912704', 'articles');
INSERT INTO `reply` VALUES ('568122238096637952', '560165053869850624', '495292298867769344', '560054676439957504', 1555492496759, '你好！！！！！😜🐣', 0, 0, '526157939635912704', 'articles');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permissions` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限信息列表',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('0', 'ROLE_ANONYMITY', '匿名用户', NULL);
INSERT INTO `role` VALUES ('1', 'ROLE_USER', '普通用户', NULL);
INSERT INTO `role` VALUES ('2', 'ROLE_ADMIN', '管理员', NULL);
INSERT INTO `role` VALUES ('3', 'ROLE_ADMINISTRATORS', '超级管理员', NULL);

-- ----------------------------
-- Table structure for slide
-- ----------------------------
DROP TABLE IF EXISTS `slide`;
CREATE TABLE `slide`  (
  `slide_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图的id',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图跳转的链接',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图图片地址',
  `index` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图次序',
  PRIMARY KEY (`slide_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status`  (
  `status_id` int(5) NOT NULL COMMENT '状态id',
  `status` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态信息',
  `status_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`status_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES (0, 'object', '反对');
INSERT INTO `status` VALUES (1, 'support', '支持');
INSERT INTO `status` VALUES (100, 'draft', '草稿');
INSERT INTO `status` VALUES (101, 'publish', '已发布');
INSERT INTO `status` VALUES (102, 'checking', '审核中');
INSERT INTO `status` VALUES (103, 'unable', '冻结');
INSERT INTO `status` VALUES (104, 'enable', '启用(正常)');
INSERT INTO `status` VALUES (201, 'focus', '关注');
INSERT INTO `status` VALUES (202, 'followed', '被关注');
INSERT INTO `status` VALUES (203, 'each', '互相关注');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` int(3) NULL DEFAULT NULL COMMENT '性别\n0:男\n1:女\n2:保密',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `bg_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主页背景图片路径',
  `introduce` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人简介\n',
  `regist_date` bigint(20) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '注册日期',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `theme_color` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题颜色',
  `head_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像路径',
  `attentions` int(10) NULL DEFAULT NULL COMMENT '关注数量',
  `fans` int(10) NULL DEFAULT NULL COMMENT '粉丝数量',
  `articles` int(10) NULL DEFAULT NULL COMMENT '文章数',
  `discussions` int(10) NULL DEFAULT NULL COMMENT '讨论数',
  `comments` int(10) NULL DEFAULT NULL COMMENT '评论数',
  `role_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `login_date` bigint(255) NULL DEFAULT NULL COMMENT '用户登陆的时间',
  `status_id` int(5) NULL DEFAULT NULL COMMENT '状态id',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `email`(`email`) USING BTREE,
  INDEX `phone`(`phone`) USING BTREE,
  INDEX `nickname`(`nickname`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `fk_user_role`(`role_id`) USING BTREE,
  INDEX `fk_user_status`(`status_id`) USING BTREE,
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('487005383798292482', '0', 'ANONYMITY', 0, '0', '0', '0', 00000000000000000000, '0', '0', '0', 0, 0, 0, 0, 0, '0', 0, 104);
INSERT INTO `user` VALUES ('495292298867769344', '30040a3c37742faa539edb5b5bf20dce', 'yaser1', 2, '335767798@qq.com', '/img/Background/d6b2d50d-45ae-4e85-ad37-cbc9319b4dc5.jpg', '北美码工，微信公众号ninechapter。互联网时代，科技界界的一切也在随着浪潮加速改变，Google的崛起，Facebook的逆袭……在这样飞速改变和发展的环境下，昔日的业界霸主，如果不谙世事，用不了多久，就会被后来者所取代。', 00000001538128486113, '17602545735', 'rgba(225, 255, 225, 1)', '/img/Avatar/355daea6-2980-4c7b-9faf-672e479f26f3.jpeg', 2, 1, 9, 0, 78, '1', 1561604968823, 104);
INSERT INTO `user` VALUES ('530104365562724352', 'c167bcdf3d93dc21f59e6a490cc58855', 'wanyuxuan', 2, '4@qq.com', '/img/Background/0b45a4e9-72b1-4d51-b48e-4004e63b52af.jpg', '这个人很懒，连介绍都没有(￢︿̫̿￢☆)', 00000001546428329596, '1', 'rgba(255,255,255,.9)', '/img/Avatar/a8db7212-796c-4424-902f-907272271cc0.jpeg', 0, 1, 1, 0, 0, '1', 1546429154447, 104);
INSERT INTO `user` VALUES ('560054676439957504', '8668d80f474934d91180b02284830674', '亚1瑟人', 1, '1805079589@qq.com', '/img/Background/default.jpg', '这个人很懒，连介绍都没有(￢︿̫̿￢☆)', 00000001553569040166, '1', 'rgba(31, 147, 255, 0.09)', '/img/Avatar/cxyzj.png', 0, 0, 1, 0, 2, '1', 1553569040176, 104);
INSERT INTO `user` VALUES ('567818973911973888', '30040a3c37742faa539edb5b5bf20dce', 'wyx1', 0, 'gylxx81w@126.com', '/img/Background/default.jpg', '这个人很懒，连介绍都没有(￢︿̫̿￢☆)', 00000001555420192930, '1', 'rgba(255,255,255,.9)', '/img/Avatar/cxyzj.png', 0, 0, 0, 0, 0, '1', 1555420192940, 104);
INSERT INTO `user` VALUES ('582600607731286016', '30040a3c37742faa539edb5b5bf20dce', '5wyx', 2, '4@qq.com', '/img/Background/1736f552-3c8c-4ff7-9bf8-2f270cdef1db.jpg', '这个人很懒，连介绍都没有(￢︿̫̿￢☆)', 00000001558944409063, '1', 'rgba(255,255,255,.9)', '/img/Avatar/6e894a36-0210-4adb-af3e-8833805abf81.jpeg', 0, 0, 0, 0, 0, '1', 1558944409072, 104);
INSERT INTO `user` VALUES ('593746820593614848', '30040a3c37742faa539edb5b5bf20dce', 'test1', 2, 'gylxx51w@126.com', '/img/Background/default.jpg', '这个人很懒，连介绍都没有(￢︿̫̿￢☆)', 00000001561601873295, '15250823423', 'rgba(255,255,255,.9)', '/img/Avatar/57de4f84-84cc-4360-a256-7873ff291b4d.jpeg', 0, 0, 0, 0, 0, '1', 1561601947758, 104);

-- ----------------------------
-- Table structure for user_label
-- ----------------------------
DROP TABLE IF EXISTS `user_label`;
CREATE TABLE `user_label`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `labels` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户标签组',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_label_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_label_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_label
-- ----------------------------
INSERT INTO `user_label` VALUES ('495292298867769344', '0', '3,4,2,5');
INSERT INTO `user_label` VALUES ('530104365562724352', '530108289023737856', '5,2,1,4');
INSERT INTO `user_label` VALUES ('560054676439957504', '560054804584333312', '5,2,1,3');
INSERT INTO `user_label` VALUES ('593746820593614848', '593746893629030400', ',2,1,3,5');

SET FOREIGN_KEY_CHECKS = 1;
