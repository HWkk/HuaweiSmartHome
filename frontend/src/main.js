import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import VueRouter from 'vue-router'
import routeConfig from './router-config'
import config_ from './components/config.vue'

import 'element-ui/lib/theme-chalk/index.css'

import vueHeadful from 'vue-headful';


import ECharts from 'vue-echarts' // refers to components/ECharts.vue in webpack

// import ECharts modules manually to reduce bundle size
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'

import echarts from "echarts";
Vue.prototype.$echarts = echarts;

Vue.component('vue-headful', vueHeadful);

Vue.component('v-chart', ECharts)



Vue.prototype.GLOBAL = config_

Vue.use(ElementUI)
Vue.use(VueRouter)

Vue.config.productionTip = false
const router = new VueRouter({
    routes: routeConfig
})

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')