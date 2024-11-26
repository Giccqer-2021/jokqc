<script setup>
import { ref } from "vue";//导入 ref 函数

let user = ref({ //测试form表单中的账号密码与一个对象的绑定,这些数据会以json形式发送给后端
    userAccount: 'banana', //一般账号密码默认为空
    userPassword: '123456',
    rememberMe: true
})

const submitMethod = () => {//为提交按钮绑定一个提交事件,通常为将这些数据传递给指定url后端的方法
    alert('账号:' + user.value.userAccount + "---" + '密码:' + user.value.userPassword)
}

let loginRules = ref({//为表单控件的账号密码输入行制定输入规则,ref绑定一个对象
    userAccount: [  //对像名要与user中的属性,prop标签属性一致
        //required:true表明不能为空否则输出message信息,trigger:'blur'表明该事件会在输入框失去焦点时检测一次
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    userPassword: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 16, message: '密码长度为6-16位', trigger: 'blur' }//输入框的最大最小长度,不满足条件则输出message
    ]
})

let formRef = ref({}) //提交规则认证结果的对象,写入空属性对象即可
const submitByRules = (result)=>{ //新的表单提交方法
    if(!result) return //如果结果为空,即没有给提交方法传递参数则直接返回
    result.validate((valid) =>{
        if(valid){ //valid为true时表示表单规则验证通过
            submitMethod() //此方法定义见上,通常为提交前端数据到后端的方法
        }else{ //valid为false时表示用户存在非法输入
            alert('请输入符合条件的账号密码')
        }
    })
}
</script>

<template>
    <!-- element-plus的 el-form 表单,可从网上寻找并下载样式 -->
    <!-- label-width:宽度,px表示像素,style表示html标签的原始属性 -->
    <!-- :model相当于 v-bind:model,表示该标签与user对象绑定.:rules同理,二者一起使用与规则对象loginRules绑定用来制定表单填入规则 -->
    <!-- 提交时对表单结果进行规则认证:完成上述规则绑定后,添加 ref 属性,为其绑定一个内部无属性的对象,并在提交按钮的@click方法引用该对象-->
    <el-form label-width="60px" style="" :model="user" :rules="loginRules" ref="formRef">
        <!-- 表单控件子标签,prop属性与:model,rule属性配合来指明以哪个变量为准制定规则,porp属性要写入对应的变量 -->
        <el-form-item label="账号" prop="userAccount">
            <!-- v-model与:model不同,该属性专用于input,button等子标签的属性绑定,这里获取属性不要加 .value -->
            <el-input v-model="user.userAccount" />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword">
            <el-input type="password" v-model="user.userPassword" /><!-- type="password"密码表单 -->
        </el-form-item>
        <el-form-item>
            <!-- @click相当于 v-on:click,这里绑定了submitMethod方法,与bind不同的是v-on用于绑定监听事件 -->
            <!-- (把上面写的东西划掉!)现在改为 submitByRules(formRef) 方法了,传入了一个提交规则认证结果的对象参数 -->
            <el-button type="primary" @click="submitByRules(formRef)">登 录</el-button><!-- type="primary"提交按钮 -->
        </el-form-item>
        <el-form-item>
            <!-- 复选框,绑定的变量为boolean类型 -->
            <el-checkbox label="记住我" size="large" v-model="user.rememberMe" />
        </el-form-item>
    </el-form>
</template>
<style scoped>
.el-button {
    /* 以下内容会影响本页面所有按钮组件的样式 */
    width: 100%;
    background-color: chocolate;
    font-size: large;
}
</style>