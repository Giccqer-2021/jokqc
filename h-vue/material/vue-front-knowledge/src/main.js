import { createApp } from 'vue'//从vue框架中导入createApp函数
import './style.css'//导入同目录下的css样式
import App from './App.vue'//从 App.vue 页面导入App组件

import Router from './router/router-config.js'//导入路由模块

import ElementPlus from 'element-plus'//导入ElementPlus组件
import 'element-plus/dist/index.css'//导入ElementPlus相关的样式

//使用createApp函数和App组件创建一个vue应用并挂载到index.html页面中id=app的标签下
//同时声明使用ElementPlus,router路由组件
createApp(App).use(Router).use(ElementPlus).mount('#app')