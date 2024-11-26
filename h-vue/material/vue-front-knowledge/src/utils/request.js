import axios from "axios";//aixos作用类似于ajax,可以向后端发送数据

//export 表示导出该方法,使得可以在其他页面引用该方法.export也可以用于导出变量,引用时变量命名必须与导出时相同
//本方法具有返回值,必须加return.get方法没有请求体,params请求参数会被写入url地址的后面
export const doGet = (url, params) => {
    return axios({
        method: 'get',
        url: url,
        params: params
    })
}
//post方法中data数据会被写入其请求体,也可以像get方法那样添加params请求参数写在url地址后面但没必要
export const doPost = (url, data) => {
    return axios({
        method: 'post',
        // 用该方法设置请求头参数, axios 中 Content-Type 属性默认值为 application/json ,如果有需要可以设置成以下类型
        // 该类型声明请求体使用传统的格式来发送ajax请求,这样后端的方法形参可以无需使用任何形参注解或使用
        // @RequestParam() 形参注解获取请求体内的参数
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}, 
        url: url,
        data: data
    })
}