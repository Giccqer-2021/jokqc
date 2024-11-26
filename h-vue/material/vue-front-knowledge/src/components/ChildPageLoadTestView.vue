<!-- 用于测试子路由加载 -->
<script setup>
import { useRoute } from 'vue-router';

let routeObject = useRoute() //与 useRouter() 方法不同,这个方法所构建的对象并不负责路由跳转,而是负责获取子路由相关的参数信息
const getRoutePath = () => {
    alert('该页面的路由地址是: ' + routeObject.path) //代码中不可以使用 $route
}
</script>

<template>
    <p>click方法加载子路由组件成功!</p>
    <p>该页面的路由地址是: {{ $route.path }}</p><!-- $route对象表示当前路由状态,可在页面元素中使用,但不能在script代码中使用 -->
    <!-- 使用 query 表示获取用传统方法添加的url的参数值 -->
    <p>传递到该页面的参数是: name= {{ $route.query.name }} ,message= {{ $route.query.message }}</p>
    <hr />
    <el-button plain @click="getRoutePath">点击这里使用对话框输出路由地址</el-button>
    <hr />
    <!-- 同理,$router可在页面元素中使用,用来执行路由跳转事件 -->
    <el-button plain @click="$router.push('/')">点击这里返回欢迎页面</el-button>
    <div><!-- router-link标签,相当于超链接标签,只不过连接地址为路由地址,可以使用一个对象添加路由参数信息 -->
        <router-link :to="{ path: '/main_page/child_page_load', query: { name: '漆黑噤默', message: '痛苦啊,你是我的唯一' } }">
            点击这里换一句酷炫的台词
        </router-link>
    </div>
    <hr />
    <div><!-- 使用字符串直接标明要跳转的路由地址和添加的参数信息 -->
        <router-link :to="'/main_page/child_page_load?name=苍蓝残响&message=开始吧,这是我们改变世界的序曲'">
            点击这里换另一句酷炫的台词
        </router-link>
    </div>
</template>

<style scoped>
p {
    color: red
}
</style>