<script setup>
import { doGet, doPost } from "../utils/request";
import { ref } from "vue";
import { tokenRequest, TOKEN_NAME } from "../utils/token-request";

const getRequest = () => {
  doGet(
    'http://localhost:8080/hello', //后端SpringBoot服务器要地址
    { parameter: ' 前端 返回json数据,内含字符串:你好Springboot!' } //这些数据会被写在网址后面
  ).then(resp => {
    alert(resp.data); //后端返回一个纯字符串
  })
}

let user = ref({ //绑定的账号密码与记住我
  userAccount: '',
  userPassword: '',
  rememberMe: false
})
const submitMethod = () => { //提交方法
  //then后的回调函数表示当请求成功后要执行的函数,通常为路由到某个页面
  doPost('http://localhost:8080/login_demo', user.value).then(resp => {
    alert('后端负责人 ' + resp.data.title + ' ,发来消息: ' + resp.data.message)
  }).catch(error => { //catch后的回调函数表示当执行失败后要执行的函数,如果连接服务器失败则不执行
    //可能的状态码:401身份未验证,402身份验证未通过,404页面不存在等,可以根据状态码使用不同方法处理不同的异常
    alert('登录失败,状态码: ' + error.response.status)
    console.log(error.response.data)
  }).finally(() => { //finally后的回调函数表示无论成败都要执行的函数,甚至连接服务器失败也可能执行
    console.log('登陆方法执行完毕')
  })
}

const visitBlockedPage = () => { //访问需要身份验证的网址,具有 token 才能访问,,没有的话返回401错误
  tokenRequest('/blocked_page').then(resp => {
    if(!resp) return
    alert('后端负责人 ' + resp.data.title + ' ,发来消息: ' + resp.data.message)
  })
}
const deleteToken = () => { //删除本地存储与会话存储中的 token
  window.localStorage.removeItem(TOKEN_NAME);
  window.sessionStorage.removeItem(TOKEN_NAME);
}
const getToken = (url) => { //当获取到新的token时,删除之前存储的token并将其存入会话中
  tokenRequest(url).then(resp => {
    deleteToken()
    let token = resp.data.message
    // window.localStorage.setItem(TOKEN_NAME, token); //长存储
    window.sessionStorage.setItem(TOKEN_NAME, token); //短存储
    alert('成功获取用户身份令牌token: ' + token)
  })
}
</script>

<template>
  <el-button type="primary" @click="getRequest">点此发送get请求与本地SpringBoot服务端进行连接测试</el-button>
  <hr />
  <el-form label-width="60px" style=""><!-- 登陆表单 -->
    <el-form-item label="账号">
      <el-input v-model="user.userAccount" />
    </el-form-item>
    <el-form-item label="密码">
      <el-input type="password" v-model="user.userPassword" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitMethod">点击这里将账号密码信息发送给后端</el-button>
    </el-form-item>
    <el-form-item>
      <el-checkbox label="记住我" size="large" v-model="user.rememberMe" />
    </el-form-item>
  </el-form>
  <hr />
  <el-button type="primary" @click="visitBlockedPage">点此访问需要token验证才能通过的页面</el-button>
  <hr />
  <el-button type="primary" @click="deleteToken">点此删除用户身份token令牌</el-button>
  <hr />
  <el-button type="primary" @click="getToken('/get_token')">点此获取用户身份token令牌</el-button>
  <hr />
  <el-button type="primary" @click="getToken('/get_error_token')">点此获取伪造的用户身份token令牌</el-button>
</template>

<style scoped>
.el-button {
  width: 100%;
  background-color: rgb(75, 170, 177);
  font-size: large;
}
</style>