import { createRouter, createWebHistory } from "vue-router";//本页面大部分代码为行内通用写法,照抄便是
const Router = createRouter({
    history: createWebHistory(),
    routes: [ //对象数组,path表示映射的路径名,component:() => import('网页地址')回调方法内写入其映射的路径位置
        {
            path: '/', //根路由,首次访问时的欢迎页面,默认加载路径就是 /
            component: () => import('../views/WelcomeView.vue')
        },
        {
            path: '/main_page', //登陆成功后的主要操作页面
            component: () => import('../views/MainView.vue'),
            children: [ //子路由配置,对象数组
                {
                    path: 'child_page_load', //前面不可以加斜杠,需输入 /main_page/child_page_load 加载该组件
                    component: () => import('../components/ChildPageLoadTestView.vue'),
                    name: 'ChildPage' //name属性,可以用适当的方法引用该属性从而实现页面跳转,该属性的值必须唯一
                },
                {   //需输入/main_page/child_page_load2加载,这里加:冒号表示其内容为rest风格网址所传递的参数而并非是真正的地址
                    path: 'child_page_load2/:name/:message',
                    component: () => import('../components/ChildPageLoadTestView2.vue')
                },
                {   //测试一个组件内存在多个子路由出口的情况,加载多个子路由
                    path: 'multi_children_pages_test/:name/:message',
                    component: () => import('../components/MultiChildrenPagesTest.vue'),
                    children: [{ //子路由页面中的子路由组件
                        path: '', //对子路由来说,path为空的路径就是默认路径，注意：如果不写path属性则会全局报错
                        components: { //使用 components 属性时就不要使用 component 属性了,这里配置一组映射页面路径
                            // default 默认路由路径的出口就是 <router-view />
                            default: () => import('../components/ChildPageLoadTestView2.vue'),
                            // 使用 name 属性声明该页面的路由出口: <router-view name="second" />
                            second: () => import('../components/VueBindingTest.vue'),
                            // <router-view name="third" />
                            third: () => import('../components/ButtonIconTest.vue')
                        }
                    },
                    { //子子路由路径,必须同时定义页面三个出口所对应的组件,即使 default 对应的组件没有变化也要写上否则不显示组件
                        path: 'another_group',
                        components: {
                            default: () => import('../components/ChildPageLoadTestView2.vue'),
                            second: () => import('../components/FormTest.vue'),
                            third: () => import('../components/AxiosOuterTest.vue')
                        }
                    }]
                },
                {
                    path: 'vue_binding_test', //vue双向绑定语法测试模块
                    component: () => import('../components/VueBindingTest.vue')
                },
                {
                    path: 'button_icon_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/ButtonIconTest.vue')
                },
                {
                    path: 'form_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/FormTest.vue')
                },
                {
                    path: 'axios_outer_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/AxiosOuterTest.vue')
                },
                {
                    path: 'axios_spring_boot_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/AxiosSpringBootTest.vue')
                },
                {
                    path: 'message_box_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/MessageBoxTest.vue')
                },
                {
                    path: 'vue_life_cycle_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/VueLifeCycleTest.vue')
                },
                {
                    path: 'vue_instruction_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/VueInstructionTest.vue')
                },
                {
                    path: 'echarts_test', //Element Plus按钮与图标组件测试
                    component: () => import('../components/EChartsTest.vue')
                },
                {
                    path: 'up_and_down_load_flies', //Element Plus按钮与图标组件测试
                    component: () => import('../components/UpAndDownLoadFiles.vue')
                }
            ]
        }
    ]
})
//export default 表示导出该变量,与export不同,一个页面最多可以拥有一个export default,但引用时可以自行对导出内容命名
export default Router;