<!-- 主页面,从欢迎页面跳转或登录至此,可用作用户界面 -->
<script setup>
import { useRouter } from "vue-router";
import { nextTick, provide, ref } from "vue";

let router = useRouter();
const backToWelcomePage = () => {
  router.push('/')//返回默认欢迎页面
}
const backToLastStep = () => {
  router.back() //back()方法:返回上一次路由的页面,对子路由也有效
}

const loadChildPage = () => { //加载子路由的按钮方法,其中/main_page指向本页面,这里使用带?问号的url字符串向子路由页面传递了两个参数
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

const showViewOrNot = ref(true) //已绑定 router-view 路由出口的 v-if 属性
const reloadPage = () => { //用来刷新组件页面的方法,该方法一般在子页面调用
  // window.location.reload(); //该方法为网页自带的刷新整个页面的方法,调用后会连同整个父页面被刷新,不推荐
  showViewOrNot.value = false; // v-if=false ,使子页面处于卸载状态
  // nextTick() 方法需要从 vue 组件中声明导入,形参为回调函数,表示在下次DOM更新循环结束之后延迟执行该函数
  // 调用该函数使得子页面彻底被卸载完毕后再重新加载,否则可能会重载失败
  nextTick(() => {
    showViewOrNot.value = true; // v-if=true ,重新加载子页面
  })
  console.log('页面已刷新')
}
// provide() 方法需要从 vue 组件中声明导入,需填入一个字符串和回调函数.该回调函数将会以该字符串命名,向子页面穿透提供该方法
// 使得 reloadPage() 方法可以被其他子页面和子页面的子页面使用,使其能有效传递来自父页面的字段 showViewOrNot 
// 对于子页面,需使用 inject('flush') 引用该方法
provide('flush', reloadPage)

</script>

<template>
  <!-- el-container:用于对页面进行整体布局的容器 -->
  <el-container>
    <el-aside><!-- 侧边栏内容 -->
      <!-- default-active:页面加载时默认激活菜单的 index ,1-1表示1号菜单下的1号子菜单-->
      <!-- :router="true" 将 index 属性视为子路由地址,开启菜单栏点击后自动加载子路由组件的功能-->
      <el-menu default-active="1-1" :router="true">
        <el-sub-menu index="1"><!-- sub-menu表示包含子节点的菜单节点,可如下指定标题 -->
          <template #title>
            <span>路由跳转与vue生命周期测试</span>
          </template>
          <!-- el-menu-item表示不包含子节点的最终节点菜单,index表示其序号或子路由地址,@click表示绑定的vue点击事件 -->
          <el-menu-item index="1-1" @click="backToWelcomePage">跳回欢迎界面</el-menu-item>
          <el-menu-item index="1-2" @click="backToLastStep">路由回滚</el-menu-item>
          <el-menu-item index="1-3" @click="loadChildPage">使用click方法加载有参子组件</el-menu-item><!-- 有参 -->
          <el-menu-item index="1-4" @click="loadChildPageByObject">另一种方法向组件传参</el-menu-item><!-- 有参 -->
          <!-- 此处 index 属性为子路由地址,点击即可出发子路由加载事件,同上-->
          <!-- 该网址使用rest风格向子路由页面传递了两个参数 -->
          <el-menu-item index="/main_page/child_page_load2/猪猪侠/超级棒棒糖">使用菜单自带组件加载带参组件</el-menu-item>
          <el-menu-item index="/main_page/vue_life_cycle_test">vue生命周期方法测试</el-menu-item>
          <el-menu-item index="/main_page/multi_children_pages_test/超天酱/啊哈哈哈哈哈哈哈">这个子组件内部有多个子组件</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <span>vue语法</span><!-- 在此声明vue语法相关的学习模块,已写好路由 -->
          </template>
          <el-menu-item index="/main_page/vue_binding_test">vue双向绑定测试</el-menu-item>
          <el-menu-item index="/main_page/vue_instruction_test">vue通用指令测试</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="3">
          <template #title>
            <span>Element Plus 与 ECharts 测试</span><!-- 在此声明Element Plus组件测试模块 -->
          </template>
          <el-menu-item index="/main_page/button_icon_test">按钮与图标测试</el-menu-item>
          <el-menu-item index="/main_page/form_test">表单测试</el-menu-item>
          <el-menu-item index="/main_page/message_box_test">消息与消息弹出框测试</el-menu-item>
          <el-menu-item index="/main_page/echarts_test">ECharts测试</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="4">
          <template #title>
            <span>axios数据交互测试</span><!-- 在此声明Element Plus组件测试模块 -->
          </template>
          <el-menu-item index="/main_page/axios_outer_test">外网数据交互</el-menu-item>
          <el-menu-item index="/main_page/axios_spring_boot_test">本地Springboot服务器连接测试</el-menu-item>
          <el-menu-item index="/main_page/up_and_down_load_flies">上传下载文件测试</el-menu-item>
        </el-sub-menu>

      </el-menu>
    </el-aside>
    <el-main>
      <!-- 主要区域内容,声明了子路由加载的位置.v-if绑定了一个布尔变量,为true时加载组件,为false不加载组件 -->
      <router-view v-if="showViewOrNot" />
    </el-main>
  </el-container>
</template>

<style scoped>
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

.el-button {
  width: 90%;
}

.el-menu {
  background-color: rgb(200, 200, 200);
}
</style>