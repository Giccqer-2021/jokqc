<script setup>
import { ElMessage, ElMessageBox } from 'element-plus' //使用前要导入

const open1 = () => {
    ElMessage({
        showClose: true, //是否展示消息提示框的关闭按钮
        message: '消息对话框', //消息提示框显示的文字
        // type: 'info' //默认就是 info 可省略
    })
}
const open2 = () => {
    ElMessage({
        showClose: true,
        message: '成功对话框',
        type: 'success'
    })
}
const open3 = () => {
    ElMessage({
        showClose: true,
        message: '警告对话框',
        type: 'warning'
    })
}
const open4 = () => {
    ElMessage({
        showClose: true,
        message: '错误对话框',
        type: 'error'
    })
}

const openBox = () => {
    ElMessageBox.alert('你打开了这个对话框', '标题', { //消息提示弹框上的文字和标题
        // autofocus: false, //如果想取消自动对焦功能,添加该属性
        confirmButtonText: '确认', //确认按钮上的文字
        callback: () => { //回调函数,当点击确认后要执行的函数
            ElMessage({
                type: 'info',
                message: '你确认了这个对话框',
            })
        },
    })
}
const openConfirmBox = () => {
    ElMessageBox.confirm('你是选择确认还是取消?', '标题', //确认消息弹框上的文字和标题
        {
            confirmButtonText: '确认', //确认按钮上的文字
            cancelButtonText: '取消', //取消按钮上的文字
            type: 'warning', //消息弹框类型
        }
    )
        .then(() => { //选择确定按钮后要执行的函数
            ElMessage({
                type: 'success',
                message: '你选择了确认',
            })
        })
        .catch(() => { //选择取消按钮后要执行的函数
            ElMessage({
                type: 'info',
                message: '你选择了取消',
            })
        })
}
</script>

<template>
    <el-button :plain="true" @click="open1">消息</el-button>
    <el-button :plain="true" @click="open2">成功</el-button>
    <el-button :plain="true" @click="open3">警告</el-button>
    <el-button :plain="true" @click="open4">错误</el-button>
    <hr />
    <el-button plain @click="openBox">点击打开消息提示对话框</el-button>
    <el-button plain @click="openConfirmBox">点击打开消息确认对话框</el-button>
</template>