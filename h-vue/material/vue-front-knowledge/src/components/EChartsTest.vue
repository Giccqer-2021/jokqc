<script setup>
import { onMounted } from 'vue';
import * as echarts from 'echarts';

//注意:这个空括号和箭头不能省否则报错
onMounted(() => { //在刚开始加载页面元素时执行,vue生命周期函数钩子,这里用来加载 ECharts 图表
  loadStatisticalChart() //加载折线图
  loadPieChart() //加载饼图
})

//以下大部分为固定写法,从网上照抄便是
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

const loadPieChart = () => { //加载饼状图的方法,封装于此
  let chartDom = document.getElementById('pieChart'); //获取 id 为 pieChart 的自定义标签元素
  let myChart = echarts.init(chartDom);
  let option = {
    title: {
      text: '饼状图标题',
      subtext: '副标题',
      left: 'center' //标题位置
    },
    tooltip: {
      trigger: 'item' //触发条件:鼠标鼠标置于单个饼图中
    },
    legend: { //额外信息
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '鼠标置入图内时的提示信息',
        type: 'pie', //样式:饼状图
        radius: '50%', //其半径占整个标签的比值
        data: [
          { value: 1048, name: '蓝色' },
          { value: 735, name: '绿色' },
          { value: 580, name: '黄色' },
          { value: 484, name: '红色' },
          { value: 300, name: '青色' }
        ],
        emphasis: { //高亮的标签和图形样式
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  option && myChart.setOption(option);
}
</script>

<template>
  <!-- 折线图堆叠,id值要与上方代码对应 -->
  <div id="statisticalChart" style="width: 80%; height: 400px;">折线图会被加载到这里(文字会被替换)</div>
  <hr />
  <!-- 饼状图 -->
  <div id="pieChart" style="width: 80%; height: 400px;">饼图会被加载到这里(文字会被替换)</div>
</template>

<style scoped></style>