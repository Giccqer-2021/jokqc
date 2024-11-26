# router路由,vue生命周期

## 父路由(页面)的加载及切换

1. 输入cmd指令安装路由模块: 
   
   ```sh
   npm i vue-router
   ```

   之后需要在 [main.js](material\vue-front-knowledge\src\main.js) 中导入并声明使用它:
   
   ```javascript
   import Router from './router/router-config.js'
   app.use(Router)//let app=createApp(App)
   ```
   
2. 关于路由原理:一般设定 / 为根路径,而 /A /B /C 等网址皆为根路径的子路径, /A/a /A/b /A/c 为 /A 的子路径,以此类推.加载页面时,先加载父路径页面元素,方法,样式等,在此基础上(可能位于父页面的某个位置)加载子路径页面,子路径页面随时可以被切换且不会影响父路径页面的加载.同样,仅使用网址即可十分准确地告知服务器需要加载的父页面,子页面是什么,例如:

   > 网址 /A/b/3 表示加载根页面下的A页面,再在A页面中加载b组件,再在b组件中加载3组件

   这在多级列表或菜单栏的应用中十分普遍,通常我们将 [App.vue](material\vue-front-knowledge\src\App.vue) 作为路由根节点,为了能够进行整个页面级别的路由,在这个文件中一般不会写入多余的元素:
   ```vue
   <template>
     <router-view />
   </template>
   ```

   <router-view />标签声明了子路由的出口,每个页面只能拥有一个该标签,可以使用特定的方法随时切换子页面或子组件

3. 在 src 源码下创建 router 文件夹并创建 [router-config.js](material\vue-front-knowledge\src\router\router-config.js) ,声明两个地址映射,模拟用户首次访问的欢迎页面和登陆后的操作页面:
   ```javascript
   import { createRouter, createWebHistory } from "vue-router";
   const Router = createRouter({
       history: createWebHistory(),
       //对象数组,path表示映射的路径名,component:() => import('网页地址')回调方法内写入其映射的路径位置
       routes: [ 
           {
               path: '/', //根路由,首次访问时的欢迎页面,默认加载路径就是 /
               component: () => import('../views/WelcomeView.vue')
           },
           {
               path: '/main_page', //登陆成功后的主要操作页面
               component: () => import('../views/MainView.vue')
           }
       ]
   })
   export default Router;
   ```

4. 在 src 文件夹下创建文件夹 views,创建 [WelcomeView.vue](material\vue-front-knowledge\src\views\WelcomeView.vue) 欢迎视图组件,写入一个按钮及其对应的事件 toMainPage() :
   ```vue
   <script setup>
   import { useRouter } from "vue-router"; //导入路由
   let router = useRouter(); //定义对象
   const toMainPage = () => { //为按钮绑定一个跳转页面事件
     router.push('/main_page') //在router.js中已经写好其映射地址了,点击跳转至主页面
   }
   </script>
   <template>
       <p>欢迎学习vue前端相关知识</p><hr/>
       <button @click="toMainPage">让我们开始吧</button>
   </template>
   ```

   创建 [MainView.vue](material\vue-front-knowledge\src\views\MainView.vue) 用户操作的主页面,写入一个跳回页面的按钮用来测试页面的往返跳转,方法为 backToWelcomePage() :
   ```vue
   <script setup>
   import { useRouter } from "vue-router";
   let router = useRouter();
   const backToWelcomePage = () => {
       router.push('/')//返回默认欢迎页面,对于没有子路由的页面可以用 router.back() 方法返回上一级路由
   }
   </script>
   <template>
       <p>欢迎来到主界面</p><hr/>
       <button @click="backToWelcomePage">跳回欢迎界面</button>
   </template>
   ```

5. 运行cmd指令: npm run dev 点击按钮测试路由效果

## 子路由(组件)的加载及传参

1. 建议先完成 element-plus 对 [MainView.vue](material\vue-front-knowledge\src\views\MainView.vue) 主页面的页面布局后学习

2. 在 [router-config.js](material\vue-front-knowledge\src\router\router-config.js) 中配置:在 path 属性为 '/main_page' 的对象中添加 children 属性,该属性为数组对象,对于rest风格传递参数的网址有其独特的网址配置:
   ```javascript
   children: [ //子路由配置,对象数组
       {
           path: 'child_page_load', //前面不可以加斜杠,需输入 /main_page/child_page_load 加载该组件
           component: () => import('../components/ChildPageLoadTestView.vue'),
           name: 'ChildPage' //name属性,可以用适当的方法引用该属性从而实现页面跳转,该属性的值必须唯一
       },
       {   //需输入/main_page/child_page_load2加载,这里加:冒号表示其内容为rest风格网址所传递的参数而并非是真正的地址
           path: 'child_page_load2/:name/:message', 
           component: () => import('../components/ChildPageLoadTestView2.vue')
       }
   ]
   ```

   同时在 components 文件夹中创建对应的测试用vue组件: [ChildPageLoadTestView.vue](material\vue-front-knowledge\src\components\ChildPageLoadTestView.vue) ,用来测试路由跳转,路由地址的获取,使用 query 属性载入url参数和测试 <router-link> 标签的用法:
   ```vue
   <script setup>
   import { useRoute } from 'vue-router';
   let routeObject = useRoute() //与useRouter()方法不同,该方法构建的对象不负责路由跳转,而负责获取子路由相关的参数信息
   const getRoutePath = () => {
       alert('该页面的路由地址是: ' + routeObject.path) //代码中不可以使用 $route
   }
   </script>
   <template>
       <p>click方法加载子路由组件成功!</p>
       <p>该页面的路由地址是: {{ $route.path }}</p><!-- $route表示当前路由状态,可在页面中使用,不可能在代码中使用 -->
       <!-- 使用 query 表示获取用传统方法添加的url的参数值 -->
       <p>传递到该页面的参数是: name= {{ $route.query.name }} ,message= {{ $route.query.message }}</p>
       <hr />
       <el-button plain @click="getRoutePath">点击这里使用对话框输出路由地址</el-button>
       <hr />
       <!-- 同理,$router可在页面元素中使用,用来执行路由跳转事件 -->
       <el-button plain @click="$router.push('/')">点击这里返回欢迎页面</el-button>
       <div><!-- router-link标签,相当于超链接标签,只不过连接地址为路由地址,可以使用一个对象添加路由参数信息 -->
           <router-link :to="{ path: '/main_page/child_page_load', 
                             query: { name: '漆黑噤默', message: '痛苦啊,你是我的唯一' } }">
               点击这里换一句酷炫的台词
           </router-link>
       </div><hr />
       <div><!-- 使用字符串直接标明要跳转的路由地址和添加的参数信息 -->
           <router-link :to="'/main_page/child_page_load?name=苍蓝残响&message=开始吧,这是我们改变世界的序曲'">
               点击这里换另一句酷炫的台词
           </router-link>
       </div>
   </template>
   ```

   同时创建  [ChildPageLoadTestView2.vue](material\vue-front-knowledge\src\components\ChildPageLoadTestView2.vue) 文件,测试使用 params 属性载入 rest风格网址的参数:
   ```vue
   <template>
       <p>菜单栏加载子路由组件成功!</p>
       <p>该页面的路由地址是: {{ $route.path }}</p>
       <!-- 同理,使用 params 属性来获取rest风格网址中的参数信息 -->
       <p>传递到该页面的参数是: name= {{ $route.params.name }} ,message= {{ $route.params.message }}</p>
   </template>
   ```

3. 添加能触发点击事件的组件(可以是按钮,这里用三个子菜单),测试使用两种不同的路由触发方式(绑定按钮事件或菜单自带的路由功能)以及三种不同方式传参方式,注意:若想让第三个子菜单路由事件生效,必须要在 <el-sub-menu> 菜单父项中添加 :router="true" 声明将子菜单 <el-menu-item> 的 index 属性视为路由跳转到的地址 
   ```vue
   <template>
   <!-- :router="true" 将 index 属性视为子路由地址,开启菜单栏点击后自动加载子路由组件的功能-->
   <el-menu default-active="1-1" :router="true">
       <el-sub-menu index="1"><!-- sub-menu表示包含子节点的菜单节点,可如下指定标题 -->
           <template #title>
               <span>路由跳转与vue生命周期测试</span>
           </template>
   	<!-- 绑定了按钮事件,使用字符串指定url和要传递的参数 -->
       <el-menu-item index="1-1" @click="loadChildPage">使用click方法加载有参子组件</el-menu-item>
       <!-- 绑定了按钮事件,使用一个对象指定url和要传递的参数 -->
       <el-menu-item index="1-2" @click="loadChildPageByObject">另一种方法向组件传参</el-menu-item>
       <!-- 此处 index 属性为子路由地址,点击即可出发子路由加载事件,同上-->
       <!-- 该网址使用rest风格向子路由页面传递了两个参数 -->
       <el-menu-item index="/main_page/child_page_load2/猪猪侠/超级棒棒糖">使用菜单自带组件加载带参组件</el-menu-item>
       </el-sub-menu>
   </el-menu>
   </template>
   ```

   其中两个绑定了按钮事件的路由方法为 loadChildPage() 和 loadChildPageByObject() :
   ```vue
   <script setup>
   //加载子路由的按钮方法,其中/main_page指向本页面,这里使用带?问号的url字符串向子路由页面传递了两个参数
   const loadChildPage = () => { 
     router.push('/main_page/child_page_load?name=胡桃&message=太阳出来我晒太阳')
   }
   const loadChildPageByObject = () => { //同上,不同点在于本次使用带有子路由名称和query参数属性的对象作为跳转依据
     router.push({
       name: 'ChildPage', //对应网址 /main_page/child_page_load ,取决于路由配置
       query: { //相当于在网址后额外添加的参数
         name: '邵',
         message: '我纵茕茕孑立,难避漫漫长夜'
       }
     })
   }
   </script>
   ```

4. 在 <el-main> 主要内容区域标签中添加:
   ```html
   <router-view /><!-- 声明了子路由加载的位置 -->
   ```

   运行cmd指令: npm run dev ,分别点击三个创建好的子菜单,观察右侧页面的变化及参数传递情况.在第一个子菜单被点击后,点击右侧页面中的几个按钮和链接地址,观察其作用是否符合预期

## 同页面多子路由出口的组件映射

1. 在子路由中同样可以声明多个子路由,在 [router-config.js](material\vue-front-knowledge\src\router\router-config.js) 中,添加 main_page 的子路由(以及该子路由的子路由):
   ```javascript
   path: 'multi_children_pages_test/:name/:message',
   component: () => import('../components/MultiChildrenPagesTest.vue'), //该页面属于教程页面
   children: [{ //子路由页面中的子路由组件
       path: '', //对子路由来说,path为空的路径就是默认路径，注意：如果不写path属性则会全局报错
       components: { //使用 components 属性时就不要使用 component 属性了,这里配置一组映射页面路径
           // default 默认路由路径的出口就是 <router-view />
           default: () => import('../components/ChildPageLoadTestView2.vue'),
           // 使用 name 属性声明该页面的路由出口: <router-view name="second" />,
           second: () => import('../components/BindingTest.vue'), //使用任意可映射到的页面即可
           // <router-view name="third" />
           third: () => import('../components/ButtonIconTest.vue') //使用任意可映射到的页面即可
       }
   },
   { //子子路由路径,必须同时定义页面三个出口所对应的组件,即使 default 对应的组件没有变化也要写上否则不显示组件
       path: 'another_group',
       components: {
           default: () => import('../components/ChildPageLoadTestView2.vue'),
           second: () => import('../components/FormTest.vue'), //使用任意可映射到的页面即可
           third: () => import('../components/AxiosOuterTest.vue') //使用任意可映射到的页面即可
       }
   }]
   ```

2. 创建 multi_children_pages_test 所对应的页面 [MultiChildrenPagesTest.vue](material\vue-front-knowledge\src\components\MultiChildrenPagesTest.vue) :
   ```vue
   <script setup>
   import { useRouter } from "vue-router";
   let router = useRouter();
   const group_changed = () => { //切换为本页面下的其他子页面,显然某些路径段并非真正的路径
     router.push('/main_page/multi_children_pages_test/菲利普/没人能替悲伤的我哭泣/another_group')
   }
   </script>
   <template>
     <router-view /><!-- 默认子路由地址 --><hr />
     <router-view name="second" /><hr />
     <router-view name="third" /><hr />
     <el-button type="primary" @click="group_changed">点击这里换一组子路由页面(换不回去)</el-button>
   </template>
   ```

3. 测试页面,观察页面是否成功加载文件中所映射的子页面.点击按钮,观察页面是否成功替换其中的所有子组件

## vue生命周期

1. 当 vue 页面在执行时,会有以下生命周期:
   <img src="material/vue生命周期.png" alt="vue生命周期" style="zoom: 33%;" />
   其中前三个周期( setup,beforeCreate,created )在代码中无法被引用,其他的生命周期只若要引用至代码中,只需在其名称前面加on,作为方法从 vue 组件中导入即可,其中 onMounted() 方法在日常中使用得最多

2. 在页面 [VueLifeCycleTest.vue](material\vue-front-knowledge\src\components\VueLifeCycleTest.vue) 中创建一个div标签和一个按钮,为按钮绑定切喊div标签中的文字的方法 divTextSwitch() ,用来引起页面元素的变化刷新
   ```vue
   <script setup>
   import { ref } from "vue";
   let divText = ref("这是本页面div标签元素中的文字") //与div双向绑定的文字
   let switchFlag = ref(true) //切换文字的标志变量
   const divTextSwitch = () => { //按钮绑定的切换文字的方法
       if (switchFlag.value) {
           divText.value = "你看到了我,看到了我"
           switchFlag.value = false
       }
       else {
           divText.value = "是哪一种颜色,悲伤或快乐"
           switchFlag.value = true
       }
   }
   </script>
   <template>
       <div>{{ divText }}</div>
       <hr />
       <el-button plain @click="divTextSwitch">点击修改上方的文字</el-button>
   </template>
   ```

3. 在本页面中添加相应的 vue生命周期函数钩子 ,依次加入所有可用的生命周期方法:
   ```vue
   <script setup>
   import { onBeforeMount, onBeforeUnmount, onBeforeUpdate, onMounted, onUnmounted, onUpdated } from "vue";
   onBeforeMount(() => { //比onMounted()方法执行得早些
       console.log('onBeforeMount方法已执行')
   })
   //最常用的方法,在刚开始加载页面元素时执行,只执行一次,一般用来执行从后端获取用户数据然后加载它的方法
   onMounted(() => {
       console.log('onMounted方法已执行')
       alert('onMounted: 这是页面元素创建后弹出的对话框')
   })
   onBeforeUpdate(() => { //比onUpdated()方法执行得早些
       console.log('onBeforeUpdate方法已执行')
   })
   //页面元素内容有变化时会执行的方法,只要变化就执行,可能执行很多次,若本页面某变量发生改变而页面元素未改变则不执行该方法
   onUpdated(() => {
       console.log('onUpdated方法已执行')
       alert('onUpdated: 这是页面元素被改写后弹出的对话框')
   })
   onBeforeUnmount(() => { //比onUnmounted()方法执行得早些
       console.log('onBeforeUnmount方法已执行')
   })
   onUnmounted(() => { //当离开该页面(页面元素销毁后)执行的方法,只执行一次
       console.log('onUnmounted方法已执行')
       alert('这是页面元素销毁后弹出的对话框')
   })
   </script>
   ```

4. 运行该页面,打开控制台,观察在进入该页面和离开该页面时的弹窗及控制台内容.点击按钮刷新页面元素,观察弹窗和控制台

5. 刷新页面的方法:可以使用以下方法对整个页面进行刷新:
   ```javascript
   window.location.reload();
   ```

   如果仅刷新某个路由子页面,可以在父页面 [MainView.vue](material\vue-front-knowledge\src\views\MainView.vue) 中的路由出口标签 <router-view> 为 v-if 绑定一个布尔变量,该变量为 true 时表示加载子页面,为 false 时表示卸载子页面
   ```html
   <router-view v-if="showViewOrNot" />
   ```

   创建该变量,为变量绑定一个刷新该变量的方法:
   ```javascript
   import { nextTick, ref } from "vue";
   const showViewOrNot = ref(true) //已绑定 router-view 路由出口的 v-if 属性
   const reloadPage = () => { //用来刷新组件页面的方法,该方法一般在子页面调用
     showViewOrNot.value = false; // v-if=false ,使子页面处于卸载状态
     // nextTick() 方法需要从 vue 组件中声明导入,形参为回调函数,表示在下次DOM更新循环结束之后延迟执行该函数
     // 调用该函数使得子页面彻底被卸载完毕后再重新加载,否则可能会重载失败
     nextTick(() => {
       showViewOrNot.value = true; // v-if=true ,重新加载子页面
     })
     console.log('页面已刷新')
   }
   ```

   使用 vue 组件的 provide() 方法,将该函数穿透到子页面,使子页面可以调用该方法和父页面的 showViewOrNot 字段
   ```javascript
   import { provide, ref } from "vue";
   provide('flush', reloadPage)
   ```

   在子页面 [VueLifeCycleTest.vue](material\vue-front-knowledge\src\components\VueLifeCycleTest.vue) 中,创建刷新该子页面的按钮:
   ```html
   <el-button plain @click="flushPage">点击这里刷新本页面</el-button>
   ```

   创建为按钮绑定的 flushPage() 方法:
   ```javascript
   import { inject } from "vue";
   //该方法导入了父页面刷新子页面的方法 reloadPage()
   let flushPage = inject('flush')
   ```

   运行页面,在页面中点击该按钮,查看对话框和控制台输出.该按钮可以还原页面中被更改的 <div> 标签中的文本

