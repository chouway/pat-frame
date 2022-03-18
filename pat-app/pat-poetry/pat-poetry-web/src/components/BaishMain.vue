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


    <div v-show="fullScreen" class="poet-fullScreen" style="margin-top:12px">
      <el-row style="margin-top:12px" justify="center" >
        <el-col :span="20">
          <el-card class="box-card" size="large" >
            <template #header>
              <div class="card-header">
                <span style="margin-left: 100px;;">《{{ targetCardRef.title }}》  {{ targetCardRef.author}}</span>
                <el-button type="text" @click="fullScreen=false" style="float:right;padding-bottom: 35px;"> <el-icon><Minus/></el-icon> </el-button>
                <el-button type="text" title="百科" style="float:right;padding-bottom: 35px;margin-right:10px;"> <el-icon><Document/></el-icon> </el-button>
              </div>
            </template>
            <el-scrollbar>
              <div v-for="(p,index) in targetCardRef.paragraphs" :key="'p'+index" class="text item" style="margin:8px 0px;padding:10px 0px;">{{ p }}</div>
            </el-scrollbar>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div v-show="poetResult.total==0&&!fullScreen" style="margin-top:12px">


      <el-row justify="center">

          <el-empty description="未找到"/>

      </el-row>
    </div>
    <div  v-show="poetResult.total>0&&!fullScreen" ref="mainPoetRef">

          <el-row style="margin-top:12px" justify="center" :gutter="20">
            <el-col v-for="info in poetResult.poetInfoBOs" :key="'i'+info.id" :span="5" style="margin:10px 0px;padding:0px 10px;">
                <el-card class="box-card" size="large" >
                  <template #header>
                    <div class="card-header">
                      <span>《 {{ info.title }} 》 </span>
                      <span> {{ info.author}} </span>

                      <el-button style="float:right;padding-bottom: 35px;" type="text" @click="clickTargetCard(info)" ref="targetCardRef"> <el-icon><FullScreen/></el-icon> </el-button>
                      <el-button style="float:right;padding-bottom: 35px;margin-right:10px;" title="百科" type="text"> <el-icon><Document/></el-icon> </el-button>
                    </div>
                  </template>
                    <el-scrollbar :height="cardItem + 'px'" >
                      <div v-for="(p,index) in info.paragraphs" :key="'p'+index" class="text item" style="margin:8px 0px;padding:10px 0px;">{{ p }}</div>
                  </el-scrollbar>
                </el-card>
          </el-col>
        </el-row>
        <el-row justify="center" style="margin-top:10px">
            <el-pagination :background="true" layout="prev, pager, next" :total="poetResult.total" :page-size="8" :hide-on-single-page="true" @current-change="handleCurrentChange">
            </el-pagination>
        </el-row>


    </div>
<!--      <el-footer>
      </el-footer>-->
</template>

<script setup>
import {ElMessage} from "element-plus";
import {ref,reactive,watch,onMounted} from 'vue'

import axios from 'axios'

//是否后台加载完
const init = ref(false);
//是否单项展开
const fullScreen = ref(false);
//目标单项
const targetCardRef = reactive({title:"",paragraphs:[]});
//锁定位置诗主体位置  计算 card高度
const mainPoetRef = ref("")
//卡片默认高度 350
const cardItem = ref(350);
const pageNum = ref(1);
const pageSize = ref(8);
//搜索关键字
const searchKey = ref("");
//输入框后缀图标
const suffixIcon = ref("search");

//搜索结果主体
const poetResult = reactive({
  total:-1,
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

  axios.post("/api/poet/es/search",{key:searchKey.value,size:pageSize.value,pageNum:pageNum.value})
      .then(
          (res) => {
            if(res.data.success){
                 init.value = true;
                 poetResult.total = res.data.info.total;
                 poetResult.pageNum = res.data.info.pageNum;
              if (res.data.info.poetInfoBOs) {
                 poetResult.poetInfoBOs = res.data.info.poetInfoBOs;
              }else{
                 poetResult.poetInfoBOs = [];
                 poetResult.total = 0;
                 poetResult.pageNum = 1;
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
//页码变动处理
const handleCurrentChange = (val) => {
  pageSize.value = 8;
  pageNum.value = val;
  searchAsync();
}
//单项展开
const clickTargetCard = (info)=>{
  fullScreen.value = !fullScreen.value;
  console.info(info.title);
  targetCardRef.title = info.title;
  targetCardRef.paragraphs = info.paragraphs;
  targetCardRef.author = info.author;

}

onMounted(()=>{
  searchAsync();
  // console.info("window.innerHeight" + window.innerHeight)
  //动态调整 卡片高度
  cardItem.value = (window.innerHeight - 240 - 36)/2 -150;
  /*setTimeout(()=>//获取元素的高度 渲染时才可见
    const {y} = mainPoetRef.value.getBoundingClientRect();
    console.info("y="+y);
  },2000)*/

})

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

  .poet-autocomplete >>> .el-icon{
    font-size:150%
  }

  .poet-fullScreen >>> .el-row{
    letter-spacing: 10px;
    font-size: 20px;
  }

</style>