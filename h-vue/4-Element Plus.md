# [Element Plus](https://element-plus.org/zh-CN/) 

## 开始:[页面布局](http://element-plus.org/zh-CN/component/form.html) 

1. 输入cmd指令安装该模块: npm i element-plus ,在安装基础组件的同时也会自动安装相关的 icons-vue 图标组件.显然,所有element puls组件都是标签都是以 el- 开头的,子标签往往拥有更多的横线内容

2. [main.js](material\vue-front-knowledge\src\main.js) 插入以下元素:

   ```javascript
   import ElementPlus from 'element-plus'//导入ElementPlus组件
   import 'element-plus/dist/index.css'//导入ElementPlus相关的样式
   app.use(ElementPlus)//let app=createApp(App)
   ```

3. 访问 [el-container](https://element-plus.org/zh-CN/component/container.html) 说明网站,清空 [MainView.vue](material\vue-front-knowledge\src\views\MainView.vue) 中 <template> 板块的内容并加入侧边栏容器和主要区域容器:

   ```vue
   <template>
       <el-container>
         <el-aside>
           侧边栏内容
         </el-aside>
         <el-main>
       	主要区域内容
         </el-main>
       </el-container>
   </template>
   ```

   在 <style> 板块中为该该布局添加样式:
   ```css
   .el-aside {
     background: rgb(66, 66, 66);
     height: calc(100vh);
     width: 20%;
     text-align: center;
     color: rgb(66, 66, 66);
     font-size: 28px;
   }
   .el-main {
     text-align: center;
     margin-top: 100px;
   }
   ```

4. 访问 [Menu菜单](https://element-plus.org/zh-CN/component/menu.html) 说明网站,在侧边栏 <el-aside> 标签中添加侧栏垂直菜单:
   ```vue
   <!-- default-active:页面加载时默认激活菜单的 index ,1-1表示1号菜单下的1号子菜单--> 
   <el-menu 
   default-active="1-1" 
   >
       <el-sub-menu index="1"><!-- sub-menu表示包含子节点的菜单节点,可如下指定标题 -->
           <template #title>
             <span>路由跳转测试</span>
           </template>
           <!-- el-menu-item表示不包含子节点的最终节点菜单,index表示其序号或子路由地址,@click表示绑定的vue点击事件 -->
           <el-menu-item index="1-1" @click="backToWelcomePage">跳回欢迎界面</el-menu-item>
           <el-menu-item index="1-2" @click="backToLastStep">路由回滚</el-menu-item>
       </el-sub-menu>
       <el-menu-item index="2">
         <span>第二栏</span>
       </el-menu-item>
       <el-menu-item index="3">
         <span>第三栏</span>
       </el-menu-item>
       <el-menu-item index="4">
         <span>第四栏</span>
       </el-menu-item>
   </el-menu>
   ```

   在 <style> 中为该菜单添加样式
   ```css
   .el-menu{
     background-color: rgb(200, 200, 200);
   }
   ```

   同时在 <script> 中添加 backToLastStep() 方法测试路由回滚功能:

   ```javascript
   const backToLastStep=()=>{
       router.back()//back()方法:返回上一次路由的页面,对子路由也有效
   }
   ```

5. 运行cmd指令: npm run dev 点击侧边栏相关按钮测试路由跳转

## [按钮与图标](https://element-plus.org/zh-CN/component/button.html)

1. 创建 [ButtonIconTest.vue](material\vue-front-knowledge\src\components\ButtonIconTest.vue) 子组件,在其中的 <template> 标签中插入按钮组件:

   ```html
   <div class="mb-4">
       <el-button>Default</el-button>
       <el-button type="primary">Primary</el-button>
       <el-button type="success">Success</el-button>
       <el-button type="info">Info</el-button>
       <el-button type="warning">Warning</el-button>
       <el-button type="danger">Danger</el-button>
   </div>
   ```

   打开页面查询

2. Element Plus 图标:输入 npm i @element-plus/icons-vue (此步骤可省略) ,其中 @后指的是作用域,/后指的是域中包名.在 [ButtonIconTest.vue](material\vue-front-knowledge\src\components\ButtonIconTest.vue) 的 <script> 中插入:

   ```javascript
   import {Check,Delete,Edit,Message,Search,Star} from '@element-plus/icons-vue'//引入图标组件
   ```

   然后在<template> 中插入:

   ```html
   <div>
       <el-button :icon="Search" circle />
       <el-button type="primary" :icon="Edit" circle />
       <el-button type="success" :icon="Check" circle />
       <el-button type="info" :icon="Message" circle />
       <el-button type="warning" :icon="Star" circle />
       <el-button type="danger" :icon="Delete" circle />
   </div>
   ```

   即可显示带图标的按钮,当然图标也可单独使用:

   ```html
   <el-icon><Setting /></el-icon>

## [form表单](http://element-plus.org/zh-CN/component/form.html)

1. 根据网上的说明,创建 [FormTest.vue](material\vue-front-knowledge\src\components\FormTest.vue) ,其基本骨架如下:
   ```vue
   <template>
       <el-form label-width="60px">
           <el-form-item label="账号" prop="userAccount">
               <el-input />
           </el-form-item>
           <el-form-item label="密码" prop="userPassword">
               <el-input type="password" /><!-- type="password"密码表单 -->
           </el-form-item>
           <el-form-item>
               <el-button type="primary">登 录</el-button><!-- type="primary"提交按钮 -->
           </el-form-item>
           <el-form-item>
               <el-checkbox label="记住我" size="large" /><!-- 复选框 -->
           </el-form-item>
       </el-form>
   </template>
   <style scoped>
   .el-button {
       /* 以下内容会影响本页面所有按钮组件的样式 */
       width: 100%;
       background-color: chocolate;
       font-size: large;
   }
   </style>
   ```

   测试运行页面,此时还不能输入账号密码

2. 为表单绑定对象,在 <script> 标签中写入:
   ```javascript
   import { ref } from "vue";//导入 ref 函数
   let user = ref({ //测试form表单中的账号密码与一个对象的绑定,这些数据会以json形式发送给后端
       userAccount: 'banana', //一般账号密码默认为空
       userPassword: '123456',
       rememberMe: true
   })
   ```

   对于账号的input标签(使用 v-model 属性声明绑定,注意:与 v-bind 无关,且使用时不加 .value):
   ```html
   <el-input v-model="user.userAccount"/>
   ```

   对于密码的input标签:
   ```html
   <el-input type="password" v-model="user.userPassword"/>
   ```

   对于记住我的checkbox复选框标签:
   ```html
   <el-checkbox label="记住我" size="large" v-model="user.rememberMe"/>
   ```

   然后再为登陆按钮绑定click点击事件 submitMethod() :
   ```html
   <el-button type="primary" @click="submitMethod">登 录</el-button>
   ```

   对应的点击事件写在 <script> 标签中:
   ```javascript
   const submitMethod = () => {//为提交按钮绑定一个提交事件,通常为将这些数据传递给指定url后端的方法
       alert('账号:' + user.value.userAccount + "---" + '密码:' + user.value.userPassword)
   }
   ```

   测试运行页面,修改账户密码并点击登录,观察对话框输出

3. 为表单绑定输入规则:与上方绑定对象不同,这里需要使用一套独立的绑定系统,需先将表单整体与user对象绑定(使用 :model 属性),然后指定一个与表单 :rules 规则属性绑定的对象 loginRules :

   ```html
   <el-form label-width="60px" style="" :model="user" :rules="loginRules">
   ```

   该对象定义在 <script> 标签中:

   ```javascript
   let loginRules = ref({//为表单控件的账号密码输入行制定输入规则,ref绑定一个对象
       userAccount: [  //对像名要与user中的属性,prop标签属性一致
           //required:true表明不能为空否则输出message信息,trigger:'blur'表明该事件会在输入框失去焦点时检测一次
           { required: true, message: '请输入用户名', trigger: 'blur' }
       ],
       userPassword: [
           { required: true, message: '请输入密码', trigger: 'blur' },
           { min: 6, max: 16, message: '密码长度为6-16位', trigger: 'blur' }//输入框的最大最小长度,不满足条件则输出message
       ]
   })
   ```

   对于账号与密码的 el-form-item 标签,添加prop属性值,其值与user对象内的属性名一致:
   ```html
   <el-form-item label="账号" prop="userAccount">
   ```

   ```html
   <el-form-item label="密码" prop="userPassword">
   ```

   测试运行页面,测试在非法输入的状态下产生的相关提示
   
4. 为表单创建提交规则,点击提交按钮时用户输入的数据必须合法否则不会将数据提交给后端.为 <el-form> 最外层表单标签添加 ref 属性,指向一个自创的,内部无属性的对象 formRef :
   ```vue
   <el-form label-width="60px" style="" :model="user" :rules="loginRules" ref="formRef">
   ```

   修改提交按钮的 @click 方法,为该方法传递该参数
   ```vue
   <el-button type="primary" @click="submitByRules(formRef)">登 录</el-button>
   ```

   其对应的方法为:
   ```javascript
   let formRef = ref({}) //提交规则认证结果的对象
   const submitByRules = (result)=>{ //新的表单提交方法
       if(!result) return //如果结果为空,即没有给提交方法传递参数则直接返回
       result.validate((valid) =>{
           if(valid){ //valid为true时表示表单规则验证通过
               submitMethod() //此方法定义见上,通常为提交前端数据到后端的方法
           }else{ //valid为false时表示用户存在非法输入
               alert('请输入符合条件的账号密码')
           }
       })
   }
   ```

   运行前端,将用户账号密码清空(或执行其他非法输入)然后按提交按,观察弹出对话框弹内的消息

## [消息提示框](https://element-plus.org/zh-CN/component/message.html)和[消息提示(确认)弹框](https://element-plus.org/zh-CN/component/message-box.html) 

1. 在 [messageBoxTest.vue](material\vue-front-knowledge\src\components\messageBoxTest.vue) 中添加触发消息提示框的按钮,为其绑定 open1() 事件触发提示框:
   ```vue
   <script setup>
   import { ElMessage } from 'element-plus' //使用前要导入
   const open = () => {
       ElMessage({
           showClose: true, //是否展示消息提示框的关闭按钮
           message: '消息对话框', //消息提示框显示的文字
           type: 'info' //可选择 success,warning,info,error 四种类型,默认为 info 且可省略
       })
   }
   </script>
   <template>
       <el-button :plain="true" @click="open">消息</el-button>
   </template>
   ```

   运行并测试其效果

2. 添加触发消息弹出框事件相关的按钮并为其绑定 openBox() 事件:
   ```vue
   <script setup>
   import { ElMessageBox } from 'element-plus' //使用前要导入
   const openBox = () => {
       ElMessageBox.alert('你打开了这个对话框', '标题', { //消息提示弹框上的文字和标题
           // autofocus: false, //如果想取消自动对焦功能,添加该属性
           confirmButtonText: '确认', //确认按钮上的文字
           callback: () => { //回调函数,当点击确认后要执行的函数
               ElMessage({
                   type: 'info',
                   message: '你确认了这个对话框',
               })
           },
       })
   }
   </script>
   <template>
       <el-button plain @click="openBox">点击打开消息提示对话框</el-button>
   </template>
   ```

   添加确认消息弹出框事件并为与其绑定的按钮添加  事件:
   ```vue
   <script setup>
   import { ElMessageBox } from 'element-plus' //使用前要导入
   const openConfirmBox = () => {
       ElMessageBox.confirm('你是选择确认还是取消?', '标题', //确认消息弹框上的文字和标题
           {
               confirmButtonText: '确认', //确认按钮上的文字
               cancelButtonText: '取消', //取消按钮上的文字
               type: 'warning', //消息弹框类型
           }
       )
           .then(() => { //选择确定按钮后要执行的函数
               ElMessage({
                   type: 'success',
                   message: '你选择了确认',
               })
           })
           .catch(() => { //选择取消按钮后要执行的函数
               ElMessage({
                   type: 'info',
                   message: '你选择了取消',
               })
           })
   }
   </script>
   <template>
       <el-button plain @click="openBox">点击打开消息提示对话框</el-button>
   </template>
   ```

   测试运行网页,观察点击按钮后触发的消息提示框中的信息
