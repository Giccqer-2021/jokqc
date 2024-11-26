# forge版客户端服务端搭建

## windows系统客户端搭建

1. 确定要使用的 minecraft 版本及安装对应的 java 版本:

   > minecraft 1.16.5 及更低版本需要 java 8
   >
   > minecraft 1.20.1 版本需要 java 17
   >
   > minecraft 1.20.1 以上的版本需要 java 21 且操作系统为64位
2. 将素材文件中的 [HMCL-3.5.9.exe](material\HMCL-3.5.9.exe) 启动器复制到创建客户端用的工程文件夹中,运行.登陆账户(可以离线登录),在游戏版本中选择要下载的minecraft及forge版本,选择后即可自动下载.关于该启动器的用法详见 [教程网站](https://docs.hmcl.net/) 
   可以去 [mc百科](https://www.mcmod.cn/) 网站, [modrinth](https://modrinth.com/mods) 网站或 [curseforge](https://www.curseforge.com/minecraft) 网站寻找并下载模组.对于后两者,可以下载其对应的app

## windows系统服务端搭建

1. 安装好 java 后,从 [官网](https://files.minecraftforge.net/net/minecraftforge/forge/) 下载对应版本的 forge ,将forge置入工作区文件夹中,在此文件夹中打开cmd窗口,输入命令下载并安装库文件:

   ```sh
   java -jar forge-版本号-installer.jar nogui --installServer
   ```

   由于网络问题该进程可能会中断(显示 fail 等字样),可以尝试重新输入该命令继续安装,直到最终出现 success 字样即为成功
   等待下载完毕后可删除下载的forge文件和日志

2. (针对1.16.5或更早版本)创建 run.bat 文件,写入:

   ```sh
   java -Xmx分配的内存 -jar -Dfile.encoding=UTF-8 forge-版本号.jar --nogui
   pause
   ```

   分配的内存根据实际情况而定,单位 m 或 G,如果需要显示服务器gui则将结尾的 --nogui 移除.

3. (针对1.20.1或更高版本)修改 user_jvm_args.txt 文件,结尾修改内存配置:

   ```sh
   -Xmx分配的内存
   ```

   分配的内存根据实际情况而定,单位 m 或 G
   如果不需要显示服务器的gui则修改 run.bat 文件,在命令行尾部加入 --nogui

   ```sh
   java @user_jvm_args.txt @libraries/net/minecraftforge/forge/1.20.1-47.3.7/win_args.txt --nogui %*
   pause
   ```
   
4. 运行 run.bat ,第一次运行不会正式启动服务器而是会生成 eula.txt 文件,修改:
   ```properties
   eula=true
   ```

   再次运行 run.bat 文件服务器即可运行成功,此时可输入 stop 指令停止服务器,对于客户端,输入以下 url 以进行连接:

   > 网址:25565

## Linux系统服务端搭建

1. 将 linux 系统下载源替换为阿里云镜像,并下载好 minecraft 所需要的对应版本的 java

2. 安装 screen 分屏软件:

   ```sh
   yum -y install screen
   screen -v
   ```

3. 下载 [forge](https://files.minecraftforge.net/net/minecraftforge/forge/) 创建 /home/minecraft 目录,进入并将 forge 移入,输入命令:

   ```sh
   java -jar forge-版本号-installer.jar nogui --installServer
   ```

   由于网络问题该进程可能会中断(显示 fail 等字样),可以尝试重新输入该命令继续安装,直到最终出现 success 字样即为成功
   等待下载完毕后可删除下载的forge文件和日志

   ```sh
   rm -rf forge-版本号-installer.jar
   rm -rf forge-版本号-installer.jar.log
   ```

4. (针对1.16.5或更早版本)创建 run.sh 文件,写入:

   ```sh
   java -Xmx分配的内存 -jar -Dfile.encoding=UTF-8 forge-版本号.jar --nogui
   ```

   分配的内存根据实际情况而定,单位 m 或 G

5. (针对1.20.1或更高版本)修改 run.sh 文件,在命令行尾部加入 --nogui

   ```sh
   java @user_jvm_args.txt @libraries/net/minecraftforge/forge/1.20.1-47.3.7/unix_args.txt --nogui "$@"
   ```

   修改 user_jvm_args.txt 文件,结尾加入内存配置:

   ```sh
   -Xmx分配的内存
   ```

6. 修改 run.sh 文件权限,尝试启动服务器:

   ```sh
   sudo chmod -R 777 run.sh
   ./run.sh
   ```

   结束后生成 eula.txt 文件,修改:

   ```shell
   eula=true
   ```

   开放服务器端口 tcp 25565 

   ```sh
   firewall-cmd --zone=public --add-port=25565/tcp --permanent
   firewall-cmd --reload
   firewall-cmd --zone=public --list-port
   ```

   (可选)内置语音模组的端口为 udp 24454 可同时开放

7. 使用以下命令创建 minecraft 分屏:

   ```sh
   screen -S minecraft
   ```

   在该状态下执行 ./run.sh 命令启动服务器,此时可关闭对话窗口

   重新连接窗口命令("minecraft"可省略):

   ```sh
   screen -r minecraft
   ```

   可输入 stop 指令停止服务器,对于客户端,输入以下 url 以进行连接:

   > 网址:25565

