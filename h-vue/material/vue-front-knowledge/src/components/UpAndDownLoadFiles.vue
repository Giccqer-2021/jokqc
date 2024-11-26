<script lang="ts" setup>
import { ref } from 'vue'
import { genFileId } from 'element-plus'
//import type 是 ECMAScript 模块系统中的一种语法,用于引入类型信息而不引入实际的运行时代码,常与 TypeScript 类型检查工具一起使用
import type { UploadInstance, UploadProps, UploadRawFile, UploadUserFile } from 'element-plus'
import { uploadFiles, downloadFile } from '../utils/file-request.js'

let uploadRef = ref<UploadInstance>() //绑定组件数据的对象
let fileList = ref<UploadUserFile[]>([]) //绑定组件列表元素的对象
//当上传的文件数量超过限制数时,清空原数据中的文件信息,使用新选择的文件覆盖它
const handleExceed: UploadProps['onExceed'] = (files) => {
  uploadRef.value!.clearFiles()
  let file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploadRef.value!.handleStart(file)
}
const submitUpload = () => { //点击按钮后触发的提交事件
  uploadRef.value!.submit()
}
const successUpload = () => { //文件上传成功后触发
  alert('文件上传成功,已上传: ' + fileList.value[0].name)
  uploadRef.value!.clearFiles()
}
const failUpload = () => { //文件上传失败后触发
  alert('文件上传失败')
}

//使用 post 方法上传文件.该方法会根据上传的文件数量分别发送多个不同的异步文件上传请求,因此也同样会返回多个成功响应体
const uploadByAxios = (options) => {
  let data = new FormData() //将文件编码并包含进FormData对象中,固定写法
  data.append("file", options.file)
  uploadFiles(data).then(resp => {
    alert(resp.data.title + ': ' + resp.data.message)
  })
}

let filePath = ref("内置文件夹\\二级警报BGM.ogg"); //要下载的文件路径,与表单内容绑定
//用来处理从响应体中获取下载文件内容的处理方法,需要先创建一个隐藏的表单元素并主动触发其"点击"事件,固定写法照写便是
const downloadOperation = resp => {
  let blob = new Blob([resp.data]) // 以二进制文件对象 Blob 的方式封装响应体中的数据
  const url = window.URL.createObjectURL(blob) // 创建 Blob 对象的临时 url
  console.log('创建的下载节点: ' + url)
  const link = document.createElement('a') // 创建一个a标签用于下载
  link.href = url // 设置其 href 属性
  link.setAttribute('download', filePath.value.split('\\').pop().split('/').pop()) // 设置下载的文件名,键值'download'为固定写法
  document.body.appendChild(link) // 把标签加载页面的body中
  link.click() // 主动触发点击事件模拟下载
  document.body.removeChild(link) // 从DOM中移除a标签
  window.URL.revokeObjectURL(url) // 释放URL对象
  alert("文件开始下载!")
}
const errorOperator = error => { //当文件下载出错(例如404)时要执行的回调函数
  let errorCode = error.response
  if (errorCode) alert("文件下载失败,错误代码: " + errorCode.status)
  else alert("文件下载失败,远程服务器无响应或跨域响应错误")
}
const downloadSubmit = () => { //第一个submit按钮绑定的下载事件
  downloadFile('get', filePath.value).then(downloadOperation).catch(errorOperator)
}
const downloadSubmit2 = () => { //第二个submit按钮绑定的下载事件
  downloadFile('get', filePath.value, 2).then(downloadOperation).catch(errorOperator)
}
const downloadSubmit3 = () =>  { //第三个submit按钮绑定的下载事件
  downloadFile('post', filePath.value, 3).then(downloadOperation).catch(errorOperator)
}
</script>
<template>
  <!-- el-upload 为 Element Plus 的一种用于上传文件的组件,参数定义如下:
   ref:本组件中的后台数据所绑定的对象
   action:类似于表单的 action 属性,向后端网址发送文件上传请求,其请求体内文件对象键值固定为 file 
   :limit:单次上传的文件个数限制
   :on-exceed:当选择上传的文件超过 limit 限制后要使用的处理方法
   :auto-upload:当选择(或拖拽)文件后是否立即进行上传
   drag:是否启用文件拖拽功能,默认false
   v-model:file-list:本组件中的列表元素所绑定的对象
   :on-success:上传成功时要执行的方法
   :on-error:上传出错时要执行的方法
   注意:这段组件使用传统表单提交的方式上传文件,并不涉及axios-->
  <el-upload ref="uploadRef" action="http://localhost:8080/storage/upload" :limit="1" :on-exceed="handleExceed"
    :auto-upload="false" drag v-model:file-list="fileList" :on-success="successUpload" :on-error="failUpload">
    <template>
      <el-button type="primary">选择一个文件,或将文件拖入此处</el-button>
    </template>
    <el-button class="ml-3" type="success" @click="submitUpload"><!-- @click:绑定了按钮点击触发事件submitUpload -->
      将选择的文件上传至后端
    </el-button>
    <template>
      <div class="el-upload__tip text-red">
        仅限上传一个文件,新选择的文件将会覆盖旧文件
      </div>
    </template>
  </el-upload>
  <hr /><!-- 同理,本组件使用传统表单提交方式上传多个文件, multiple: 是否允许在提交框中提交多个文件,默认为false -->
  <el-upload action="http://localhost:8080/storage/multi_upload" drag multiple>
    <el-button type="primary">直接上传多个文件,可将文件拖入后上传</el-button>
  </el-upload>
  <hr /><!-- 本组件使用axios自定义文件上传方式,绑定了 uploadByAxios 方法
    :http-request:覆盖默认的 Xhr 行为,自行实现上传文件的请求 -->
  <el-upload drag multiple :http-request="uploadByAxios">
    <el-button type="primary">使用axios上传多个文件,可将文件拖入后上传</el-button>
  </el-upload>
  <hr /><!-- 实际上文件的下载就是发送一个get请求 -->
  <a href="http://localhost:8080/storage/download/超天酱.jpg">点击这里下载一张超天酱的照片</a>
  <br><!-- 由于后端的设置,这里使用逗号,分隔请求的文件路径 -->
  <a href="http://localhost:8080/storage/download2/内置文件夹,二级警报BGM.ogg">点击这里下载内置文件夹下二级警报音乐</a>
  <hr />
  <!-- 使用 el-form 表单来决定从后端下载哪个文件 -->
  <el-form label-width="200px">
    <el-form-item label="请输入要下载的文件路径">
      <el-input v-model="filePath" /><!-- 其中的内容与 filePath 字符串进行了绑定 -->
    </el-form-item>
    <el-form-item><!-- 三个提交方法对应着后端三种下载方法 -->
      <el-button type="primary" @click="downloadSubmit">使用方法1下载(get方法)</el-button>
      <el-button type="primary" @click="downloadSubmit2">使用方法2下载(get方法)</el-button>
      <el-button type="primary" @click="downloadSubmit3">使用方法3下载(post方法)</el-button>
    </el-form-item>
  </el-form>
</template>