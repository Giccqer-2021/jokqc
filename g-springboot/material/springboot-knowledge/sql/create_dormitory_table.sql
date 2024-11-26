CREATE TABLE `knowledge_data`.`dormitory` (
  `student_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `student_name` VARCHAR(20) NOT NULL,
  `is_male` TINYINT(1) UNSIGNED NOT NULL,
  `student_phone` BIGINT UNSIGNED NULL,
  `notes` VARCHAR(60) NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE INDEX `student_id_UNIQUE` (`student_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'A demo of student dormitory';
