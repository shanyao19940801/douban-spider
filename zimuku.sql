CREATE TABLE `t_zimu_info` (
	`id` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`zimu_id` BIGINT(10)  NOT NULL,
	`zimu_title` varchar(500)  NOT NULL COMMENT '字母名称',
	`zimu_translator` TINYINT(4)  NULL DEFAULT NULL COMMENT '翻译字幕组1:YYest',
	`zimu_language` INT(10)  not NULL COMMENT '1:双;2:繁;3:繁,双;4:英;5:英,双;6:英,繁;7:英,繁,双;8:简;9:简,双;10:繁,简;11:双,繁,简;12:简,英;13:简,英,双;14:简,英,繁;15:all',
	`zimu_quality`TINYINT(4)  NULL DEFAULT NULL COMMENT '字幕质量',
	`zimu_type` INT(10)  not NULL COMMENT '1:srt,2:ass\ssa,3:all',
	`detail_url` varchar(500) null default null comment '详情页url',
	`download_page_url` varchar(500) null default null comment '下载页url',
	`is_deleted` TINYINT(4) NOT NULL COMMENT '是否删除',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`last_update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='字幕'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;

CREATE TABLE `t_zimu_info_extend` (
	`id` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`zimu_info_id` BIGINT(10)  NOT NULL,
	`ref_type` TINYINT(10)  not NULL COMMENT '1:下载地址',
	`extend_value` varchar(500) null default null comment 'ref_type:1:下载url',
	`extend_value_type` TINYINT(10) UNSIGNED NULL default null COMMENT '1:',
	`is_deleted` TINYINT(4) NOT NULL COMMENT '是否删除',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`last_update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `zimu_info_id` (`zimu_info_id`)
)
COMMENT='字幕'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `t_zimu_html` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`html_value` text NULL default null,
	`html_type` TINYINT(4) NOT NULL COMMENT '类型1：列表，2：详情',
	`is_deleted` TINYINT NOT NULL COMMENT '是否删除',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`last_update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)

)
COMMENT='字幕源码内容'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=0
;






