CREATE TABLE `move` (
  `id` varchar(8) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `othername` varchar(100) DEFAULT NULL COMMENT '又名',
  `director` varchar(50) DEFAULT NULL COMMENT '导演',
  `screenwriter` varchar(50) DEFAULT NULL COMMENT '编剧',
  `mainaactors` varchar(500) DEFAULT NULL COMMENT '主演',
  `type` varchar(10) DEFAULT NULL COMMENT '类型',
  `region` varchar(50) DEFAULT NULL COMMENT '制片地区',
  `language` varchar(20) DEFAULT NULL COMMENT '语言',
  `showdate` varchar(10) DEFAULT NULL COMMENT '上市日期',
  `runtime` varchar(5) DEFAULT NULL COMMENT '片长',
  `imdb` varchar(100) DEFAULT NULL COMMENT 'imdb链接',
  `score` double DEFAULT NULL COMMENT '豆瓣评分',
  `votecount` double DEFAULT NULL COMMENT '评价人数',
  `evaluationTotal` double DEFAULT NULL COMMENT '评价总数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

