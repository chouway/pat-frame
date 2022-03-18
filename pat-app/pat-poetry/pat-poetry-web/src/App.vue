<template>
  <el-header>
    <el-image :src="require('./assets/logo.png')" style="margin-top:-40px;"></el-image>
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
    go2baish(){
      this.$router.push('/baish')
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
  margin-top: 60px;
}
</style>
