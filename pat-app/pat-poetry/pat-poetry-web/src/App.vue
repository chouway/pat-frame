<template>
  <el-header>
    <el-button @click="go2baish" title="中华古典文集">百诗</el-button>
    <el-image :src="require('./assets/logo.png')" size></el-image>
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
