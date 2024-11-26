import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],

  server:{
    //host:'0.0.0.0' ,//ip地址,可能会造成混乱,不建议设置成 0,0,0,0
    port: 80, // 设置服务启动端口号,浏览器默认端口号就是80
    open: true, // 设置服务启动时是否自动打开浏览器
  }
})
