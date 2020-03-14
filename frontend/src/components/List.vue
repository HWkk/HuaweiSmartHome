
<template>
    <div id="list">
        <!-- p id="demo">我的第一个段落</p-->
        <vue-headful title="智能家居建模与应用系统" />

        <el-input v-model="entityName" placeholder="请输入设备名称"></el-input>
        <el-button type="primary" @click="getAttributes()">获取过滤后的设备属性</el-button>

        <el-checkbox-group v-model="checkList">
            <el-checkbox v-for="attr in attributes" :label="attr"></el-checkbox>
        </el-checkbox-group>
        <el-button type="primary" @click="buildModel()" v-if="showBuildModelButton">开始构建模型</el-button>

        <p id="buildProcess"></p>
        <img id="modelPng" src="" height="300" width="500" v-if="showModelPng">

        <p></p>
        <el-button type="primary" @click="showRelation()" v-if="showUseModelButton">开启模型应用功能</el-button>

        <p id="relationText"></p>
        <div v-for="(attr, index) in filterAttributes" v-if="showConfirmButton">
            <p>{{ attr }}:</p>
            <el-input v-model="relationList[index]" placeholder="请输入可能有关系的设备硬件"></el-input>
        </div>
        <el-button type="primary" @click="putRelationAndStart()" v-if="showConfirmButton">确定</el-button>
    </div>
</template>
<script>

import axios from 'axios';

export default {

    data: function() {
        return {
            entityName: '',
            attributes:[],
            checkList:[],
            showBuildModelButton:false,
            showModelPng:false,
            showUseModelButton:false,
            filterAttributes:[],
            relationList:[],
            showConfirmButton:false
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
                    _this.showBuildModelButton = true;
                });
        },

        buildModel() {
            var _this = this;
            document.getElementById('buildProcess').innerHTML = '建模中,模型实时变化图如下:';
            axios.get(this.GLOBAL.configip + 'buildModel?checkList=' + _this.checkList)
                .then(function(response) {
                });
        },

        showRelation() {
            var _this = this;
            axios.get(this.GLOBAL.configip + 'getFilterAttributes?entityName=' + _this.entityName)
                .then(function(response) {
                    _this.filterAttributes = response.data.split(",");
                    document.getElementById('relationText').innerHTML = '请输入可能与对应属性有关的设备硬件:';
                    _this.showConfirmButton = true;
                });
        },

        putRelationAndStart() {
            var _this = this;
            console.log(_this.relationList);
            axios.get(this.GLOBAL.configip + 'putRelationAndStart?relationList=' + _this.relationList)
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

        websocketonmessage: function (message) {
            console.log(message.data);
            var _this = this;
            //document.getElementById('demo').innerHTML = message.data;
            if(message.data == 'FinishModel') {
                document.getElementById('buildProcess').innerHTML = '建模完成,模型展示如下:';
                _this.showUseModelButton = true;
            } else {
                if(!_this.showModelPng)
                    _this.showModelPng = true;
                document.getElementById('modelPng').src = message.data;
            }
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

   .el-checkbox {
       display: block !important;
       //margin-top: 10px !important;
   }
</style>