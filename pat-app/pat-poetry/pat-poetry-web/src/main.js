import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import BaishMain from './components/BaishMain'
import {createRouter,createWebHashHistory} from 'vue-router'
import axios from 'axios'
import VueWechatTitle from 'vue-wechat-title'//动态修改title
import {useCookies} from 'vue3-cookies'
import store from "./store";

import * as ElIconModules from '@element-plus/icons-vue'

//https://router.vuejs.org/zh/guide/#router-link
const routes = [
    { path: '/baish', component: BaishMain , meta: {
            title: '百诗'
        }}
]
const router = createRouter({
    history: createWebHashHistory(),  // 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    routes, // `routes: routes` 的缩写
})
// https://element-plus.gitee.io/zh-CN/guide/quickstart.html element-plus 完整引入代码
router.beforeEach((to,from,next)=>{
    if(to.meta.title) {
        document.title=to.meta.title
        const {cookies} = useCookies();
        cookies.set('title',document.title)
    }
    next()
})
const app = createApp(App)
Object.keys(ElIconModules).forEach(function(key) {
    app.component(ElIconModules[key].name, ElIconModules[key])
})
app.config.globalProperties.$http = axios
// app.config.globalProperties.$cookies = VueCookies
app.use(router)
app.use(VueWechatTitle)
app.use(ElementPlus,{size:'large'})
app.use(store)
app.mount('#app')


