CREATE TABLE `knowledge_data`.`company` (
  `employee_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `employee_name` VARCHAR(20) NOT NULL,
  `is_male` TINYINT(1) UNSIGNED NOT NULL,
  `employee_phone` BIGINT UNSIGNED NULL,
  `notes` VARCHAR(60) NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE INDEX `employee_id_UNIQUE` (`employee_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'The demo company table';
