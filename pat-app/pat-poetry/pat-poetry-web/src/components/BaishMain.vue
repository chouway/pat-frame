<template>
  <el-row justify="center">
    <el-col :span="5">
      <div class="poet-autocomplete">

      <el-autocomplete :fetch-suggestions="suggestAsync" v-model.lazy="searchKey" placeholder="中华古典文集" size="large"
                       :hide-loading="true" class="input-with-select" maxlength="100" :suffix-icon="suffixIcon" clearable="true">
        <template #append><el-button @click="searchAsync">搜索</el-button></template>
      </el-autocomplete>
      </div>
    </el-col>

  </el-row>

    <div  v-show="poetSearchResultBOs.total>0">

        <el-row style="margin-top:40px" justify="center" :gutter="30">
          <el-col v-for="info in poetInfoBOs" :key="'i'+info.id" :span="4">
            <el-card class="box-card" size="large" style="padding:5px">
              <template #header>
                <div class="card-header">
                  <span>Card name</span>
                  <el-button class="button" type="text">{{ info.title }}</el-button>
                </div>
              </template>
              <div v-for="(p,index) in info.paragraphs" :key="'p'+index" class="text item">{{ p }}</div>
            </el-card>
          </el-col>
        </el-row>

        <el-row justify="center" style="margin-top:30px">
          <el-pagination background layout="prev, pager, next" :total="1000" :page-sizes="10" :hide-on-single-page="true">
          </el-pagination>
        </el-row>

    </div>
</template>

<script>
import {ElMessage} from "element-plus";
export default {

  name: "BaishMain",
  data(){
    return {
      searchKey: "",
      suffixIcon: "search",
      poetSearchResultBOs:{
        total:0,
        pageSize:10,
        pageNum:1,
        propKeys:[],
        poetAggsBO:[],
        poetInfoBOs:[]
      }
    }
  },
  mounted() {
    this.$nextTick(function () {
        this.searchAsync();

    })
  },
  methods:{
    searchAsync(){
      let that = this;
      this.$http.post("/api/poet/es/search",{keyword:this.searchKey})
          .then(
              (res) => {
                console.info(that.poetSearchResultBOs.total);
                console.info("data"+ res);
                if(res.data.success){
                  console.info(that);
                  that.$set(that.poetSearchResultBOs.total, 1);
                  console.info(that.poetSearchResultBOs.total);
                }else{
                  ElMessage.warning(res.data.message);
                }
              }
          ).catch(
          (err) => {
            console.error("err"+err);
            ElMessage.error("server error");
          }
      )
    },
    suggestAsync(searchKey, cb){
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
              ElMessage.warning(res.data.message);
            }
          }
      ).catch(
          () => {
            ElMessage.error("server error");
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