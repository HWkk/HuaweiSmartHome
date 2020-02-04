<template>
    <div id="app">
        <div class="header">
            <div class="content">
                <router-link to="/">智能家居</router-link>
                <!-- <span class="admin"><router-link to="/automation">已有规则列表</router-link></span> -->
       <!--          <span class="title automation" @click="gotoAutomation()">所有规则列表</span>
                <span class="title manage" @click="gotoManage()">规则管理</span> -->
            </div>
        </div>
        <div class="container">
            <router-view></router-view>
            <div class="footer">
                <span>Copyright &copy; 2020 Institute of Software</span>
            </div>
        </div>
    </div>
</template>
<script>
import '../public/js/prism.js';

export default {
    name: 'app',
    data: function() {
        return {

        }
    },
    methods: {
        gotoAutomation() {
            this.$router.push({ path: '/automation' });
        },
        gotoManage() {
            this.$router.push({ path: '/manage' });
        }
    }
}
</script>
<style>
@import "../public/css/prism.css";
@import "../public/css/fontello.css";

html,
body,
#app {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    position: relative;
}

.header {
    margin: 0 0 10px;
    background-color: #fafbfc;
    border-bottom: 1px solid #e1e4e8;
}

.header .content {
    display: block;
    max-width: 1100px;
    margin: 0 auto;
    overflow: hidden;
}


.content span {
    display: block;
    float: left;
    height: 60px;
    line-height: 60px;
    margin: 0;
    /*color: #909399;*/
}


.header a {
    display: block;
    float: left;
    color: #000;
    font-size: 1.5rem;
    text-decoration: none;
    margin-left: 10px;
    height: 60px;
    line-height: 60px;
}

/*----------------*/

.container {
    position: relative;
    min-width: 800px;
    margin: 0 50px;
    padding-bottom: 30px;
}

.footer {
    width: 100%;
    border-top: 1px #e1e4e8 solid;
    text-align: center;
    padding-top: 20px;
    margin: 30px 0 10px;
}

.footer span {
    text-align: center;
}

.admin a {
    display: inline-block;
    float: right;
    margin-right: 10px;
    font-size: 1rem;
}

.header .title {
    display: inline-block;
    margin-left: 20px;
    font-size: 1rem;
    cursor: pointer;
}


.page-title {
    height: 40px;
    overflow: hidden;
    margin: 0 10px 5px;
}

.page-title span {
    font-size: 1.1rem;
    height: 40px;
    line-height: 40px;
}
</style>