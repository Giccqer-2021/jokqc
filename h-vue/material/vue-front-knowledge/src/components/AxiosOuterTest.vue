<script setup>
import { doGet, doPost } from "../utils/request";//导入get,post方法(这个模块是自己写的)
// 按钮绑定的向往外发送数据的方法,用doPost()返回的对象.then()中添加回调函数用来处理发送数据成功后要做的事(不成功则报错)
// resp表示服务端返回的数据
const getRequest = () => {
    doGet(
        //相当于 https://echo.apifox.com/get?q1=这是参数1&q2=这是参数2 ,该url为外网专门用以测试端口连接的网站网址
        'https://echo.apifox.com/get',
        {
            q1: "这是参数1",
            q2: "这是参数2"
        }
    ).then(resp => {
        console.log(resp.data)//控制台输出
        //对话框输出
        alert('get请求发送的请求参数为: q1-' + resp.data.args.q1 + '   q2-' + resp.data.args.q2)
    })
}
const postRequest = () => {
    doPost(
        'https://echo.apifox.com/post',//一般post方法不会在url中携带请求参数,但也可以这么做
        {
            d: "你好呀",
            dd: "打声招呼吧"
        }
    ).then(resp => {
        console.log(resp.data)
        //resp.data表示得到请求体数据
        alert('post请求发送请求体为:' + resp.data.data)
    })
}
</script>

<template>
    <!-- 按下后会向指定的外网发生一些数据,分别使用get与post方法 -->
    <el-button type="primary" @click="getRequest">点此发送get请求进行外网连接测试</el-button>
    <hr />
    <el-button type="primary" @click="postRequest">点此发送post请求进行外网连接测试</el-button>
</template>