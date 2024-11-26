<script setup>
// 引入图标组件,本页面生效,否则无法显示图标
import { Check, Delete, Edit, Message, Search, Star, Setting } from '@element-plus/icons-vue'
</script>

<template>
  <div class="mb-4"><!-- 来自 Element Plus 官网的按钮代码 -->
    <el-button>Default</el-button>
    <el-button type="primary">Primary</el-button>
    <el-button type="success">Success</el-button>
    <el-button type="info">Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button type="danger">Danger</el-button>
  </div>
  <hr /><!-- 换行 -->
  <div><!-- 以下按钮皆带图标,icon=图标,要先在script中引入 -->
    <el-button :icon="Search" circle />
    <el-button type="primary" :icon="Edit" circle />
    <el-button type="success" :icon="Check" circle />
    <el-button type="info" :icon="Message" circle />
    <el-button type="warning" :icon="Star" circle />
    <el-button type="danger" :icon="Delete" circle />
  </div>
  <hr /><!-- 单独使用的图标 -->
  <el-icon>
    <Setting />
  </el-icon>
</template>