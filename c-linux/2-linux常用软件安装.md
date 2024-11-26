# linux常用软件安装

## 将软件下载源地址替换为阿里云镜像

请在Linux系统中依次输入以下命令:

```sh
cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak
curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
cat /etc/yum.repos.d/CentOS-Base.repo
yum clean all
yum makecache
```

## java

### 卸载旧版本java(如果有的话)

```sh
rpm -qa | grep -i java
rpm -qa | grep -i java | xargs -n1 rpm -e --nodeps
```

### 自动安装(仅对java11或更早版本生效)

```sh
yum -y list java*
yum -y install java-11-openjdk.x86_64
java -version
```

一般情况下,java的安装位置为：/usr/lib/jvm

### 手动安装(以java17为例)

1. 从 java-17 [官网下载地址](https://www.oracle.com/java/technologies/downloads/?er=221886#java17) 下载压缩包,下载版本为 x64 Compressed Archive .创建并进入 /usr/local/java .将解压包移入该目录并解压(解压后可删除压缩包):

   ```sh
   tar zxvf jdk-17_linux-x64_bin.tar.gz
   rm -rf jdk-17_linux-x64_bin.tar.gz
   ```

   修改 /etc/profile 文件

   ```sh
   vim /etc/profile
   ```

   插入以下环境变量:

   ```sh
   export JAVA_HOME=/usr/local/java/jdk-17.0.12
   export CLASSPATH=.:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar
   export PATH=$JAVA_HOME/bin:$PATH
   ```

   输入指令,重加载环境变量并检查java版本:

   ```sh
   cat /etc/profile
   source /etc/profile
   java -version
   ```


## mysql

### 卸载mariadb

依次输入以下指令:

```sh
rpm -qa | grep mariadb
rpm -e --nodeps mariadb-libs-5.5.52-1.el7.x86_64
```

### 下载mysql

在 [下载官网](https://dev.mysql.com/downloads/mysql/) 下载指定的 rpm 压缩包,将其置入linux自定义的位置(这里为  /usr/local/mysql_package )

<img src="material\mysql下载配置.png" alt="mysql下载配置" style="zoom: 50%;" />

(可选)或输入以下指令,将该压缩包直接下载至 /usr/local/mysql_package 中:

```sh
mkdir /usr/local/mysql_package
cd /usr/local/mysql_package
wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.39-1.el7.x86_64.rpm-bundle.tar
```

然后解压缩
```sh
tar -xvf mysql-8.0.39-1.el7.x86_64.rpm-bundle.tar -C /usr/local/mysql_package
```

然后依次执行安装命令:
```sh
rpm -ivh mysql-community-common-8.0.39-1.el7.x86_64.rpm 
rpm -ivh mysql-community-client-plugins-8.0.39-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-8.0.39-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-8.0.39-1.el7.x86_64.rpm
rpm -ivh mysql-community-icu-data-files-8.0.39-1.el7.x86_64.rpm
yum install openssl-devel -y
rpm -ivh mysql-community-devel-8.0.39-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-8.0.39-1.el7.x86_64.rpm
```

(可选)最后删除压缩包:
```sh
rm -rf /usr/local/mysql_package/
```

### 初始化mysql

以下语句的作用分别为,查看随机生成的密码,登陆,重置密码,查看当前所有的数据库名

```sh
cat /var/log/mysqld.log | grep 'password'
mysql -u root -p
//输入密码
```

```sql
alter user 'root'@'localhost' identified by '新密码';
show databases;
```

以下为日常维护时常用的 mysql 命令

| 用途                 | 命令                                 |
| -------------------- | ------------------------------------ |
| 查看服务器状态       | systemctl status mysqld              |
| 开启/关闭/重启服务器 | systemctl start/stop/restart  mysqld |
| 设置/取消开机自启    | systemctl enable/disable  mysqld     |

### 构建远程连接

#### 开放端口指令

mysql默认端口为 3306

```sh
firewall-cmd --zone=public --add-port=3306/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-port
```

#### 权限设置指令

先登陆:
```sh
mysql -u root -p
//输入密码
```

开放根用户权限(注:一般情况下不会将根用户权限开放至远程端口,如果可以请自行创建其他用户并开放相应权限):

```sql
use mysql;
update user set host='%' where user='root';
flush privileges;
select host, user, plugin from user;
```

创建一个名为 test_database 的数据库

```sql
create database test_database;
```

在Windows打开 MySQLWorkbench.exe ,根据在linux系统中使用命令 ifconfig 得到的ip地址连接连接3306端口,连接的数据库名为test_database ,用户名为root并输入正确的密码后进行连接

## [tomcat](https://tomcat.apache.org/)

### 下载压缩包并解压

```sh
cd /usr/local
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.28/bin/apache-tomcat-10.1.28.tar.gz --no-check-certificate
tar -zxvf apache-tomcat-10.1.28.tar.gz 
mv apache-tomcat-10.1.28 tomcat10
rm -rf apache-tomcat-10.1.28.tar.gz
```

### 开放端口8080

```sh
firewall-cmd --zone=public --add-port=8080/tcp --permanent
firewall-cmd --reload
```

### 运行测试

进入tomcat程序文件夹

```sh
cd /usr/local/tomcat10/bin
```

启动

```sh
./startup.sh
```

关闭

```sh
./shutdown.sh
```

## redis

### 下载,安装,配置

1. [下载压缩包](https://download.redis.io/releases/),这里以 redis-7.4.0.tar.gz 为例,使用 ftp 将压缩包置入 /opt 目录下解压后删除安装包:

   ```sh
   cd /opt
   tar -zxvf redis-7.4.0.tar.gz
   rm -rf redis-7.4.0.tar.gz
   ```

2. 安装 gcc 及其相关组件(要先配置阿里云镜像),然后查看其版本

   ```sh
   yum install gcc-c++ -y
   (可选) yum install centos-release-scl -y
   gcc -v
   ```

3. 分别执行命令安装:

   ```sh
   cd /opt/redis-7.4.0
   make
   make install
   ```

   配置文件 /etc/sysctl.conf 

   ```sh
   vim /etc/sysctl.conf
   ```

   加入以下内容,允许其内存被过度提交:

   > vm.overcommit_memory=1

   然后重启

   ```sh
   reboot
   ```

4. 修改配置文件:

   ```sh
   cd /opt/redis-7.4.0
   vim redis.conf
   ```

   修改内容,增加以下语句允许其在后台启动

   > daemonize yes

   修改内容允许其被跨ip访问(原为 bind 127.0.0.1 -::1)

   > bind 0,0,0,0

   添加内容(密码,端口号,最大内存):

   >requirepass 123456@abcdef
   >
   >port:6379
   >
   >maxmemory 512mb

5. 启动服务端(可以输入 ps -ef|grep redis 来检测服务端是否成功启动)

   ```sh
   redis-server /opt/redis-7.4.0/redis.conf 
   ```

   启动客户端(如果端口号为默认则不用加 **-p** 后内容,可以添加 **-h** 来指定ip)

   ```sh
   redis-cli -p 6379
   ```

   输入

   ```sh
   auth 123456@abcdef
   ping
   ```

   返回 PONG 则表示连接成功,可以在客户端输入 shutdown 关闭服务器

### 开放外部链接

外部链接配置:开放防火墙 6379,tcp 端口

```sh
firewall-cmd --zone=public --permanent --add-port=6379/tcp
firewall-cmd --reload
firewall-cmd --list-all
```

在windows操作系统中使用cmd输入

```sh
redis-cli -h 192.168.249.128 -p 6379
```

### 设置开机自启

创建文件

```sh
vim /etc/systemd/system/redis.service
```

内容(注意配置文件路径):

```sh
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /opt/redis-7.4.0/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

然后输入以下命令设置开机自启

```sh
systemctl daemon-reload
systemctl enable redis
```

### 常用运维命令

|            命令            |       用途       |
| :------------------------: | :--------------: |
| **systemctl enable redis** | **设置开机自启** |
|   systemctl start redis    |       启动       |
|    systemctl stop redis    |       重启       |
|  systemctl restart redis   |       重启       |
|   systemctl status redis   |     查看状态     |
