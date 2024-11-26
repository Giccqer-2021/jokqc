# vue项目安装配置构建,cnpm

*本说明使用工程 [vue前端知识(默认前端工程)](material\vscode-vue-front-knowledge.bat) 

## node.js

1. 从 [官网下载](https://nodejs.org/en/download) 安装文件并完成安装后,进入 C:\Program Files\nodejs ,创建文件夹 node_global , node_cache ,打开cmd,查看版本号并配置npm包管理器的全局安装包文件夹位置和缓存文件夹位置(C盘空间不够可以放在D盘):

   ```sh
   node -v
   npm config set prefix "C:\Program Files\nodejs\node_global"
   npm config set cache "C:\Program Files\nodejs\node_cache"
   npm config get prefix
   npm config get cache
   ```

   可以使用 node -h 查看node命令指南

2. 配置相应的环境变量

   > NODE_HOME=C:\Program Files\nodejs
   > (path)%NODE_HOME%
   > (path)%NODE_HOME%\node_global

## [npm](https://www.npmjs.com/)与[vite](https://cn.vitejs.dev/)

1. 安装node.js时会自动下载安装 npm,使用cmd输入以下命令:

   > 查看版本 查看下载源 更换下载源(淘宝镜像源)

   ```sh
   npm -v
   npm config get registry
   npm config set registry https://registry.npmmirror.com/
   ```

   注:默认镜像源地址为 https://www.npmjs.com/ ,覆盖原地址后不要忘记
   使用该命令下载 vite 构建工具(快速构建前端Vue项目的脚手架)

   ```sh
   npm install vite -g
   ```

2. npm常用命令:

   |         命令         |                  说明                   |                           附加参数                           |
   | :------------------: | :-------------------------------------: | :----------------------------------------------------------: |
   |       npm init       |   初始化当前目录,生成package.json文件   |                                                              |
   |     npm i 模块名     |            安装某模块(依赖)             | -g 安装到全局目录<br />-S 将安装包信息加入到 dependencies 生产依赖<br />-D 将安装包信息加入到 devDependencies 开发依赖 |
   |        npm i         |    安装package.json中声明的所有依赖     |                                                              |
   | npm uninstall 模块名 | 卸载模块,默认不移除package.json中的信息 | -g 卸载全局模块<br />-S 同时移除 dependencies 下的信息<br />-D 同时移除 devDependencies 下的信息 |
   |     npm outdated     | 检查本项目所使用的模块版本是否可以更新  |                                                              |
   |  npm update 模块名   |         将某模块更新至最新版本          | 模块名@版本号 更新模块到指定版本<br />模块名@latest  更新到最新的版本 |
   |        npm -v        |               查看版本号                |                                                              |
   |       npm root       |          查看项目模块所在目录           |                       -g 查看全局模块                        |
   |    npm ls 模块名     |    查看依赖某模块且已安装的模块列表     |                                                              |
   |        npm ls        |       查看本项目已安装的模块列表        |                                                              |
   |   npm view 模块名    |          查看某模块的详细信息           | dependencies 仅查看模块依赖关系<br />version 仅查看最新版本号<br />versions 仅查看历史版本号<br />repository.url 仅查看安装来源 |
   |   npm info 模块名    |        查看某模块的详细信息,同上        |                                                              |
   |      npm prune       |         清除项目中未被使用的包          |                                                              |
   |   npm repo 模块名    |  打开浏览器跳转到github中该模块的仓库   |                                                              |
   
   更新npm可使用指令: npm install -g npm@latest

## 创建并启动vue项目:

1. 输入cmd指令(以下三者之一):

   ```sh
   npm create vite@latest
   ```

   ```sh
   npm create vue@latest
   ```

   ```sh
   vue ui
   ```

   创建项目文件夹后进入,输入以下命令安装其必备的依赖模块(初始化vue工程):

   ```sh
   npm i
   ```

2. 输入以下指令运行 vue 项目:

   ```sh
   npm run dev
   ```
   
   访问 http://localhost:5173/ 测试前端运行,在cmd窗口输入 h+回车 获得帮助,输入 q+回车 结束前端运行

## vscode

1. vscode为行业内处理前端工程的通用编辑器,从 [安装包下载地址 ](https://code.visualstudio.com/)下载完毕后,建议同时安装以下插件:
   通用:

   > xml标签同步修改: Auto Rename Tag
   > 使用浏览器运行html页面: open in browser
   > 自动添加闭合标签: Auto Close Tag
   > css样式表自动补全: HTML CSS Support
   > ES6语法提示: JavaScript (ES6) code snippets
   > 路径提示与补全: Path Intellisense
   > 补全代码: Mithril Emmet
   > 前端代码高亮和格式化: Prettier - Code formatter
   > 英语拼写检查: Code Spell Checker
   > 自定义静态代码分析与格式化工具: ESLint

   vue相关:

   > vue语法高亮和格式化: Vue 3 Snippets
   > vue代码提示补全: VueHelper
   > Element-Plus代码自动生成: Element-Plus Snippets

   扩展包:

   > 包含多个插件,vue全家桶: Vue Extension Box

2. 右击项目文件夹,选择"通过Code打开"创建的vue项目

3. 在窗口中选择 [package.json](material\vue-front-knowledge\package.json) ,可直接进行前端运行测试

4. 按 Ctrl+` 创建新的cmd终端

## cnpm

???
