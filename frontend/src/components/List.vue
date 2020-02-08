
<template>
    <div id="list">
        <!-- p id="demo">我的第一个段落</p -->
        <vue-headful title="智能家居" />

        <el-input v-model="entityName" placeholder="请输入设备名称"></el-input>
        <el-button type="primary" @click="getAttributes()">获取设备属性</el-button>

        <el-checkbox-group v-model="checkList">
            <el-checkbox v-for="attr in attributes" :label="attr"></el-checkbox>
        </el-checkbox-group>
        <el-button type="primary" @click="buildModel()">开始构建模型</el-button>

        <p id="buildProcess"></p>
        <img id="modelPng" src="http://img1.imgtn.bdimg.com/it/u=3982309790,3339526893&fm=26&gp=0.jpg">
        <p></p>

        <el-button type="primary" @click="checkData()">开始异常检测</el-button>
    </div>
</template>
<script>

import axios from 'axios';

export default {

    data: function() {
        return {
            entityName: '',
            attributes:[],
            checkList:[]
        }
    },

    created() { // 页面创建生命周期函数
          this.initWebSocket()
    },

    destroyed: function () { // 离开页面生命周期函数
        this.websocketclose();
    },

    methods: {

        check() {
            axios.get(this.GLOBAL.configip + 'start')
                .then(function(response) {
                    console.log(response);
                });

            setInterval(function() {
                document.getElementById('modelPng').src = "http://img5.imgtn.bdimg.com/it/u=3670224609,2460418974&fm=26&gp=0.jpg";
            }, 1000);
        },

        getAttributes() {
            var _this = this;
            axios.get(this.GLOBAL.configip + 'getAttributes?entityName=' + _this.entityName)
                .then(function(response) {
                    _this.attributes = response.data.split(",");
                });
        },

        buildModel() {
            var _this = this;
            document.getElementById('buildProcess').innerHTML = '建模中';
            axios.get(this.GLOBAL.configip + 'buildModel?checkList=' + _this.checkList)
                .then(function(response) {
                    //document.getElementById('buildProcess').innerHTML = '建模完成';
                    //document.getElementById('modelPng').src = response.data;
                });
        },

        checkData() {
            axios.get(this.GLOBAL.configip + 'checkData')
                .then(function(response) {

                });
        },

        initWebSocket: function () {
            this.websock = new WebSocket("ws://localhost:8080/websocket");
            this.websock.onopen = this.websocketonopen;
            this.websock.onerror = this.websocketonerror;
            this.websock.onmessage = this.websocketonmessage;
            this.websock.onclose = this.websocketclose;
        },

        websocketonopen: function () {
            console.log("WebSocket连接成功");
        },

        websocketonerror: function (e) {
            console.log("WebSocket连接发生错误");
        },

        websocketonmessage: function (e) {
           console.log(e.data);
           document.getElementById('modelPng').src = e.data;
        },

        websocketclose: function (e) {
            console.log("connection closed (" + e.code + ")");
        }
    },
    mounted() {

    },
}
</script>
<style type="text/css">
    #result{
        margin-top: 20px;
        display: block;
        width: 600px;
    }

</style>