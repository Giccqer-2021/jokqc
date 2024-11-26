# gradle安装配置,命令,groovy

## gradle安装配置 

1. [下载gradle压缩包](https://gradle.org/install/),解压,配置环境变量:
   
   >[GRADLE_HOME](D:\gradle\gradle-8.9)=Gradle文件夹地址
   >
   >GRADLE_USER_HOME=gradle仓库地址
   >指定本地Maven仓库时用：M2_HOME=maven仓库地址
   
   写入Path
   
   > %GRADLE_HOME%\bin
   >
   
   然后在cmd中输入以下指令检查版本
   
   ```sh
   gradle -version
   ```
   
4. 配置更换下载源为[阿里云仓库](https://developer.aliyun.com/mvn/guide): 进入 gradle-版本号\init.d 文件夹中,创建 init.gradle 文件(或从 [素材文件夹](material) 中复制)并写入:

   ```groovy
   allprojects {
       repositories {
           //mavenLocal() //表示优先使用maven本地仓库,按情况配置
       	maven { name "Alibaba" ; url "https://maven.aliyun.com/repository/public" 		}
       	maven { allowInsecureProtocol=true //允许使用未经安全协议认证的仓库,下方也要加
                   name "Bstek" ; url "http://nexus.bsdn.org/content/groups/public/" 		}
           mavenCentral()
   }
    
       buildscript { 
           repositories { 
               maven { name "Alibaba" ; url 'https://maven.aliyun.com/repository/public' }
               maven { allowInsecureProtocol=true
                   name "Bstek" ; url 'http://nexus.bsdn.org/content/groups/public/' }
               maven { name "M2" ; url 'https://plugins.gradle.org/m2/' }
           }
       }
   }
   ```

## gradle命令

???

## Groovy(非必须安装)

gradle本身内置了groovy语言库文件因此无需安装,但为了能够在java中书写groovy代码并学习gradle语法可以考虑安装.先从网站[下载](https://groovy.apache.org/download.html)对应的压缩包,解压并配置环境变量:

> GROOVY_HOME=groovy位置
> 写入Path: %GROOVY_HOME%\bin

在cmd中运行以下指令检查版本:

```sh
groovy -v
```

## Groovy语法简要说明

### 基础

1. 默认修饰符public，若类文件中不声明类定义则视为脚本
2. def声明方法变量，以最后一句结果作为返回值。
3. 语句结尾可不加分号
4. 若不引起歧义,括号()大括号{}皆可省略

### 对象赋值取值

|             赋值方法              |     取值方法     |
| :-------------------------------: | :--------------: |
|          对象.属性名=xxx          |   对象.属性名    |
|         对象的setter方法          | 对象的getter方法 |
|        对象["属性名"]=xxx         |  对象["属性名"]  |
| new 对象(属性名1:xxx,属性名2:xxx) |                  |

### 字符串

|     引号     |               作用                |
| :----------: | :-------------------------------: |
|   ' 内容 '   |             纯字符串              |
|   " 内容 "   |    可引用变量${}和运算的字符串    |
| ''' 内容 ''' | 可引用变量${}可运算可换行的字符串 |

### 闭包

1. 基本构造{ 参数1,参数2 -> 方法体}
   无参可省略参数，若省略箭头则默认包含隐藏参数 it
2. 闭包是 groovy.lang.Closure 类的实例
3. 声明方法时若需要调用闭包一般将其放在最后参数位置
4. 如上，则调用方法时可将闭包写在调用方法之外，可省略无用的()和{}
