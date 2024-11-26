<script setup>
import { ref } from 'vue';

let vTextMessage = ref('这个标签用来测试 v-text 语法')
let VHtmlMessage = ref('<span style="color:red">这个标签用来测试 v-html 语法</span>')
let showDiv = ref(true)
let flagNum = ref(1)
const changeFlagNum = () => {
    if (flagNum.value == 1) {
        flagNum.value = 2
    } else if (flagNum.value == 2) {
        flagNum.value = 3
    } else {
        flagNum.value = 1
    }
}
let messageList = ref(['此处用来测试 v-for 循环', '你好', '谢谢', '小笼包', '再见'])
</script>

<template>
    <!-- 标签文本内容会被 vTextMessage 变量的值替换,使用 v-text 属性时不可以自行在标签中写入文本 -->
    <div v-text="vTextMessage"></div>
    <!-- 可以不使用变量绑定而直接使用字符串声明,其中的字符串要加单引号 '' -->
    <div v-text="'这个标签不使用变量测试 v-text 语法'"></div>
    <!-- v-html 表示在该div中要插入的标签元素,因为易受到攻击不推荐使用-->
    <div v-html="VHtmlMessage"></div>
    <hr />
    <!-- 相当于css样式 display:none ,仅仅是隐藏标签而非销毁 -->
    <div v-show="showDiv">可被隐藏的标签,测试 v-show 语法</div>
    <button @click="showDiv = !showDiv">点击按钮隐藏或显示上方的标签</button>
    <hr />
    <!-- v-if 的值为 true 时会加载标签, false 时会销毁标签 -->
    <div v-if="flagNum == 1">v-if语句标签,标志数: {{ flagNum }} </div>
    <!-- v-else-if 必须配合v-if 属性使用 -->
    <div v-else-if="flagNum == 2">v-else-if语句标签,标志数: {{ flagNum }} </div>
    <!-- v-else必须配合 v-if 属性使用 -->
    <div v-else>v-else语句,标志数: {{ flagNum }} </div>
    <button @click="changeFlagNum">点击按钮切换上方标签内容</button>
    <hr />
    <!-- 测试 v-for 语句循环添加不同的 div 标签,其中 message 为遍历的 messageList 字符串数组中的单个字符串 -->
    <!-- index 为数组元素的序号(从0开始计数) -->
    <div v-for="(message, index) in messageList" :id="'VFD' + index">
        {{ message }}
    </div>
</template>

<style scoped>
#VFD2 { /* id=VFD2 的div标签使用该样式("谢谢") */
    background-color: aquamarine;
}
</style>