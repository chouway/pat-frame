<template>
  <el-header>

        <el-image :src="require('./assets/logo.png')" style="position: absolute;top:1%;left:11%;" title="百诗"></el-image>
        <div style="position: absolute;top:1%;left:14%;"><h2>百诗</h2></div>
        <el-button @click="go2SignUp" style="position:absolute;top:2%;right:10%;">注册</el-button>
        <el-button type="primary" @click="go2Login" style="position:absolute;top:2%;right:14%;" >登录</el-button>
  </el-header>
  <el-main>
    <router-view v-wechat-title='$route.meta.title'>
    </router-view>
  </el-main>

</template>

<script>
export default {
  name: 'App',
  mounted() {
    this.$router.push('/baish')
  },
  methods:{
    go2SignUp(){//注册
      window.location.href="http://127.0.0.1:18081/signUp";
    },
    go2Login(){//登录
      window.location.href="http://127.0.0.1:18081/oauth/authorize?response_type=code&client_id=poetry-web-dev&redirect_uri=http%3A%2F%2F127.0.0.1%3A8080&scope=server";
    }
  }

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
