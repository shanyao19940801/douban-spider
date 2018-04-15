#创建知乎用户表
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_token` varchar(100) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `business` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `agrees` int(11) DEFAULT NULL,
  `thanks` int(11) DEFAULT NULL,
  `asks` int(11) DEFAULT NULL,
  `answers` int(11) DEFAULT NULL,
  `articles` int(11) DEFAULT NULL,
  `followees` int(11) DEFAULT NULL,
  `followers` int(11) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建usertoken表
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for usertoken
-- ----------------------------
DROP TABLE IF EXISTS `usertoken`;
CREATE TABLE `usertoken` (
  `user_token` varchar(100) NOT NULL,
  PRIMARY KEY (`user_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;