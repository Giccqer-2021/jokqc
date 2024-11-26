# vue项目说明及vue语法

## vue项目文件结构

![vue架构结构](material\vue架构结构.png)

|       文件        |          说明          |                  备注                  |
| :---------------: | :--------------------: | :------------------------------------: |
|   node_modules    |     项目的 js 依赖     |           执行 npm i 后生成            |
|      public       | 公共静态文件,图片链接  |       由src目录文件外的文件使用        |
|      **src**      |       **源代码**       |                                        |
|    index.html     |   项目首页,访问入口    |                                        |
| package-lock.json | 对 js 依赖进行版本锁定 |       执行 npm i 后生成,无需修改       |
|   package.json    |   项目的 js 依赖管理   |                无需修改                |
|  vite.config.js   |    vite 脚手架配置     | [详细说明](https://vitejs.dev/config/) |

可以删除 README.md 文件并清空 src/components , src/asserts 文件下的文件,将 style.css 中的内容清空,清理 [App.vue](material\vue-front-knowledge\src\App.vue) 文件内容为:
```vue
<template>
</template>
```

以此来初始化项目,若需要创建 vue 模块可以复制 [素材](material\ComponentDemo.vue) 

## 单文件说明

1. 对于 [package.json](material\vue-front-knowledge\package.json) , dependencies 节点下为工程依赖, devDependencies 节点下为运行时依赖

2. 对于 [vite.config.js](material\vue-front-knowledge\package.json) 下方输入以下内容配置端口
   ```javascript
   export default defineConfig({
     plugins: [vue()],
     server:{
       //host:'0.0.0.0' ,//ip地址
       port: 80, // 设置服务启动端口号,浏览器默认端口号就是80
       open: true, // 设置服务启动时是否自动打开浏览器
     }
   })
   ```
   
3. 对于 [src/main.js](material\vue-front-knowledge\src\main.js) ,在这个文件可以声明并导入相关的依赖模块,该依赖在全局生效
   ```javascript
   import { createApp } from 'vue'//从vue框架中导入createApp函数
   import './style.css'//导入同目录下的css样式
   import App from './App.vue'//从 App.vue 页面导入App组件
   //使用createApp函数和App组件创建一个vue应用并挂载到index.html页面中id=app的标签下
   createApp(App).mount('#app')
   ```
   
4. 在 [index.html](material\vue-front-knowledge\index.html) 文件中可以修改标题和图标:
   ```html
   <html lang="en">
     <head>
       <meta charset="UTF-8" />
       <!-- 修改 href 属性以修改图标,图标位于public文件夹下,支持ico,png,svg等格式文件 -->
       <link rel="icon" type="image/svg+xml" href="/flower_bud.ico" />
       <meta name="viewport" content="width=device-width, initial-scale=1.0" />
       <title>Vue教学</title><!-- 此处修改标题 -->
     </head>
     <body>
       <div id="app"></div>
       <script type="module" src="/src/main.js"></script>
     </body>
   </html>
   ```

5. 一个 .vue模块 需要包含:

   |           标签名            |       用途       |               备注                |
   | :-------------------------: | :--------------: | :-------------------------------: |
   | <script setup></script>标签 | 写JavaScript代码 | 可以没有,必须写上setup否则不执行  |
   |  <template></template>标签  |    写html标签    |           **必须要有**            |
   | <style scoped></style>标签  |    写css样式     | 可以没有,scoped表示在当前页面生效 |

   顺序随机

## vue语法说明

1. [前端语法参考网站](https://www.w3school.com.cn/) 

2. 变量,常量或方法的修饰符

   | 修饰符  |           作用范围            |                备注                |
   | :-----: | :---------------------------: | :--------------------------------: |
   |   var   |        可跨大括号使用         | 不推荐使用,易造成混乱,可以重复声明 |
   | **let** |     仅在当前大括号内使用      |     **推荐使用**,不可重复声明      |
   |  const  | 常量,若为对象则不可改变其地址 | 推荐用于定义回调函数,不可重复声明  |

3. 关于三等号 : === 表示两边的数值的值与类型全部相同时输出 true

4. 关于 ref() 函数:使用时需在 script 标签中导入

   ```javascript
   import { ref } from "vue";
   ```

   在定义变量或回调函数时使用,如:

   ```javascript
   let num = ref(0)  //定义一个整数
   let arr = ref([]) //定义一个数组
   let obj = ref({}) //定义一个对象
   ```

   当 template 中与该变量绑定的标签其邦定值发生变化时,这些值也会发生改变,在html标签尖括号外用 {{}} 进行数值双向绑定:

   ```html
    <div>{{ '数字:' + num }}</div>
   ```

   若与标签属性值绑定则在属性值前加 v-bind: 或 : ,如:

   ```html
   <img :src="image_path" />
   ```

   ref() 原理为创建一个 RefImpl 代理对象,在 js 代码中使用其值则需要加 .value,如某按钮事件中使用的num值:

   ```javascript
   const add_num = () => {
     num.value++ //这里要用 num.value 不能直接用 num
   }
   ```

   关于具体的使用样例详见 [VueBindingTest.vue](material\vue-front-knowledge\src\components\VueBindingTest.vue)

5. 关于 v-model 双向绑定指令说明:该指令几乎只用于输入框文本域等数据变化视图中,相当于对该组件同时绑定了其 :value 属性和 @input 事件以实现输入框文本和变量值的实时同步更新,在  [VueBindingTest.vue](material\vue-front-knowledge\src\components\VueBindingTest.vue) 中的演示(需先在 script 中用ref定义 inputMessage 变量字段):
   ```html
   <div>下方输入框中的文字: {{ inputMessage }}</div>
   <p>会在输入时实时更新其他被绑定内容的对话框 &nbsp;<input v-model="inputMessage"/></p>
   ```

   更新输入框中的数据也会使得div中的文本发生变化,对于 v-model 属性有其不同的变种:

   > v-model.lazy : 在对话框内容更改完毕后才会更新与之绑定的数据
   > v-model.trim : 将用户输入的前后的空格去掉

6. [vue其他指令说明](https://cn.vuejs.org/api/built-in-directives)(皆用于html标签中的属性,详见 [VueInstructionTest.vue](material\vue-front-knowledge\src\components\VueInstructionTest.vue) )

   |           指令           |                  用法                  |                             备注                             |
   | :----------------------: | :------------------------------------: | :----------------------------------------------------------: |
   |   v-bind:属性="变量名"   |            将属性与变量绑定            |                       相当于 :标签属性                       |
   |    v-on:事件="方法名"    |         将待监听事件与方法绑定         |                         相当于 @事件                         |
   |     v-model="变量名"     |    将变量与数据变化视图中的内容绑定    |                         使用方法见上                         |
   |     v-text="变量名"      |      使用变量的值替换标签中的文本      |                                                              |
   |     v-html="变量名"      | 将变量内容所反应的html标签插入该标签中 | 该变量的值能成功映射对应的html标签<br />有安全隐患,不建议使用 |
   |   v-show="布尔变量名"    |  为false时会隐藏对应的标签(不是销毁)   |                                                              |
   |    v-if="布尔变量名"     | 为true时加载该标签,为false时销毁该标签 |                                                              |
   |  v-else-if="布尔变量名"  |          需要配合v-if标签使用          |                                                              |
   |   v-else="布尔变量名"    |          需要配合v-if标签使用          |                                                              |
   | v-for="元素名 in 数组名" | 相当于js的foreach语句,需配合in或of使用 | 也可以是 "(元素名,索引值) in 数组名"<br />不推荐与 v-if 属性同时使用 |

   注意:一般使用双引号 " " 用来为 <template> 标签的属性赋值,对于 vue 专有属性其值表示的是 script 字段,方法等(通常需要在 <script> 中定义),如果要在其中直接使用字符串则需要额外在其中使用单引号 ' ' 来声明.对于 <script> 标签中的字符串一般使用单引号 ' ' 引用字符串
