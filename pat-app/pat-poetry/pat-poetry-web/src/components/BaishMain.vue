<template>
  <el-row justify="center" v-show="!fullScreen">
    <el-col :span="5">
      <div class="poet-autocomplete">
        <el-autocomplete :fetch-suggestions="suggestAsync" v-model.lazy="searchKey" placeholder="中华古典文集" size="large"
                         :hide-loading="true" class="input-with-select" maxlength="100" :suffix-icon="suffixIcon"
                         clearable="true">
          <template #append>
            <el-button @click="searchAsync" v-loading="searchAsyncLoading">搜索</el-button>
          </template>
        </el-autocomplete>
      </div>
    </el-col>

  </el-row>



  <div v-show="poetResult.total==0&&!fullScreen" style="margin-top:12px">
    <el-row justify="center">
      <el-empty description="未找到"/>
    </el-row>
  </div>
  <div v-show="poetResult.total>0&&!fullScreen" ref="mainPoetRef">

    <el-row style="margin-top:12px" justify="center" :gutter="20">
      <el-col v-for="(info,index) in poetResult.poetInfoBOs" :key="'info_'+info.id" :span="5"
              style="margin:10px 0px;padding:0px 10px;">
        <el-card class="box-card" size="large">
          <template #header>
            <div class="card-header">
              <span>《 {{ info.title }} 》 </span>
              <span> {{ info.author }} </span>

              <el-button title="放大" style="float:right;padding-bottom: 35px;" type="text" @click="clickTargetCard(info)"
                         ref="targetCardRef">
                <el-icon>
                  <FullScreen/>
                </el-icon>
              </el-button>


              <el-popover
                  :placement="index>3?'top-start':'bottom-start'"
                  width="500"
                  trigger="click"
                  persistent = "false"
              >
                <slot name="title">
                  <a :href="poetBaike.baikeUrl" target="_blank">{{poetBaike.baikeTitle}}</a>
                </slot>
                <slot name="content">
                    <el-scrollbar max-height="400px">
                      <p v-for="(baikeDesc,index) in poetBaike.baikeDescs" :key="'bk_desc_'+index" style="max-width: 600px;">
                        &nbsp;&nbsp;&nbsp;&nbsp;{{baikeDesc}}
                      </p>
                    </el-scrollbar>
                  <el-descriptions :border="true">
                    <el-descriptions-item  v-for="(propertyBO,index) in poetBaike.propertyBOs" :key="'bk_prop_'+index" :label="propertyBO.key">
                      {{ propertyBO.value }}</el-descriptions-item>
                  </el-descriptions>

                </slot>
                <template #reference>
                  <el-button style="float:right;padding-bottom: 35px;margin-right:10px;" title="百科" type="text" @click="poetBaikeShow(info.id)">
                    <el-icon>
                      <Document/>
                    </el-icon>
                  </el-button>
                </template>
              </el-popover>



            </div>
          </template>
          <el-scrollbar :height="cardItem + 'px'">
            <div v-for="(p,index) in info.paragraphs" :key="'p'+index" class="text item"
                 style="margin:8px 0px;padding:10px 0px;">{{ p }}
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>
    <el-row justify="center" style="margin-top:10px">
      <el-pagination :background="true" layout="prev, pager, next" :total="poetResult.total" :page-size="8"
                     :hide-on-single-page="true" @current-change="handleCurrentChange">
      </el-pagination>
    </el-row>
  </div>


    <div v-show="fullScreen" class="poet-fullScreen" style="margin-top:12px">
      <el-row style="margin-top:12px" justify="center">
        <el-col :span="20">
          <el-card class="box-card" size="large">
            <template #header>
              <div class="card-header">
                <span style="margin-left: 100px;;">《{{ targetCardRef.title }}》  {{ targetCardRef.author }}</span>
                <el-button type="text" @click="fullScreen=false" style="float:right;padding-bottom: 35px;" title="缩小">
                  <el-icon>
                    <Minus/>
                  </el-icon>
                </el-button>

                <el-popover
                    placement="bottom-end"
                    width="500"
                    trigger="click"
                    persistent = "false"
                >
                  <slot name="title">
                    <a :href="poetBaike.baikeUrl" target="_blank">{{poetBaike.baikeTitle}}</a>
                  </slot>
                  <slot name="content">
                    <el-scrollbar max-height="400px">
                      <p v-for="(baikeDesc,index) in poetBaike.baikeDescs" :key="'bk_desc_'+index" style="max-width: 600px;margin-down:2px">
                        &nbsp;&nbsp;&nbsp;&nbsp;{{baikeDesc}}
                      </p>
                    </el-scrollbar>
                    <el-descriptions :border="true">
                      <el-descriptions-item  v-for="(propertyBO,index) in poetBaike.propertyBOs" :key="'bk_prop_'+index" :label="propertyBO.key">
                        {{ propertyBO.value }}</el-descriptions-item>
                    </el-descriptions>

                  </slot>
                  <template #reference>
                    <el-button type="text" title="百科" style="float:right;padding-bottom: 35px;margin-right:10px;" @click="poetBaikeShow(targetCardRef.id)">
                      <el-icon>
                        <Document/>
                      </el-icon>
                    </el-button>
                  </template>
                </el-popover>

              </div>
            </template>
            <el-scrollbar>
              <div v-for="(p,index) in targetCardRef.paragraphs" :key="'p'+index" class="text item"
                   style="margin:8px 0px;padding:10px 0px;">{{ p }}
              </div>
            </el-scrollbar>
          </el-card>
        </el-col>
      </el-row>
    </div>


  <!--      <el-footer>
        </el-footer>-->
</template>

<script setup>
import {ElMessage} from "element-plus";
import {ref, reactive, watch, onMounted} from 'vue'

import axios from 'axios'

//是否加载搜索中
const searchAsyncLoading = ref(false);

//是否单项展开
const fullScreen = ref(false);
//目标单项
const targetCardRef = reactive({id: -1,title: "", paragraphs: []});
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
  total: -1,
  pageNum: 1,
  propKeys: [],
  poetAggsBO: [],
  poetInfoBOs: []
})
//监测searchKey变化
watch(searchKey, (newV, oldV) => {
  if (searchKey.value.length == 0) {
    suffixIcon.value = 'search'
  } else {
    suffixIcon.value = ''
  }
  if (newV != oldV) {
    searchAsync();
  }

})

//百科显示
const poetBaikeTitle=ref("");
const poetBaike = reactive({})
const poetBaikeShow = ((infoId)=> {
  console.info("poetBaikeShow==" + poetBaike)
  axios.post("/api/poet/baike?", "infoId="+infoId)
      .then(
          (res) => {
            if (res.data.success) {
              poetBaike.baikeUrl = res.data.info.baikeUrl;
              poetBaike.baikeTitle = res.data.info.baikeTitle;
              poetBaike.baikeDescs = res.data.info.baikeDescs;
              poetBaike.propertyBOs = res.data.info.propertyBOs;
              var poetBaikeResp = res.data.info;
              if(poetBaikeResp.poetChapter&&poetBaikeResp.poetSection){
                  poetBaikeTitle.value =  poetBaikeResp.poetSet + "&nbsp;>&nbsp;" + poetBaikeResp.poetChapter + "&nbsp;>&nbsp;" + poetBaikeResp.poetSection + "&nbsp;>&nbsp;" + poetBaikeResp.poetTitle;
              }else if(poetBaike.poetChapter){
                poetBaikeTitle.value =  poetBaikeResp.poetSet + "&nbsp;>&nbsp;" + poetBaikeResp.poetChapter + "&nbsp;>&nbsp;" + poetBaikeResp.poetTitle;
              }else{
                poetBaikeTitle.value =  poetBaikeResp.poetSet + "&nbsp;>&nbsp;" +  poetBaikeResp.poetTitle;
              }
            } else {
              ElMessage.warning(res.data.message);
            }
          })
      .catch(
          (err) => {
            console.error("err" + err);
            ElMessage.error("server error");
          })
})


//后台访问  搜索
const searchAsync = () => {
  searchAsyncLoading.value = true;
  axios.post("/api/poet/search", {key: searchKey.value, size: pageSize.value, pageNum: pageNum.value})
      .then(
          (res) => {
            if (res.data.success) {
              poetResult.total = res.data.info.total;
              poetResult.pageNum = res.data.info.pageNum;
              if (res.data.info.poetInfoBOs) {
                poetResult.poetInfoBOs = res.data.info.poetInfoBOs;
              } else {
                poetResult.poetInfoBOs = [];
                poetResult.total = 0;
                poetResult.pageNum = 1;
              }
            } else {
              ElMessage.warning(res.data.message);
            }
          }
      ).catch(
      (err) => {
        console.error("err" + err);
        ElMessage.error("server error");
      }
  ).finally(() => {
    searchAsyncLoading.value = false;
  })
}
//后台访问  推荐词
const suggestAsync = (queryString, cb) => {
  axios.post("/api/poet/suggest", {keyword: queryString})
      .then(
          (res) => {
            console.info("data" + res);
            if (res.data.success) {
              var results = [];
              for (let i = 0; i < res.data.info.length; i++) {
                results.push({value: res.data.info[i].keyword});
              }
              cb(results);
            } else {
              ElMessage.warning(res.data.message);
            }
          }
      ).catch(
      (err) => {
        ElMessage.error("server error:" + err);
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
const clickTargetCard = (info) => {
  fullScreen.value = !fullScreen.value;
  console.info(info.title);
  targetCardRef.id = info.id;
  targetCardRef.title = info.title;
  targetCardRef.paragraphs = info.paragraphs;
  targetCardRef.author = info.author;

}

onMounted(() => {
  searchAsync();
  // console.info("window.innerHeight" + window.innerHeight)
  //动态调整 卡片高度
  cardItem.value = (window.innerHeight - 240 - 36) / 2 - 150;
  /*setTimeout(()=>//获取元素的高度 渲染时才可见
    const {y} = mainPoetRef.value.getBoundingClientRect();
    console.info("y="+y);
  },2000)*/

})



</script>

<style scoped>

.poet-autocomplete >>> .el-autocomplete {
  width: 100%;

}

.poet-autocomplete >>> .el-autocomplete-suggestion {
  width: 100%;
}

.poet-autocomplete >>> .el-input__inner {
  height: 60px;
  font-size: 20px;
}

.poet-autocomplete >>> .el-icon {
  font-size: 150%
}

.poet-fullScreen >>> .el-row {
  letter-spacing: 10px;
  font-size: 20px;
}

</style>