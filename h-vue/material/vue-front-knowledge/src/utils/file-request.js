import axios from "axios";

axios.defaults.baseURL = 'http://localhost:8080/storage';
/**
 * 将某文件上传至后端.
 * 
 * @param {FormData} files 将要上传的文件数据
 * @returns 待处理的axios对象
 */
export const uploadFiles = (files) => {
    return axios({
        method: 'post',
        url: '/upload', //由于el-upload组件的文件上传机制,即使不使用 /multi_upload 多文件上传方法也能进行多文件上传
        data: files
    })
}
/**
 * 从后端服务器中下载指定路径的文件.
 * 
 * @param {string} method 请求方式
 * @param {string} fileName 请求文件的完整路径
 * @param {string} modeNumber 使用后端的哪个个方法来下载文件(默认为第一个)
 * @returns 
 */
export const downloadFile = (method, fileName, modeNumber = '') => {
    //为了能让后端的 @PathVariable() 注解读取并拆分路径,这里将路径名与文件名改为用逗号,分隔
    let fileNameFormatting = fileName.replace("\\",",").replace("/",",")
    return axios({
        method: method,
        url: '/download' + modeNumber + '/' + fileNameFormatting
        // responseType: 'blob' //是否添加该参数并不影响数据下载
    })
}