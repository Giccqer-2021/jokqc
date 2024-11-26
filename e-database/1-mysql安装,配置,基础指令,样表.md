# mysql安装,配置,基础指令,样表

## windows版本安装

在 [Mysql下载地址官网](https://dev.mysql.com/downloads/installer/) 中下载对应的exe文件,在安装过程中最好同时选择装入 MySQL Shell 和 MySQLWorkbench ,安装设置的 root 密码不要设置得太简单且不要忘记,然后在cmd中输入以下指令进行第一次登陆

```sh
mysql -u root -p
//输入密码
```

该指令的参数还有(可省略): -P 连接主机的端口号,-h 主机地址, 127.0.0.1 表示本机
输入密码后登录,输入以下指令查看其版本:

```sql
select version();
```

退出则输入 quit 或 exit

mysql默认为开机自启,可以输入以下cmd命令手动开启关闭:

```cmd
net start mysql
net stop mysql
```

MySQLWorkbench 汉化方法:使用 素材中的 [main_menu.xml](material\main_menu.xml) 文件替换位于 MySQL\MySQL Workbench 8.0\data 位置中的 main_menu.xml 文件(汉化前记得先备份原文件)

关于linux版安装教程详见 [这里](..\c-linux\2-linux常用软件安装.md)

## 配置

新建配置文件 my.ini ,路径为 MySQL\MySQL Server 8.0\my.ini ,按需求写入:
```ini
[mysqld]
# 默认就是3306端口
port=3306
# 设置mysql数据库的数据的存放目录,默认位于你的服务端安装目录的Data文件夹中
datadir=C:\Program Files\MySQL\MySQL Server 8.0\Data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。
max_connect_errors=10
# 服务端使用的字符集默认为utf8mb4
character-set-server=utf8mb4
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证登陆用户身份
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
```

## (可选)mysql shell与mysql workbanch

mysql shell可以在开始菜单中直接打开(位置为 mysql/mysql shell),打开后输入以下命令然后输入密码来使用 mysql shell 登录mysql(可以选择保存密码)

> \connect root@localhost

输入 \sql 来使用 sql 语言

同理,可以在桌面或者开始菜单中的相同位置打开 mysql workbanch ,二者都是开源软件

## 数据库指令

|             命令              |           用途           |
| :---------------------------: | :----------------------: |
|        show databases;        |   显示当前拥有的数据库   |
| show create database 数据库名 |   查看数据库的基本信息   |
|  create database  数据库名;   |        创建数据库        |
|    drop database 数据库名;    |        删除数据库        |
|        use  数据库名;         |        操作数据库        |
|      select database();       | 查看当前正在使用的数据库 |

## 表指令

|                      命令                       |              用途              |
| :---------------------------------------------: | :----------------------------: |
|                  show tables;                   |   查看当前数据库中的所有表名   |
|             show create table 表名;             |       查看某表的基本信息       |
|                   desc 表名;                    |       查看某表的字段信息       |
|               select * from 表名;               |      查看某表中的所有数据      |
|    create table 表名(列名 类型,列名 类型…);     |             创建表             |
|                drop table 表名;                 |             删除表             |
|         alter table 表名 add 列名 类型;         |             添加列             |
|       alter table 表名 drop column 列名;        |             删除列             |
|             show columns from 表名;             |  查看表中的所有列及其详细信息  |
|       alter table 旧表名rename to 新表名;       |         修改某表的表名         |
| alter table 表名 change 旧列名 新列名 数据类型; | 修改某表中某列的名字或数据类型 |
|     alter table 表名 modify 列名 数据类型;      |    修改某表中某列的数据类型    |
|      alter table 某表 add 新列名 数据类型;      |         为某表增加一列         |
|          alter table 某表 drop 旧列名;          |        删除某表中的一列        |

关于使用mysql workbench创建数据表时的项目含义:

PK: Primary Key 主键
NN: Not Null 非空 ,设置非空时可以设置其默认值以允许插入空数据
UQ: Unique 唯一
BIN: Binary 二进制（比text更大）
UN: Unsigned 无符号数（非负数）
ZF: Zero Fill 填充零，比如设计列类型为 int(4) ,创建表时输入字段为1， 则自动填充为0001
AI: Auto Increment 自增

一些基础的数据格式:

|  数据格式  |                             描述                             |
| :--------: | :----------------------------------------------------------: |
|    INT     |                         整型(4字节)                          |
|   BIGINT   |                         整型(8字节)                          |
|  TINYINT   |                         整型(1字节)                          |
|   FLOAT    |                            单精度                            |
|   DOUBLE   |                            双精度                            |
|  CHAR(M)   |                     定长为M字节的字符串                      |
| VARCHAR(M) | 最长为M+1字节的变长字符串,可存储M个任意字符(汉字,英文字母等) |
|    TEXT    |                     文本(最长65535字节)                      |
|    DATE    |                             日期                             |
|    TIME    |                             时间                             |
|    BLOB    |                  二进制数据(最大65535字节)                   |

对于数值类型(如INT,TINYINT)可以添加括号并声明其要显示的位数,一般配合 Zero Fill 填充零 选项使用

## 数据指令

|                命令                 |       用途       |
| :---------------------------------: | :--------------: |
| insert into 表名 values(值,值,值…); |     添加数据     |
|    delete from 表名 where 条件;     |     删除数据     |
| update 表名 set 列名=值 where 条件; |     修改数据     |
|        truncate table 表名;         | 清空表内所有数据 |

## 样表

本教程使用的数据库: knowldege_database
```sql
create database knowledge_database;
use knowledge_database;
```

本教程创建并使用以下插入数据的数据表进行教学(一个工程所使用的所有数据表的所有字段名最好都不要重复):

1. simple_user_table 与其他表无关联
    ```sql
    CREATE TABLE `knowledge_database`.`simple_user_table` (
      `user_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
      `user_name` VARCHAR(45) NOT NULL,
      `user_is_male` TINYINT(1) UNSIGNED NOT NULL,
      `user_age` TINYINT UNSIGNED NOT NULL,
      `user_phone` BIGINT UNSIGNED NOT NULL,
      `user_address` VARCHAR(45) NULL,
      `user_comment` VARCHAR(45) NULL,
      PRIMARY KEY (`user_id`),
      UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
      UNIQUE INDEX `user_name_UNIQUE` (`user_name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COMMENT = 'Simple demo table of mysql.';
    ```
    ```sql
    INSERT INTO `knowledge_database`.`simple_user_table` (`user_id`, `user_name`, `user_is_male`, `user_age`, `user_phone`, `user_address`, `user_comment`) VALUES ('1', '蔡徐坤', '1', '24', '123456789012', '蔡徐村', '它很美,看一眼就会爆炸');
    INSERT INTO `knowledge_database`.`simple_user_table` (`user_name`, `user_is_male`, `user_age`, `user_phone`, `user_address`, `user_comment`) VALUES ('乔碧萝', '0', '60', '098765432109', '碧萝村', '你们看,她就像东施一样美丽');
    INSERT INTO `knowledge_database`.`simple_user_table` (`user_name`, `user_is_male`, `user_age`, `user_phone`, `user_address`) VALUES ('卢本伟', '1', '27', '11122223333', '伞兵村');
    INSERT INTO `knowledge_database`.`simple_user_table` (`user_name`, `user_is_male`, `user_age`, `user_phone`) VALUES ('波澜哥', '1', '33', '44455556666');
    ```
    
2. employee_table
    ```sql
    CREATE TABLE `knowledge_database`.`employee_table` (
      `employee_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
      `employee_name` VARCHAR(45) NOT NULL,
      `employee_department_id` TINYINT UNSIGNED NULL,
      PRIMARY KEY (`employee_id`),
      UNIQUE INDEX `employee_id_UNIQUE` (`employee_id` ASC) VISIBLE,
      UNIQUE INDEX `employee_name_UNIQUE` (`employee_name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
    ```

    department_table
    ```sql
    CREATE TABLE `knowledge_database`.`department_table` (
      `department_id` TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
      `department_name` VARCHAR(45) NOT NULL,
      PRIMARY KEY (`department_id`),
      UNIQUE INDEX `department_id_UNIQUE` (`department_id` ASC) VISIBLE,
      UNIQUE INDEX `department_name_UNIQUE` (`department_name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
    ```

    ```sql
    INSERT INTO `knowledge_database`.`department_table` (`department_id`, `department_name`) VALUES ('1', '控制部');
    INSERT INTO `knowledge_database`.`department_table` (`department_name`) VALUES ('情报部');
    INSERT INTO `knowledge_database`.`department_table` (`department_name`) VALUES ('培训部');
    INSERT INTO `knowledge_database`.`department_table` (`department_name`) VALUES ('安保部');
    INSERT INTO `knowledge_database`.`department_table` (`department_name`) VALUES ('中央本部');
    INSERT INTO `knowledge_database`.`department_table` (`department_name`) VALUES ('惩戒部');
    ```
    
    添加外键关联:
    
    ```sql
    ALTER TABLE `knowledge_database`.`employee_table` 
    ADD INDEX `employee_department_idx` (`employee_department_id` ASC) VISIBLE;
    ;
    ALTER TABLE `knowledge_database`.`employee_table` 
    ADD CONSTRAINT `employee_department`
      FOREIGN KEY (`employee_department_id`)
      REFERENCES `knowledge_database`.`department_table` (`department_id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION;
    ```
    
    为 employee_table 添加数据:
    ```sql
    INSERT INTO `knowledge_database`.`employee_table` (`employee_id`, `employee_name`, `employee_department_id`) VALUES ('1', '孙悟空', '1');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('猪八戒', '1');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('左冷禅', '2');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('令狐冲', '2');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('岳不群', '2');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('漆黑噤默', '3');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('苍蓝残响', '3');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('堇紫泪滴', '3');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('殷红迷雾', '3');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`, `employee_department_id`) VALUES ('威尔逊', '4');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`) VALUES ('超天酱');
    INSERT INTO `knowledge_database`.`employee_table` (`employee_name`) VALUES ('阿P');
    ```
    
3. dormitory_table ,用于 mybatis 逆向工程测试
    ```sql
    CREATE TABLE `knowledge_database`.`dormitory_table` (
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
    
    INSERT INTO `knowledge_database`.`dormitory_table` (`student_id`, `student_name`, `is_male`, `student_phone`, `notes`) VALUES ('1', '王梅梅', '0', '12111112333', '她很美');
    INSERT INTO `knowledge_database`.`dormitory_table` (`student_name`, `is_male`, `student_phone`, `notes`) VALUES ('二狗子', '1', '45344541232', '他又名李狗蛋');
    INSERT INTO `knowledge_database`.`dormitory_table` (`student_name`, `is_male`, `student_phone`) VALUES ('漆黑噤默', '1', '32456789012');
    INSERT INTO `knowledge_database`.`dormitory_table` (`student_name`, `is_male`) VALUES ('蔡徐美', '0');
    ```
    
4. time_table ,用于雪花算法测试和时间格式类型数据的操作测试
    ```sql
    CREATE TABLE `knowledge_database`.`time_table` (
      `id` BIGINT UNSIGNED NOT NULL,
      `description` VARCHAR(45) NOT NULL,
      `date` DATE NULL,
      `time` TIME NULL,
      `date_time` DATETIME NULL,
      `time_stamp` TIMESTAMP NULL,
      `year` YEAR NULL,
      PRIMARY KEY (`id`),
      UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COMMENT = 'SnowFlake and date.';
    
    INSERT INTO `knowledge_database`.`time_table` (`id`, `description`, `date`, `time`, `date_time`, `time_stamp`, `year`) VALUES ('1382593728514', 'timeStamp', FROM_UNIXTIME(1726803514), FROM_UNIXTIME(1726803514), FROM_UNIXTIME(1726803514), FROM_UNIXTIME(1726803514), FROM_UNIXTIME(1726803514));
    INSERT INTO `knowledge_database`.`time_table` (`id`, `description`, `date`, `time`, `date_time`, `time_stamp`, `year`) VALUES ('1382593728515', 'timeText','2024-9-20','14:18:00','2024-9-20 14:18:00','2024-9-20 14:18:00','2024');
    INSERT INTO `knowledge_database`.`time_table` (`id`, `description`, `date`, `time`, `date_time`, `time_stamp`, `year`) VALUES ('1382593728516', 'timeFunction',CURDATE(),CURTIME(),NOW(),CURRENT_TIMESTAMP,YEAR(NOW()));
    ```
    
    



