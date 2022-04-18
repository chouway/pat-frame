<template>

  <el-image :src="require('./assets/logo.png')" style="position: absolute;top:1%;left:11%;" title="百诗"></el-image>
  <div style="position: absolute;top:1%;left:14%;"><h2>百诗</h2></div>
  <el-button @click="go2SignUp" style="position:absolute;top:2%;right:10%;" v-show="loginShow">注册</el-button>
  <el-button type="primary" @click="go2Login" style="position:absolute;top:2%;right:14%;" v-show="loginShow">登录</el-button>
  <el-main style="margin-top: 30px;">
    <router-view v-wechat-title='$route.meta.title'>
    </router-view>
  </el-main>

</template>

<script setup>

import {ref,onMounted} from 'vue';
import { useRouter } from 'vue-router'
import { userStore } from "./store/info"


/**
 * 获取url参数
 * @returns {string}
 */
const urlSearchParams = function(){
  var urlSearchParams = "";
  var index_0 = window.location.href.indexOf("?");
  if(index_0>0){
    urlSearchParams = window.location.href.substring(index_0);
  }
  var index_1 = urlSearchParams.lastIndexOf("#");
  if(index_1>0){
    urlSearchParams = urlSearchParams.substring(0,index_1);
  }
  return new URLSearchParams(urlSearchParams);
}
const store = userStore(); //用户登录信息
const loginShow = ref(true);//登录 注册 是否 展示
const code = ref(urlSearchParams().get("code"));//code码
const isRefresh = ref(false);//是否重刷token  (token 12小时有效  refresh token 30天有效)
if(code.value){
  loginShow.value = false;
}else{
  if(store.$state.validTs!=-1){
    if(store.$state.validTs>new Date().getTime()){//token有效
      loginShow.value = false;
    }else {//刷新token
      isRefresh.value = true;
    }
  }
}


const router = useRouter()
onMounted(() => {
  //code换取token
  if(code.value){
     store.setCode(code.value);
  }else if(isRefresh.value){
    store.refresh();
  }else{
     router.push('/baish')
  }
})

const go2SignUp = function(){//注册
      window.location.href="http://127.0.0.1:18081/signUp";
    }
const go2Login = function(){//登录
      window.location.href="http://127.0.0.1:18081/oauth/authorize?response_type=code&client_id=poetry-web-dev&redirect_uri=http%3A%2F%2F127.0.0.1%3A8080&scope=server";
}


</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
