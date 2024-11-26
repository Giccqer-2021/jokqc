<script setup>
import { useRouter } from "vue-router";
let router = useRouter();

const group_changed = () => { //切换为本页面下的其他子页面,显然某些路径段并非真正的路径
  router.push('/main_page/multi_children_pages_test/菲利普/没人能替悲伤的我哭泣/another_group')
}
</script>

<template>
  <router-view /><!-- 默认子路由地址 -->
  <hr />
  <router-view name="second" />
  <hr />
  <router-view name="third" />
  <hr />
  <el-button type="primary" @click="group_changed">点击这里换一组子路由页面(换不回去)</el-button>
</template>

<style scoped>
.el-button {
  width: 90%;
  color: #11110e;
  background-color: rgb(209, 134, 187);
  font-size: large;
}
</style>