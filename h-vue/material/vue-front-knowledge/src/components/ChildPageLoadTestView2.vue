<!-- 用于测试子路由加载 -->
<template>
    <p>菜单栏加载子路由组件成功!</p>
    <p>该页面的路由地址是: {{ $route.path }}</p>
    <!-- 同理,使用 params 属性来获取rest风格网址中的参数信息 -->
    <p>传递到该页面的参数是: name= {{ $route.params.name }} ,message= {{ $route.params.message }}</p>
</template>
<style scoped>
p {
    color: blue
}
</style>