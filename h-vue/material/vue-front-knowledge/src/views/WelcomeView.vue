<!-- 首次访问本网站所加载的欢迎页面,可设计为登陆页面 -->
<script setup>
import { useRouter } from "vue-router"; //导入路由
let router = useRouter(); //定义对象
const toMainPage = () => { //为按钮绑定一个跳转页面事件
  // window.location.href = '/main_page' //传统的页面跳转方法,全页面刷新,不推荐,易造成混乱
  router.push('/main_page') //在router.js中已经写好其映射地址了,点击跳转至主页面
}
</script>
<template>
  <p>欢迎学习vue前端相关知识</p>
  <hr />
  <button @click="toMainPage">让我们开始吧</button>
</template>