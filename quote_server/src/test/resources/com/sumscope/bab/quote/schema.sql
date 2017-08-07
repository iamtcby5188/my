-- -----------------------------------------------------
-- Table `ssr_quote`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ssr_quote` ;

CREATE TABLE IF NOT EXISTS `ssr_quote` (
  `id` VARCHAR(32) NOT NULL COMMENT '数据库主键字段，一个UUID字符串，唯一标记票据报价单\n',
  `direction` VARCHAR(3) NULL COMMENT '报价方向：\nOUT： 卖出\nIN： 买入\nUDF：不限，用于全国直贴与全国转贴',
  `bill_type` VARCHAR(3) NULL COMMENT '票据类型\nBKB： 银行票据 Bank Bill\nCMB：商业票据 Company Bill',
  `bill_medium` VARCHAR(3) NULL COMMENT '票据介质：\nPAP：纸票\nELE：电票',
  `memo` VARCHAR(500) NULL COMMENT '备注，最多250汉字',
  `quote_company_id` VARCHAR(32) NULL COMMENT '报价机构ID，该ID为IAM系统中机构ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。 \n',
  `contact_id` VARCHAR(32) NULL COMMENT '联系人ID，为IAM系统中用户ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。',
  `operator_id` VARCHAR(32) NULL COMMENT '操作人ID，为实际建立和维护报价单的IAM用户ID，一般情况下与quoter_id一致。',
  `create_datetime` DATETIME NULL COMMENT '报价单建立日期',
  `last_update_datetime` DATETIME NULL COMMENT '最后更新日期',
  `effective_datetime` DATETIME NULL COMMENT '生效日期。用户可以今天报明天开始生效的报价。',
  `expired_datetime` DATETIME NULL COMMENT '报价单作废日期，默认逻辑是生效日期的晚上22：00',
  `quote_status` VARCHAR(3) NULL COMMENT '报价单状态\nDFT: 草案， draft\nDSB: 已发布，distributed\nDLD：已成交，dealed\nCAL: 已撤销， canceled \nDEL: 已删除， deleted',
  `quote_price_type` VARCHAR(3) NULL COMMENT '报价类型\n可用枚举为：\n银票\nbill_type = BKB\nGG：国股 （1,3）\nCS：城商 (4)\nNS：农商 \nNX：农信 (6)\nNH：农合\nCZ：村镇 (7)\nWZ：外资 (8)\nCW：财务 (f)',
  `accpeting_house_id` VARCHAR(32) NULL COMMENT '承兑方ID，该ID为黄页系统ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。',
  `due_date` DATETIME NULL COMMENT '到期日期',
  `price` DECIMAL(6,3) NULL COMMENT '价格',
  `amount` DECIMAL(15,2) NULL COMMENT '金额',
  `province_code` VARCHAR(5) NULL COMMENT '区域code，IAM系统Code',
  `base64_img` VARCHAR(204800) NULL COMMENT 'Base64 Image',
  PRIMARY KEY (`id`))

COMMENT = '票据报价主表，记录报价单主要信息\n';


-- -----------------------------------------------------
-- Table `bab_accpeting_company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bab_accpeting_company` ;

CREATE TABLE IF NOT EXISTS `bab_accpeting_company` (
  `ID` varchar(32) NOT NULL COMMENT '机构ID，既黄页系统该机构ID。',
  `bab_accpeting_company_type` varchar(3) DEFAULT NULL COMMENT '机构类型枚举值。可选范围：\n央企 CET central enterprise \n国企 SOE state-owned enterprise\n地方性企业 LET local enterprise\n其他 OTH others',
  `company_name` varchar(45) DEFAULT NULL COMMENT '企业名称\n',
  `iam_company_id` varchar(32) DEFAULT NULL COMMENT '该机构对应的IAM机构ID，可空',
  `manager` varchar(45) DEFAULT NULL COMMENT '企业管理人',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `registration_number` varchar(30) DEFAULT NULL COMMENT '企业工商注册号',
  `last_syn_datetime` datetime DEFAULT NULL COMMENT '最后与黄页系统同步日期\n',
  `expired_datetime` datetime DEFAULT NULL COMMENT '过期日期。本列表并不维护所有曾经发过商业报价单的承兑企业，而是根据过期字段自动删除一部分数据。该日期默认是最后一次报价的一个月之后。',
  `company_name_py` varchar(64) DEFAULT NULL,
  `company_name_pinyin` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`ID`))

COMMENT = '票据承兑行列表。该列表保留了曾经在票据系统进行商票报价的企业信息。这些企业必定是从黄页系统获取的，并本地保留了企业的基础信息。该信息需要定期刷新。';


-- -----------------------------------------------------
-- Table `quote_price_trend`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quote_price_trend` ;

CREATE TABLE IF NOT EXISTS `quote_price_trend` (
  `ID` VARCHAR(32) NOT NULL COMMENT '主键',
  `quote_date` DATETIME NULL COMMENT '报价日期，只有日期部分，不记录时间部分。',
  `minor_flag` INT(1) NULL COMMENT 'true： 小纸票（银行） or 小电票（银行）\nfalse： 纸票（银行） or 电票（银行）\n不为商票查询服务',
  `bill_medium` VARCHAR(3) NULL COMMENT '纸票 or 电票',
  `bill_type` VARCHAR(3) NULL COMMENT '银行承兑 or 商业机构承兑',
  `quote_price_type` VARCHAR(3) NULL COMMENT '报价价格类型：\nGG \"国股\nCS \"城商\"\nNS \"农商\"\nNX \"农信\"\nNH \"农合\"\nCZ \"村镇\"\nWZ \"外资\"\nCW \"财务\"\nYBH \"有保函\"\nWBH \"无保函\"',
  `price_max` DECIMAL(6,3) NULL COMMENT '最高价格',
  `price_avg` DECIMAL(6,3) NULL COMMENT '平均价，平均价计算去除了报价为空的报价单，并且仅计算在合理价格区间中的价格。',
  `price_min` DECIMAL(6,3) NULL COMMENT '最低价格，去除了不合理报价区间的价格',
  `create_datetime` DATETIME NULL COMMENT '该记录产生的时间',
  `trade_type` VARCHAR(3) NULL COMMENT '交易模式：买断or 回购。 仅用于全国转贴',
  `quote_type` VARCHAR(3) NULL COMMENT '报价单类型：全国转贴（NPC）or全国直贴（SSC）',
  PRIMARY KEY (`ID`))
COMMENT = '价格走势表。记录每个报价类型的价格的最高价，最低价及平均价。';


-- -----------------------------------------------------
-- Table `ssc_quote`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ssc_quote` ;

CREATE TABLE IF NOT EXISTS `ssc_quote` (
  `id` VARCHAR(32) NOT NULL COMMENT '数据库主键字段，一个UUID字符串，唯一标记票据报价单\n',
  `direction` VARCHAR(3) NULL COMMENT '报价方向：\nOUT： 卖出\nIN： 买入\nUDF：不限，用于全国直贴与全国转贴',
  `bill_type` VARCHAR(3) NULL COMMENT '票据类型\nBKB： 银行票据 Bank Bill\nCMB：商业票据 Company Bill',
  `bill_medium` VARCHAR(3) NULL COMMENT '票据介质：\nPAP：纸票\nELE：电票',
  `memo` VARCHAR(500) NULL COMMENT '备注，最多250汉字',
  `quote_company_id` VARCHAR(32) NULL COMMENT '报价机构ID，该ID为IAM系统中机构ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。 ',
  `contact_id` VARCHAR(32) NULL COMMENT '联系人ID，为IAM系统中用户ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。',
  `operator_id` VARCHAR(32) NULL COMMENT '操作人ID，为实际建立和维护报价单的IAM用户ID，一般情况下与quoter_id一致。',
  `create_datetime` DATETIME NULL COMMENT '报价单建立日期',
  `last_update_datetime` DATETIME NULL COMMENT '最后更新日期',
  `effective_datetime` DATETIME NULL COMMENT '生效日期。用户可以今天报明天开始生效的报价。',
  `expired_datetime` DATETIME NULL COMMENT '报价单作废日期，默认逻辑是生效日期的晚上22：00',
  `quote_status` VARCHAR(3) NULL COMMENT '报价单状态\nDFT: 草案， draft\nDSB: 已发布，distributed\nDLD：已成交，dealed\nCAL: 已撤销， canceled \nDEL: 已删除， deleted',
  `minor_flag` INT(1) NULL COMMENT '是否是小金额报价单，由用户选择。\n1. 选择小纸票、小电票时\n0. 选择普通纸票，电票，商票',
  `gg_price` DECIMAL(6,3) NULL COMMENT '国股价格',
  `cs_price` DECIMAL(6,3) NULL COMMENT '城商价格',
  `ns_price` DECIMAL(6,3) NULL COMMENT '农商价格',
  `nx_price` DECIMAL(6,3) NULL COMMENT '农信价格',
  `nh_price` DECIMAL(6,3) NULL COMMENT '农合价格',
  `cz_price` DECIMAL(6,3) NULL COMMENT '村镇价格',
  `wz_price` DECIMAL(6,3) NULL COMMENT '外资价格',
  `cw_price` DECIMAL(6,3) NULL COMMENT '财务价格',
  `ybh_price` DECIMAL(6,3) NULL COMMENT '有保函价格',
  `wbh_price` DECIMAL(6,3) NULL COMMENT '无保函价格',
  PRIMARY KEY (`id`))

COMMENT = '全国直贴票据报价主表，记录报价单主要信息\n';


-- -----------------------------------------------------
-- Table `npc_quote`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `npc_quote` ;

CREATE TABLE IF NOT EXISTS `npc_quote` (
  `id` VARCHAR(32) NOT NULL COMMENT '数据库主键字段，一个UUID字符串，唯一标记票据报价单\n',
  `direction` VARCHAR(3) NULL COMMENT '报价方向：\nOUT： 卖出\nIN： 买入\nUDF：不限，用于全国直贴与全国转贴',
  `bill_type` VARCHAR(3) NULL COMMENT '票据类型\nBKB： 银行票据 Bank Bill\nCMB：商业票据 Company Bill',
  `bill_medium` VARCHAR(3) NULL COMMENT '票据介质：\nPAP：纸票\nELE：电票',
  `memo` VARCHAR(500) NULL COMMENT '备注，最多250汉字',
  `quote_company_id` VARCHAR(32) NULL COMMENT '报价机构ID，该ID为IAM系统中机构ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。 ',
  `contact_id` VARCHAR(32) NULL COMMENT '联系人ID，为IAM系统中用户ID。若该字段为空，则使用bab_quote_addtional_info表中相应的名称字段。 ',
  `operator_id` VARCHAR(32) NULL COMMENT '操作人ID，为实际建立和维护报价单的IAM用户ID，一般情况下与quoter_id一致。',
  `create_datetime` DATETIME NULL COMMENT '报价单建立日期',
  `last_update_datetime` DATETIME NULL COMMENT '最后更新日期',
  `effective_datetime` DATETIME NULL COMMENT '生效日期。用户可以今天报明天开始生效的报价。',
  `expired_datetime` DATETIME NULL COMMENT '报价单作废日期，默认逻辑是生效日期的晚上22：00',
  `quote_status` VARCHAR(3) NULL COMMENT '报价单状态\nDFT: 草案， draft\nDSB: 已发布，distributed\nDLD：已成交，dealed\nCAL: 已撤销， canceled \nDEL: 已删除， deleted',
  `minor_flag` INT(1) NULL COMMENT '是否是小金额报价单，由用户选择。\n1. 选择小纸票、小电票时\n0. 选择普通纸票，电票，商票',
  `trade_type` VARCHAR(3) NULL COMMENT '交易模式。\nBOT: Buy Out 买断\nBBK: Buy Back 回购',
  `gg_price` DECIMAL(6,3) NULL COMMENT '国股价格',
  `cs_price` DECIMAL(6,3) NULL COMMENT '城商价格',
  `ns_price` DECIMAL(6,3) NULL COMMENT '农商价格',
  `nx_price` DECIMAL(6,3) NULL COMMENT '农信价格',
  `nh_price` DECIMAL(6,3) NULL COMMENT '农合价格',
  `cz_price` DECIMAL(6,3) NULL COMMENT '村镇价格',
  `wz_price` DECIMAL(6,3) NULL COMMENT '外资价格',
  `cw_price` DECIMAL(6,3) NULL COMMENT '财务价格',
  PRIMARY KEY (`id`))

COMMENT = '全国转贴票据报价主表，记录报价单主要信息\n与其他两个报价主表内容基本一致\n';



-- -----------------------------------------------------
-- Table `bab_quote_addtional_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bab_quote_addtional_info` ;

CREATE TABLE IF NOT EXISTS `bab_quote_addtional_info` (
  `quote_id` VARCHAR(32) NOT NULL COMMENT '对应报价主表ID',
  `company_name` VARCHAR(45) NULL COMMENT '机构名称，此字段与报价单表quote_company_id字段互斥。',
  `contact_name` VARCHAR(45) NULL COMMENT '联系人名称,此字段与报价单表contact_id字段互斥。',
  `contact_telephone` VARCHAR(20) NULL COMMENT '联系人电话',
  `accpeting_house_name` VARCHAR(45) NULL COMMENT '承兑方名称，此字段与报价表accpeting_house_id字段互斥',
  PRIMARY KEY (`quote_id`))

COMMENT = '某些用户拥有收集市场信息并报价的功能，此时报价单所属机构及联系人可能为非申浦注册用户，因此使用该表按名称记录对应机构，联系人及联系方式';


-- -----------------------------------------------------
-- Table `bab_joining_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bab_joining_users` ;

CREATE TABLE IF NOT EXISTS `bab_joining_users` (
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户ID，IAM系统用户ID',
  `joining_user_id` VARCHAR(32) NOT NULL COMMENT '代报价用户ID，IAM系统用户ID\n',
  `joining_display_mode` VARCHAR(3) NULL COMMENT '代报价显示模式：\nCTR: CONTACTER 报价单显示代报价人联系方式\nOPT: OPERATOR 报价单显示操作人联系方式',
  PRIMARY KEY (`user_id`))
;
