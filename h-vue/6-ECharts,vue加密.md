# ECharts,vue加密

## [ECharts](https://echarts.apache.org/zh/index.html) 

1. cmd输入 npm i echarts 安装 ECharts 模块,使用 ECharts 模块必须要在 <script> 标签中声明导入.为了使图表在页面初始化时被成功加载,还需要导入 vue 的生命钩子函数 onMounted
   ```javascript
   import { onMounted } from 'vue';
   import * as echarts from 'echarts';
   ```

2. 在 [ECharts样例官网](https://echarts.apache.org/examples/zh/index.html) 中复制粘贴相关的js代码,将代码封装入一个方法中,这里以柱状图为例,在 [EChartsTest.vue](material\vue-front-knowledge\src\components\EChartsTest.vue) 文件中,除了对 option 对象的属性定义不同外,其他语句基本是固定写法
   ```javascript
   const loadStatisticalChart = () => { //加载折线图的方法,封装于此
     let chartDom = document.getElementById('statisticalChart'); //获取 id 为 statisticalChart 的自定义标签元素
     let myChart = echarts.init(chartDom); //在div元素中初始化echarts实例
     let option = { //定义 option 对象,该对象决定了图表的具体样式,很长...
       title: {
         text: '折线图标题' //图像标题
       },
       tooltip: { //添加此属性时,用户将鼠标置于统计图内将会展示数据提示信息
         trigger: 'axis' //触发条件:鼠标鼠标置于轴线中时
       },
       legend: { //在图表中要显示的统计线段,要与下方 series 对象数组中的 name 属性相对应并赋予相应的数据
         data: ['蓝色', '绿色', '黄色', '红色', '青色']
       },
       grid: { //调整图像在所占用的 div 标签组件中的位置
         left: '3%', //图像左侧留白面积百分比
         right: '4%', //图像右侧留白面积百分比
         bottom: '3%',  //图像下方留白面积百分比
         containLabel: true //上述调整是否将横竖轴中的标签包含在内
       },
       toolbox: { //添加此属性将允许用户将图表作为图像下载
         feature: {
           saveAsImage: {}
         }
       },
       xAxis: {
         type: 'category',
         boundaryGap: false, //是否将X轴两点间居中的位置作为横轴数据点
         data: ['横轴1', '横轴2', '横轴3', '横轴4', '横轴5', '横轴6', '横轴7'] //X轴标记的数值
       },
       yAxis: {
         type: 'value'
       },
       series: [ //解释上方的 legend 对象内的 data 数组属性所对应的数据
         {
           name: '蓝色',
           type: 'line', //图表类型:线
           stack: 'Total',
           data: [120, 132, 101, 134, 90, 230, 210]
         },
         {
           name: '绿色',
           type: 'line',
           stack: 'Total',
           data: [220, 182, 191, 234, 290, 330, 310]
         },
         {
           name: '黄色',
           type: 'line',
           stack: 'Total',
           data: [150, 232, 201, 154, 190, 330, 410]
         },
         {
           name: '红色',
           type: 'line',
           stack: 'Total',
           data: [320, 332, 301, 334, 390, 330, 320]
         },
         {
           name: '青色',
           type: 'line',
           stack: 'Total',
           data: [820, 932, 901, 934, 1290, 1330, 1320]
         }
       ]
     };
     option && myChart.setOption(option); //使用option配置项渲染图表
   }
   ```

   将该图表元素置入 id=statisticalChart 的div标签中,该标签要事先定义好其长高等样式属性:
   ```html
   <div id="statisticalChart" style="width: 80%; height: 400px;">折线图会被加载到这里(文字会被替换)</div>
   ```

3. 在 <script> 标签中使用 onMounted 钩子函数触发图表加载,其中的空括号和箭头不能省否则报错:
   ```javascript
   onMounted(() => {
     loadStatisticalChart() //加载折线图
   })
   ```

4. 测试运行,查看图表样式是否被成功渲染

## vue加密

???
