<template>
  <div v-show="!fullScreen">
    <el-row justify="center" >
      <el-col :span="5">
        <div class="poet-autocomplete">
          <el-autocomplete :fetch-suggestions="suggestAsync" v-model.lazy="searchKey" placeholder="中华古典文集" size="large"
                           :hide-loading="true" class="input-with-select" maxlength="100" :suffix-icon="suffixIcon"
                           :clearable="true">
            <template #append>
              <el-button @click="searchAsync" v-loading="searchAsyncLoading">搜索</el-button>
            </template>
          </el-autocomplete>
        </div>
      </el-col>
    </el-row>

  </div>



  <div v-show="poetResult.total==0" style="margin-top:12px">
    <el-row justify="center">
      <el-empty description="未找到"/>
    </el-row>
  </div>

  <div v-show="poetResult.total>0&&!fullScreen" ref="mainPoetRef">
    <el-button style="position: absolute;right: 2px;z-index: 99" @click="aggsAsync" v-loading="aggsAsyncLoading" :type="userPropsComputer.length>0?'primary':''">筛选<el-icon><Fold/></el-icon></el-button>
    <el-drawer v-model="drawer" title="筛选" :with-header="false">
      <el-scrollbar>
      <el-form label-width="80px" label-position="right">
        <template v-for="(poetAggsBO,index) in poetResult.poetAggsBOs.info" :key="'pags-'+index">
          <el-form-item  :label="poetAggsBO.key" align="left">
            <el-checkbox-group  size="large" v-model="poetResult.userProps[index]">
              <el-checkbox v-for="(val,index) in poetAggsBO.vals" :key="'pavs-'+index" :label="val" border @change="aggsAsync('1')">
                {{ val }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </template>
        <el-button type="primary" @click="drawer = false">确定</el-button> <el-button @click="resetUserProps">重置</el-button>

      </el-form>
      </el-scrollbar>
    </el-drawer>
    <el-row style="margin-top:12px" justify="center" :gutter="20">
      <el-col v-for="(info,index) in poetResult.poetInfoBOs" :key="'info_'+info.id" :span="5"
              style="margin:10px 0px;padding:0px 10px;">
        <el-card class="box-card highlight" size="large">
          <template #header>
            <div class="card-header">
              <span style="margin-left: 70px;" v-html="info.title"> </span>
              <span v-html="info.author"></span>

              <el-button title="放大" style="float:right;padding-bottom: 35px;" type="text" @click="clickTargetCard(info)"
                         ref="targetCardRef">
                <el-icon>
                  <FullScreen/>
                </el-icon>
              </el-button>

              <template v-if="info.baikeId!=null">
                  <el-popover
                      :placement="index%4==3?'left-end':'bottom-start'"
                      width="500"
                      trigger="click"
                      :persistent = "false" v-loading="baikeLoading[index+1]"
                  >
                    <slot name="title">
                      <a :href="poetBaike.info.baikeUrl" target="_blank">{{poetBaike.info.baikeTitle}}</a>
                    </slot>
                    <slot name="content">
                        <el-scrollbar max-height="400px">
                          <p v-for="(baikeDesc,index) in poetBaike.info.baikeDescs" :key="'bk_desc_'+index" style="max-width: 600px;">
                            &nbsp;&nbsp;&nbsp;&nbsp;{{baikeDesc}}
                          </p>
                        </el-scrollbar>
                      <el-descriptions :border="true">
                        <el-descriptions-item  v-for="(propertyBO,index) in poetBaike.info.propertyBOs" :key="'bk_prop_'+index" :label="propertyBO.key">
                          {{ propertyBO.value }}</el-descriptions-item>
                      </el-descriptions>

                    </slot>
                    <template #reference>
                      <el-button style="float:right;padding-bottom: 35px;margin-right:10px;" title="百科" type="text" @click="poetBaikeShow(info.id,index+1)" v-loading="baikeLoading[index+1]">
                        <el-icon>
                          <Document/>
                        </el-icon>
                      </el-button>
                    </template>
                  </el-popover>
              </template>



            </div>
          </template>
          <el-scrollbar :height="cardItem + 'px'">
            <div v-for="(p,index) in info.paragraphs" :key="'p'+index" class="text item"
                 style="margin:8px 0px;padding:10px 0px;" v-html="p">
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
          <el-card :class="fullHighlightClass" size="large" >
            <template #header>
              <div class="card-header">
                <el-switch v-model="fullHighlight" style="float: left;" title="高亮显示"/>
                <span v-html="targetCardRef.info.title" style="margin-left: 100px;" /> <span v-html="targetCardRef.info.author"/>
                <el-button type="text" @click="fullScreen=false" style="float:right;padding-bottom: 35px;" title="缩小">
                  <el-icon>
                    <Minus/>
                  </el-icon>
                </el-button>
                <template v-if="targetCardRef.info.baikeId!=null">
                  <el-popover
                      placement="bottom-end"
                      width="500"
                      trigger="click"
                      :persistent = "false" v-loading="baikeLoading[0]"
                  >
                    <slot name="title">
                      <a :href="poetBaike.info.baikeUrl" target="_blank">{{poetBaike.info.baikeTitle}}</a>
                    </slot>
                    <slot name="content">
                      <el-scrollbar max-height="400px">
                        <p v-for="(baikeDesc,index) in poetBaike.info.baikeDescs" :key="'bk_desc_'+index" style="max-width: 600px;margin-down:2px">
                          &nbsp;&nbsp;&nbsp;&nbsp;{{baikeDesc}}
                        </p>
                      </el-scrollbar>
                      <el-descriptions :border="true">
                        <el-descriptions-item  v-for="(propertyBO,index) in poetBaike.info.propertyBOs" :key="'bk_prop_'+index" :label="propertyBO.key">
                          {{ propertyBO.value }}</el-descriptions-item>
                      </el-descriptions>

                    </slot>
                    <template #reference>
                      <el-button type="text" title="百科" style="float:right;padding-bottom: 35px;margin-right:10px;" @click="poetBaikeShow(targetCardRef.info.id,0)" v-loading="baikeLoading[0]">
                        <el-icon>
                          <Document/>
                        </el-icon>
                      </el-button>
                    </template>
                  </el-popover>
                </template>

              </div>
            </template>
            <el-scrollbar>
              <div v-for="(p,index) in targetCardRef.info.paragraphs" :key="'p'+index" class="text item"
                   style="margin:8px 0px;padding:10px 0px;" v-html="p">
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
import {ref, reactive, watch, onMounted,computed} from 'vue'
import {useCookies} from 'vue3-cookies'
import axios from 'axios'

//是否加载搜索中
const searchAsyncLoading = ref(false);
//是否加载建议中
const aggsAsyncLoading = ref(false);
//筛选抽屉
const drawer = ref(false)
//是否单项展开
const fullScreen = ref(false);
//满屏高亮
var {cookies} = useCookies();
var fullHighlightVal = cookies.get("fullHighlightVal");
if(fullHighlightVal == undefined){
  fullHighlightVal = true;
}else{
  fullHighlightVal = fullHighlightVal==='true'?true:false;
}
const fullHighlight = ref(fullHighlightVal);
//满屏高亮动态样式
const fullHighlightClass = reactive(
    {
      "box-card":true,
      "highlight":fullHighlight.value
    }
)

//目标单项
const targetCardRef = reactive({info:{}});

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
  total: -1,//共计数量
  pageNum: 1,//当前页码
  userProps:[],//用户筛选可选项  已筛选会赋值 未筛选会以 undefined的占位
  poetAggsBOs: [],//筛选信息
  poetInfoBOs: []//搜索信息
})
//计算往后台上送的用户筛选项
const userPropsComputer = computed({
  // 获取数据时调用
  get: () => {
    var props = []
    for(var i in poetResult.userProps){
      if(poetResult.userProps[i]){
        if(poetResult.poetAggsBOs.info[i]){
          var key = poetResult.poetAggsBOs.info[i].key;
          var vals = poetResult.userProps[i];
          if(key && vals && vals.length>0){
            props.push({propKey:key,propVals:vals});
          }
        }
      }
    }
    return props;
  },
  set:()=>{
    poetResult.userProps=[];
  }
})



//监测searchKey变化
watch(searchKey, (newV, oldV) => {
  if (searchKey.value.length == 0) {
    suffixIcon.value = 'search'
  } else {
    suffixIcon.value = ''
  }
  if (newV != oldV) {
    userPropsComputer.value=[];
    searchAsync();
  }

})

//百科显示
const poetBaike = reactive({info:{}})
const baikeLoading = ref([false,false,false,false,false,false,false,false,false]);
const poetBaikeShow = (infoId,index)=> {
  if(poetBaike.info){
    if (poetBaike.info.infoId === infoId) {
      return
    }
  }
  baikeLoading.value[index] = true;
  axios.post("/api/poet/baike?", "infoId="+infoId)
      .then(
          (res) => {
            if (res.data.success) {
              poetBaike.info = res.data.info;
              poetBaike.info.infoId = infoId
            } else {
              ElMessage.warning(res.data.message);
            }
          })
      .catch(
          (err) => {
            console.error("err" + err);
            ElMessage.error("server error");
          }).
      finally(()=>{
          baikeLoading.value[index] = false
   })
}


//后台访问  搜索
const searchAsync = () => {
  searchAsyncLoading.value = true;
  axios.post("/api/poet/search", {highlight:true,key: searchKey.value, size: pageSize.value, pageNum: pageNum.value,props:userPropsComputer.value})
      .then(
          (res) => {
            if (res.data.success) {
              poetResult.total = res.data.info.total;
              poetResult.pageNum = res.data.info.pageNum;
              if (res.data.info.poetInfoBOs) {
                for(var i in res.data.info.poetInfoBOs){
                    res.data.info.poetInfoBOs[i].title = '《' + res.data.info.poetInfoBOs[i].title + '》';
                }
                poetResult.propKeys = res.data.info.propKeys;
                poetResult.poetInfoBOs = res.data.info.poetInfoBOs;
              } else {
                poetResult.poetInfoBOs = [];
                poetResult.propKeys = [];
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
            // console.info("data" + res);
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
//后台筛选
const aggsAsync = (isChoosed) => {
  if(isChoosed==='1'){
    searchAsync();
  }
  aggsAsyncLoading.value = true;
  axios.post("/api/poet/aggs", {key: searchKey.value,props:userPropsComputer.value})
      .then(
          (res) => {

            if (res.data.success) {
              drawer.value = true
              if(res.data.info){
                poetResult.userProps = [];
                var info = res.data.info;
                for(var i in info){
                  var checkedVals = undefined;
                  if(info[i].choosePreSize>0){
                    checkedVals = [];
                    for (let j = 0; j < info[i].choosePreSize; j++) {
                      checkedVals.push(info[i].vals[j]);
                    }
                  }
                  poetResult.userProps.push(checkedVals)
                }

                poetResult.poetAggsBOs.info = info;
              }else{
                poetResult.poetAggsBOs.info = [];
              }
            } else {
              ElMessage.warning(res.data.message);
            }
          })
      .catch(
      (err) => {
        ElMessage.error("server error:" + err);
      }).finally(() => {
        aggsAsyncLoading.value = false;
    });
}
//页码变动处理
const handleCurrentChange = (val) => {
  pageSize.value = 8;
  pageNum.value = val;
  searchAsync();
}
//单项展开
const clickTargetCard = (info) => {
  targetCardRef.info = info;
  fullScreen.value = !fullScreen.value;
}
//重置筛选项
const resetUserProps = ()=>{
  if (userPropsComputer.value.length < 1) {
    return;
  }
  userPropsComputer.value=[];
  aggsAsync('1');
}

//监听fullHighlight
watch(fullHighlight,(newV)=>{
  fullHighlightClass.highlight=newV;
  cookies.set("fullHighlightVal",newV);
})


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

.highlight >>> em{
  color:red;
  font-style: normal;
}

.poet-fullScreen >>> em{
  font-style: normal;
}

</style>