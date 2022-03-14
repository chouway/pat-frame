<template>
  <el-row justify="center">
    <el-col :span="6">
      <div class="poet-autocomplete">

      <el-autocomplete  :fetch-suggestions="querySearchAsync" v-model.lazy="searchKey" placeholder="中华古典文集"  size="large"
                        :hide-loading="true" class="input-with-select" maxlength="100"  :suffix-icon="suffixIcon" clearable="true">
        <template #append><el-button>搜索</el-button></template>
      </el-autocomplete>
      </div>
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
  methods:{
    querySearchAsync(searchKey,cb){
        console.info("searchKey="+searchKey);
      this.$http.post("/api/poet/es/suggest",{keyword:this.searchKey})
      .then(
          (res) => {
            console.info("data"+ res);
            if(res.data.success){
              var  results = [];
              for (let i = 0; i < res.data.info.length; i++) {
                   results.push({value:res.data.info[i].keyword});
              }
              cb(results);
            }else{
              console.error(res.data.message);
            }
          }
      ).catch(
          (error) => {
          console.error("error" + error);
        }
      )

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

  .poet-autocomplete >>> .el-autocomplete{
    width:100% ;

  }
  .poet-autocomplete >>> .el-autocomplete-suggestion{
    width:100% ;
  }

  .poet-autocomplete >>> .el-input__inner{
    height: 50px;
    font-size: 18px;
  }
</style>