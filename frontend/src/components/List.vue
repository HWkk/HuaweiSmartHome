
<template>
    <div id="list">
        <vue-headful title="智能家居" />
        <el-input v-model="entity_name" placeholder="请输入设备名称"></el-input>
        <el-button type="primary" @click="get_attributes()">获取设备属性</el-button>
        <img id="result" src="http://img1.imgtn.bdimg.com/it/u=3982309790,3339526893&fm=26&gp=0.jpg">    
    </div>
</template>
<script>
import axios from 'axios';

export default {

    data: function() {
        return {
            entity_name: ''
        }
    },
    methods: {

        check() {
            var _this = this;
            axios.get(this.GLOBAL.configip + 'start')
                .then(function(response) {
                    console.log(response);
                });

            setInterval(function() {
                document.getElementById('result').src = "http://img5.imgtn.bdimg.com/it/u=3670224609,2460418974&fm=26&gp=0.jpg";
            }, 1000);
        },

        get_attributes() {
            var _this = this;
            axios.get(this.GLOBAL.configip + 'get_attributes?entity_name=' + _this.entity_name)
                .then(function(response) {
                    console.log(response);
                });
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