<script setup>
import { onBeforeMount, onBeforeUnmount, onBeforeUpdate, onMounted, onUnmounted, onUpdated, ref, inject } from "vue";

onBeforeMount(() => { //比onMounted()方法执行得早些
    console.log('onBeforeMount方法已执行')
})

//最常用的方法,在刚开始加载页面元素时执行,只执行一次,一般用来执行从后端获取用户数据然后加载它的方法
onMounted(() => {
    console.log('onMounted方法已执行')
    alert('onMounted: 这是页面元素创建后弹出的对话框')
})

onBeforeUpdate(() => { //比onUpdated()方法执行得早些
    console.log('onBeforeUpdate方法已执行')
})
//页面某元素内容有变化时会执行的方法,只要变化就执行,可能执行很多次,若本页面的某变量发生改变而页面元素没有改变则不会执行该方法
onUpdated(() => {
    console.log('onUpdated方法已执行')
    alert('onUpdated: 这是页面元素被改写后弹出的对话框')
})

onBeforeUnmount(() => { //比onUnmounted()方法执行得早些
    console.log('onBeforeUnmount方法已执行')
})

onUnmounted(() => { //当离开该页面(页面元素销毁后)执行的方法,只执行一次
    console.log('onUnmounted方法已执行')
    alert('这是页面元素销毁后弹出的对话框')
})

let divText = ref("这是本页面div标签元素中的文字") //与div双向绑定的文字
let switchFlag = ref(true) //切换文字的标志变量
const divTextSwitch = () => { //按钮绑定的切换文字的方法
    if (switchFlag.value) {
        divText.value = "你看到了我,看到了我"
        switchFlag.value = false

    }
    else {
        divText.value = "是哪一种颜色,悲伤或快乐"
        switchFlag.value = true
    }
}
// inject() 函数需从 vue 组件中声明导入,该方法导入了父页面刷新子页面的方法 reloadPage() ,被声明为按钮的 @click 方法
let flushPage = inject('flush')
</script>

<template>
    <div>{{ divText }}</div>
    <hr />
    <el-button plain @click="divTextSwitch">点击修改上方的文字</el-button>
    <hr />
    <el-button plain @click="flushPage">点击这里刷新本页面</el-button>
</template>

<style scoped>
div {
    color: green
}

.el-button {
    width: 100%;
    background-color: rgb(231, 192, 102);
    font-size: large;
}
</style>