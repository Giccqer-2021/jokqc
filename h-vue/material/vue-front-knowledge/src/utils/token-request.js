import axios from "axios";

export const TOKEN_NAME = 'Authorization'// 要写入请求头的属性名(token),同时也是写入本地存储或会话存储中的属性名(token)
axios.defaults.baseURL = 'http://localhost:8080/token';//可以写入具体的url,使得使用本页面的方法填写url时可以省略一部分

axios.interceptors.request.use((config) => { //请求拦截器,在发送请求之前做些什么,使用本页面任意导出的方法时会生效
    //会话存储,在用户关闭浏览器窗口或选项卡后就会消失,存储时间较短
    let token = window.sessionStorage.getItem(TOKEN_NAME)
    if (!token) { //如果在会话存储中未找到token
        token = window.localStorage.getItem(TOKEN_NAME)//本地存储,数据在浏览器关闭后仍然保留，直到被明确删除,存储时间较长
    }
    if (token) { //如果token不为空
        config.headers[TOKEN_NAME] = token //将token写入请求头
        // config.headers.Authorization = token //另一种写法
    }
    return config;
}, (error) => {
    return Promise.reject(error); //对请求错误做些什么,固定写法不要动
});
//响应拦截器,2xx 范围内的状态码都会触发该函数,response为响应数据,对使用本页面任意导出的方法后返回的响应体都生效
axios.interceptors.response.use((response) => {
    return response;
}, (error) => { //超出 2xx 范围的状态码都会触发该函数,对响应错误做点什么
    //输出错误状态码和从后端返回的错误消息,只有错误状态码为401或402时后端才会返回消息(即message不为空)
    alert('错误状态码: ' + error.response.status + ' 错误消息: ' + error.response.data.message)
    return Promise.reject(error); //同理,固定写法
});

export const tokenRequest = (url) => { //要导出的操作方法
    return axios({
        method: 'get',
        url: url,
    })
}