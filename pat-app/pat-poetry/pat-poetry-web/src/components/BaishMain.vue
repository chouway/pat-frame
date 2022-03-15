<template>
  <el-row justify="center">
    <el-col :span="5">
      <div class="poet-autocomplete">

      <el-autocomplete  :fetch-suggestions="suggestAsync" v-model.lazy="searchKey" placeholder="中华古典文集" size="large"
                       :hide-loading="true" class="input-with-select" maxlength="100" :suffix-icon="suffixIcon" clearable="true">
        <template #append><el-button @click="searchAsync">搜索</el-button></template>
      </el-autocomplete>
      </div>
    </el-col>

  </el-row>
    <div  v-show="poetResult.total>0">


          <el-row style="margin-top:40px" justify="center" :gutter="30">
            <el-col v-for="info in poetResult.poetInfoBOs" :key="'i'+info.id" :span="5" style="margin:12px">
                <el-card class="box-card" size="large" style="padding:2px;" >
                  <template #header>
                    <div class="card-header">
                      <span>《 {{ info.title }} 》</span>
                      <el-button class="button" type="text">{{ info.author }}</el-button>
                    </div>
                  </template>
                    <el-scrollbar height="350px">
                      <div v-for="(p,index) in info.paragraphs" :key="'p'+index" class="text item" style="margin:10px 0px">{{ p }}</div>
                  </el-scrollbar>
                </el-card>
          </el-col>
        </el-row>

        <el-row justify="center" style="margin-top:30px">
          <el-pagination :background="true" layout="prev, pager, next" :total="poetResult.total" :page-sizes="10" :hide-on-single-page="true" @current-change="handleCurrentChange">
          </el-pagination>
        </el-row>

    </div>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {ref,reactive,watch} from 'vue'

import axios from 'axios'

const pageNum = ref(1);
const pageSize = ref(8);
//搜索关键字
const searchKey = ref("");
//输入框后缀图标
const suffixIcon = ref("search");

//搜索结果主体
const poetResult = reactive({
  total:0,
  pageSize:10,
  pageNum:1,
  propKeys:[],
  poetAggsBO:[],
  poetInfoBOs:[]
})
//监测searchKey变化
watch(searchKey,() =>{
  if(searchKey.value.length==0){
    suffixIcon.value = 'search'
  }else{
    suffixIcon.value = ''
  }
  searchAsync();
})

//后台访问  搜索
const searchAsync = () => {
  console.info("searchKey=="+searchKey.value)
  axios.post("/api/poet/es/search",{key:searchKey.value,size:pageSize.value,pageNum:pageNum.value})
      .then(
          (res) => {
            if(res.data.success){
                  console.info("success")
                poetResult.total = res.data.info.total;
              if (res.data.info.poetInfoBOs) {
                 poetResult.poetInfoBOs = res.data.info.poetInfoBOs;
              }else{
                poetResult.poetInfoBOs = [];
              }
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
}
//后台访问  推荐词
const suggestAsync = (queryString,cb) =>{
  axios.post("/api/poet/es/suggest",{keyword:queryString})
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
      (err) => {
        ElMessage.error("server error:"+ err);
      }
  )
}
//
const handleCurrentChange = (val) => {
  pageSize.value = 8;
  pageNum.value = val;
  searchAsync();
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
    height: 60px;
    font-size: 20px;
  }

</style>