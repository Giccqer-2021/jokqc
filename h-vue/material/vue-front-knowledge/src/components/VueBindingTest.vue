<!-- 用于测试vue双向绑定语法 -->
<script setup>
import { ref } from "vue";//导入 ref 函数

let num = ref(0);//测试数字与标签内容的双向绑定,js中取值时记得加 .value
const add_num = () => {
  num.value++
}

let image_path = ref("/src/assets/六协会小图标.png")//测试标签属性与字符串内容的双向绑定
let image_flag = ref(true)//标志变量
const image_change = () => {//点击切换标志变量来更改图片路径
  if (image_flag.value) {
    image_path.value = "/src/assets/seven协会小图标.png"
    image_flag.value = false

  }
  else {
    image_path.value = "/src/assets/六协会小图标.png"
    image_flag.value = true
  }
}

let inputMessage = ref('这里测试输入框使用 v-model 属性与字符串字段进行绑定') //该字段与下方input输入框的 v-model 属性绑定
</script>

<template>
  <div>{{ '数字:' + num }}</div> <!-- 双大括号用于双向绑定 -->
  <el-button type="primary" @click="add_num">点此让上面的值加一</el-button>
  <hr>

  <!-- 点击按钮会切换图片,:src表示绑定了image_path变量,相当于 v-bind:src='image_path' -->
  <img :src="image_path" /><br>
  <el-button type="primary" @click="image_change">点此切换上方图片</el-button>
  <hr />

  <div>下方输入框中的文字: {{ inputMessage }}</div>
  <!-- v-model 属性,相当于将 value 属性用 v-bind 绑定,并绑定 @input 事件使被绑定的字段的值随着输入框中的内容的改变而实时更新-->
  <p>会在输入时实时更新其他被绑定内容的对话框 &nbsp;<input v-model="inputMessage" id="VMI" /></p>
  <!-- 改为使用 v-model.lazy 属性,在该对话框内容更改完毕后才会更新与之绑定的数据 -->
  <p>不会实时更新而是在输入完毕后更新的对话框 &nbsp;<input v-model.lazy="inputMessage" id="VMI" /></p>
</template>

<style scoped>
#VMI {
  width: 60%;
}
</style>