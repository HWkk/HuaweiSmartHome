<template>
    <div id="list">
        <vue-headful title="智能家居建模与应用系统" />
        <div>
            <div class="page-title">
                <span><i class="icon-doc-text"></i>1. 属性获取</span>
            </div>

            <el-input v-model="entityName" placeholder="请输入设备名称" style="width: 300px"></el-input>
            <el-button type="primary" @click="getAttributes()" style="margin-left: 20px">
                获取过滤后的设备属性
            </el-button>

            <el-checkbox-group v-model="checkList" style="margin: 10px auto">
                <el-checkbox v-for="attr in attributes" :label="attr" style="width: 20%"></el-checkbox>
            </el-checkbox-group>

            <el-button type="primary" @click="buildModel()" v-if="showBuildModelButton">开始构建模型</el-button>

        </div>
        <div style="margin: 20px auto">
            <div class="page-title" v-if="showTwo">
                <span><i class="icon-doc-text"></i>2. 模型构建</span>
            </div>

      
            <span v-text="buildProcessText" class="prompt"></span>
            <img v-if="showModelPng" id="modelPng" src="http://img5.imgtn.bdimg.com/it/u=3670224609,2460418974&fm=26&gp=0.jpg" style="height: 300px; display: block; margin: 15px auto">
            
            <el-button type="primary" @click="showRelation()" v-if="showUseModelButton">开启模型应用功能</el-button>

        </div>
        <div>
            <div class="page-title" v-if="showConfirmButton">
                <span><i class="icon-doc-text"></i>3. 输入属性与硬件的对应关系</span>
            </div>

            <div>
                <span id="relationText" class="prompt" v-if="showConfirmButton">请输入可能与对应属性有关的设备硬件(用于定位异常原因)</span>
            </div>

            <div v-if="showConfirmButton" style="margin-top: 10px">
                <el-row :gutter="20" v-for="(attr, index) in filterAttributes" style="margin-bottom: 10px">
                    <el-col :span="4">
                        <span style="height: 40px;line-height: 40px">{{ attr }}:</span>
                    </el-col>
                    <el-col :span="10">
                        <el-input v-model="relationList[index]" 
                            placeholder="请输入可能有关系的设备硬件">
                        </el-input>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="10" :offset="4">
                        <el-button type="primary" @click="putRelationAndStart()" v-if="showConfirmButton">
                            确定
                        </el-button>
                    </el-col> 
                </el-row> 
            </div>
        </div>

        <div>
            <div class="page-title" v-if="showAttrPng">
                <span><i class="icon-doc-text"></i>4. 属性数据展示</span>
            </div>
            <div v-for="modeAndAttr in modeAttrPngLoc" v-if="showAttrPng">
                <p>{{ modeAndAttr.mode }}模式的属性图:</p>
                <!-- gutter 同一行之间的间隔 -->
                <el-row :gutter="30" style="width: 90%; margin: 0 auto">
                    <el-col :span="8" v-for="pngLoc in modeAndAttr.attr" style="margin-bottom: 20px">
                        <img :src="pngLoc" style="width: 100%">
                    </el-col>
                </el-row>
            </div>
        </div>

        <div>
            <div class="page-title" v-if="showAttrPng">
                <span><i class="icon-doc-text"></i>5. 模型应用结果</span>
            </div>
            <div v-for="res in modelUseResult" v-if="showAttrPng">
                {{ res }}
            </div>
        </div>
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
            showTwo:false,
            showUseModelButton:false,
            filterAttributes:[],
            relationList:[],
            showConfirmButton:false,
            modeAttrPngLoc:[],
            showAttrPng:false,
            buildProcessText:'',
            modelUseResult:[]
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
            this.buildProcessText = '建模中,模型实时变化图如下:';
            this.showTwo = true;
            // document.getElementById('buildProcess').innerHTML = '建模中,模型实时变化图如下:';
            axios.get(this.GLOBAL.configip + 'buildModel?checkList=' + _this.checkList)
                .then(function(response) {
                });
        },

        showRelation() {
            var _this = this;
            axios.get(this.GLOBAL.configip + 'getFilterAttributes?entityName=' + _this.entityName)
                .then(function(response) {
                    _this.filterAttributes = response.data.split(",");
                    //document.getElementById('relationText').innerHTML = '请输入可能与对应属性有关的设备硬件(用于定位异常原因):';
                    if(!_this.showConfirmButton)
                        _this.showConfirmButton= true;
                });
        },

        putRelationAndStart() {
            var _this = this;
            console.log(_this.relationList);
            //document.getElementById('attrPngText').innerHTML = '分模式对属性数据进行展示:';
            this.showAttrPng = true;
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
            if(message.data.startsWith('M:')) {
                var m = message.data.substring(2);
                if(m == 'FinishModel') {
                    this.buildProcessText = '建模完成,模型展示如下:';
                    // document.getElementById('buildProcess').innerHTML = '建模完成,模型展示如下:';
                    _this.showUseModelButton = true;
                } else {
                    if(!_this.showModelPng)
                        _this.showModelPng = true;
                    document.getElementById('modelPng').src = m;
                }
            } else if(message.data.startsWith('C:')) {
                var m = message.data.substring(2);
                _this.modeAttrPngLoc = [];
                var splitMode = m.split("+");
                for(var i=0; i<splitMode.length; i++) {
                    var split = splitMode[i].split("[");
                    var modeAndAttr = {};
                    modeAndAttr.mode = split[0];
                    split[1] = split[1].substring(0, split[1].length - 1);
                    modeAndAttr.attr = split[1].split(",");
                    _this.modeAttrPngLoc.push(modeAndAttr);
                }
                console.log(_this.modeAttrPngLoc);
            } else {
                var m = message.data.substring(2);
                this.modelUseResult.push(m);
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
.el-checkbox {
    margin-top: 5px;
}

.el-checkbox+.el-checkbox {
    margin-left: 0px;
}

.prompt {
    margin-left: 15px;
}

.page-title {
    height: 40px;
    overflow: hidden;
    margin: 10px 0;
}

.page-title span {
    font-size: 1.1rem;
    height: 40px;
    line-height: 40px;
    font-weight: bold;

}
</style>