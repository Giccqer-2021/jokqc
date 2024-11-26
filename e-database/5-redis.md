# redis

## windows版安装

1. 下载 [msi安装包](https://github.com/MicrosoftArchive/redis/releases) 安装redis,设定:加入环境变量到Path,安装时设定最大内存分配为 512MB

2. 配置文件为  [redis.windows-service.conf](C:\Program Files\Redis\redis.windows-service.conf) ,在 requirepass foobared 后添加:

   > requirepass 123456@abcdef

   可将密码改为 123456@abcdef ,如果要修改端口则添加(默认6379)

   > port 6379

   如要修改最大内存则修改

   > maxmemory 512mb

3. 在桌面中右键此电脑->管理->服务,找到redis重启服务器以使密码生效

4. cmd 访问客户端,输入密码并进行ping
   ```sh
   redis-cli
   auth 123456@abcdef
   ping
   ```

   若返回 PONG 则表示连接成功,输入 quit 退出客户端(或按Ctrl + C 退出)
   
5. 对于linux版的安装详见 [这里](..\c-linux\2-linux常用软件安装.md)

## redis操作命令

可以访问 [教程网站](http://doc.redisfans.com/) 获取更详细的命令教程,可以在 idea 或 vscode(推荐) 中安装相关插件登陆客户端

redis 可操作的五大数据类型为:

> String 字符串
> List 列表
> Set 集合 (元素唯一不重复)
> Hash 表
> zSet 有序集合

### 登陆

1. 若在windows中则启动 cmd,未设置密码则无需使用 auth 命令

   ```sh
   redis-cli
   auth 123456@abcdef
   ping
   ```

2. 切换数据库(0-15号数据库),redis默认有16个数据库,默认0号数据库,可在配置文件中的 databases 字段进行配置

   ```cmd
   select 1
   ```
   
   输入 clear 清屏

### 通用单数据及String操作命令

若可以,redis会自动将 String 字符串处理为对应的 整数,浮点数

   |        命令        |              用途              |         备注         |
   | :----------------: | :----------------------------: | :------------------: |
   |     set 键 值      |         添加一对键,值          |    不建议输入中文    |
   |       get 键       |        获取某键对应的值        |                      |
   |     EXISTS 键      |        判断该键是否存在        |      返回1存在       |
   | move 键 数据库序号 |         删除某库中的键         |      返回1成功       |
   |       ttl 键       |       查看某键的生存周期       |    返回 -2 已过期    |
   |      type 键       |      查看某键对应值的类型      |                      |
   |      incr 键       |          使对应的值+1          | 值必须为数字型字符串 |
   |      decr 键       |          使对应的值-1          | 值必须为数字型字符串 |
   |  setex 键 时间 值  |   添加一对存在一定时间的键值   |       单位:秒        |
   |   expire 键 时间   | 为一对已存在的值设置其存在时间 |       单位:秒        |
   |       clear        |              清屏              |                      |

### 通用全局数据库操作命令

   |   命令   |          用途           |
   | :------: | :---------------------: |
   |  dbsize  | 查看当前数据库key的数量 |
   | keys \*  | 查看当前数据库所有的key |
   | flushdb  |   清空当前数据库数据    |
   | flushall |   清空所有数据库数据    |
