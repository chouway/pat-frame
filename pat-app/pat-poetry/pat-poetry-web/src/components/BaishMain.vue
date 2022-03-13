<template>
  <el-row justify="center">
    <el-col :span="6">
      <el-input  v-model.lazy="searchKey" placeholder="中华古典文集" maxlength="100"  class="input-with-select"  :suffix-icon="suffixIcon" clearable="true">
        <template #append><el-button>搜索</el-button></template>
      </el-input>
    </el-col>


  </el-row>


</template>

<script>
export default {
  name: "BaishMain",
  data(){
    return {
      searchKey: "",
      suffixIcon: "search"
    }
  },
  watch:{
    searchKey(newVal){
        if(newVal.length==0){
          this.suffixIcon = 'search'
        }else{
          this.suffixIcon = ''
        }
    }
  }
}
</script>

<style scoped>

</style>