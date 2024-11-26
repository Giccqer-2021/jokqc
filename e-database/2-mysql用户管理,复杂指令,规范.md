# mysql用户管理,复杂指令,规范

## mysql用户管理

### 用户查询

Mysql有一个默认的名为 mysql 的数据库:
```sql
show databases;
use mysql;
```

其中名为 user 的数据表记录了用户的详细信息
```sql
show tables;
select * from user \G;
```

其中比较重要的字段有: User 用户名,Host 该用户可从哪个主机登录,authentication_string 该用户经过 password() 函数加密后的密码.其中 User 和 Host 字段共同组成了该表的复合主键
```sql
desc user;
select User,Host,authentication_string from user;
```

### 用户的创建,改名,改密,删除

创建用户时, '登录主机' 可以用百分号 % 代替,表示该用户可以在任意主机登陆数据库('localhost '为本地).用户名和登陆主机是纯字符串时可以不写单引号 ' '(密码要写) .执行本语句时可以不设置密码
注意:刚创建的用户除了登陆外没有任何增删改查的权限

```sql
create user '用户名'@'主机名' identified by '密码';
```

使用以下指令修改用户名:
```sql
rename user '用户名'@'主机名' to '新用户名'@'新主机名';
```

修改自己的密码,密码要加引号.注意:在老版本中修改密码可能要使用 password() 函数

```sql
set password='新的密码';
```

修改其他用户的密码,要有相应的权限:

```sql
set password for '用户名'@'主机名'='新的密码';
```

为了使密码生效,输入以下指令将当前user表的用户信息提取到内存里
```sql
flush privileges;
```

删除用户,同样纯字符串可以不写单引号 ' '

```sql
drop user '用户名'@'主机名';
```

### 用户权限赋予

新创建的用户只有 USAGE (登陆)权限,查询用户权限的sql语句为
```sql
show grants for '用户名'@'主机名' \G;
```

给用户授权的sql语句,一般在root用户登录时进行操作.以下指令中的 identified by 可选,含义为:为存在的用户修改密码,若用户不存在则创建它并设置密码. with grant option：可选,表示允许用户将自己的权限授权给其它用户
```sql
grant 权限 on 库名.对象名 to '用户名'@'主机名' [identified by '密码'][with grant option];
```

多个权限可以用逗号 , 隔开,其中对于 **数据库或表本身** 的 **增删改** **权限** 为:

> CREATE,DROP,ALTER

对于 **表内数据** **增删改查** 权限为:

> INSERT,DELETE,UPDATE,SELECT

ALL (或 all privileges )表示赋予全部权限,可以使用数据库连接软件以该用户的身份登录来检测用户权限
注意:使用连接软件时,用户必须对连接的数据库或其中的表,数据等拥有至少一项权限才允许被连接.使用sql语句赋予用户查看权限:

> grant SELECT on 库名.* to '用户名'@'主机名';

对于 **库名.对象名** 对象名可以是某表名.可以用 \*.\* 代表本系统中的所有数据库的所有对象(表,视图,存储过程等)

### 用户权限回收

权限,库名对象名的语法规则与创建时的相同,注意:回收用户在某一数据库下的权限后,在该用户下一次进入该数据库时才会起作用

```sql
revoke 权限 on 库名.对象名 from '用户名'@'主机名';
```

使用以下指令使修改的权限立刻生效:
```sql
flush privileges;
```

## 聚合函数

只有SELECT子句和HAVING子句、ORDER BY子句中能够使用聚合函数,不可以在 WHERE 子句中使用它

以下函数的用法皆为: select 函数名(列名) from 表名; 其中有的列名可以用星号 * 代替

|   函数    |                             用途                             |
| :-------: | :----------------------------------------------------------: |
| count(*)  |    统计表中数据的行数或者统计指定列其值不为NULL的数据个数    |
| max(列名) | 计算指定列的最大值，如果指定列是字符串类型则使用字符串排序运算 |
| sum(列名) | 计算指定列的数值和，如果指定列类型不是数值类型则计算结果为0  |
| avg(列名) | 计算指定列的平均值，如果指定列类型不是数值类型则计算结果为0  |

以下函数的用法皆为 select 函数名(); 

|     函数      |         用途         |
| :-----------: | :------------------: |
|     NOW()     |     获取当前时间     |
|    USER()     | 获取当前登录的用户名 |
|   ABS(数值)   |       取绝对值       |
|  FLOOR(数值)  |       向下取整       |
| CEILING(数值) |       向上取整       |

## 条件查询与结果过滤

以下样例使用表 simple_user_table ,数据库名 my_database

1.  in 关键字:判断某个字段的值是否在指定集合中,如:
   ```sql
   select * from simple_user_table where user_id in (2,3);
   ```

   反向查询:
   ```sql
   select * from simple_user_table where user_id not in (2);
   ```

2. between and 判断某个字段的值是否在指定的范围之内,如:
   ```sql
   select * from simple_user_table where user_age between 25 and 40;
   ```

3. is null 判断字段的值是否为空值,如:
   ```sql
   select * from simple_user_table where user_address is not null;
   ```

4. and,or 和,或,可以连接两个或者多个查询条件

5. like 判断两个字符串是否相匹配,如(记得加引号):
   ```sql
   select * from simple_user_table where user_name like '蔡%';
   ```

   like通配符: **%** 用于匹配任意长度的字符串 , **_** 用于匹配单个字符

6. limit 执行查询数据时可能会返回很多条记录,选择其中的前几条数据
   order by 根据查询到的某列对查询字段排名, asc 表示升序排列 desc 表示降序排列,默认为升序排列

   ```sql
   select * from simple_user_table order by user_id desc limit 2;
   ```

7. group by 一般与聚合函数共同使用,根据选择中的某一列进行排序
   having 用于在数据分组后对结果进行过滤,可以使用聚合函数

   ```sql
   select count(*), user_is_male from simple_user_table group by user_is_male;
   ```

   ```sql
   select sum(user_age),user_is_male from simple_user_table group by user_is_male having sum(user_age)>50;
   ```

## 别名设置

设置表的别名,使用 as ,如:
```sql
select * from simple_user_table as user_table;
```

为字段取别名:
```sql
select user_id,user_name as 姓名 from simple_user_table;
```

## 关联查询

以下样例使用表 employee_table department_id, 二表存在外键关联

括号内的内容仅为查询条件,不会在最终结果种打印出来.本查询语句也叫子查询,若为数值条件则可将等于号=替换为 > < ! 等

```sql
select * from employee_table where employee_department_id=(select department_id from department_table where department_name='培训部');
```

若想删除存在关联关系的字段,需要先删除与该字段关联的数据:
```sql
delete from employee_table where employee_department_id=(select department_id from department_table where department_name='控制部');
delete from department_table where cname='控制部';
```

## 多表连接查询

### 交叉连接查询

返回的结果是被连接的两个表中所有数据行的笛卡儿积,在实际运用中并没有什么意义
```sql
select * from employee_table cross join department_table;
```

### 内连接查询(Inner Join)

又叫简单连接,自然连接,查询到的数据必须符合 on 后表达式的结果
```sql
select employee_table.employee_name,department_table.department_name from employee_table inner join department_table on employee_table.employee_department_id=department_table.department_id;
```

### 左外连接查询(left outer join)

显示左表的所有记录,将右表符合on条件的记录附加在左表.如果左表的某条记录在右表中不存在则对应的右表查询结果为空

```sql
select * from employee_table left outer join department_table on employee_table.employee_department_id=department_table.department_id;
```

### 右外连接查询(right outer join)

显示左表中符合on条件的记录,并显示未出现在此结果中的所有右表记录各一条(其对应的左表记录结果为空)

```sql
select * from employee_table right outer join department_table on employee_table.employee_department_id=department_table.department_id;
```

## 子查询

1. exists 关键字不产生任何数据只返回true或false,当返回值为true时外层查询才会执行.以下语句会输出 employee_table 表中所有记录
   ```sql
   select * from employee_table where exists (select * from employee_table where employee_table.employee_name='超天酱');
   ```

2. any 满足其中任意一个条件就返回一个结果作为外层查询条件.本语句会返回除了 employee_department_id 为空的所有记录
   ```sql
   select * from employee_table where employee_department_id > any (select department_id from department_table);
   ```

3. all 査询返回的结果需同时满足所有内层査询条件.本语句会输出 employee_department_id 等于 department_id 最小值的记录
   ```sql
   select * from employee_table where employee_department_id <= all (select department_id from department_table);
   ```

## 用户指令备注

1. 查询语句书写顺序: select ===> from ===> where ===> group by ===> having ===> order by ===> limit
2. 查询语句执行顺序: from ===> where ===> group by ===> having ===> select ===> order by ===> limit
3. 其他具体的sql语句教程详见 [网站教程 ](https://www.w3school.com.cn/sql/index.asp) 

## MySQL数据库设计规范

???

