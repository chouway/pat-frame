<template>
  <div @click="bodyClick">
    <div v-show="!fullScreen">
      <el-row justify="center">
        <el-col :span="5">
          <div class="poet-autocomplete">
            <el-autocomplete :fetch-suggestions="suggestAsync" v-model.lazy="searchKey" placeholder="中华古典文集"
                             size="large"
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




    <div v-show="!fullScreen" ref="mainPoetRef"
    >
      <el-button style="position: absolute;right: 2px;z-index: 99" @click="aggsAsync" v-loading="aggsAsyncLoading"
                 :type="userPropsComputer.length>0?'primary':''">筛选
        <el-icon>
          <Fold/>
        </el-icon>
      </el-button>
      <el-drawer v-model="drawer" title="筛选" :with-header="false">
        <el-scrollbar>
          <el-form label-width="80px" label-position="right">
            <template v-for="(poetAggsBO,index) in poetResult.poetAggsBOs.info" :key="'pags-'+index">
              <el-form-item :label="poetAggsBO.key" align="left">
                <el-checkbox-group size="large" v-model="poetResult.userProps[index]">
                  <el-checkbox v-for="(val,index) in poetAggsBO.vals" :key="'pavs-'+index" :label="val" border
                               @change="aggsAsync('1')">
                    {{ val }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </template>
            <el-form-item label="更多筛选" align="left">
              <el-select v-model="moreAggsKey" placeholder="属性Key" no-data-text="无" filterable="true"
                         @focus="moreAggsKeyAsync"
                         :filter-method="filterMoreAggsKey"
                         v-loading="moreAggsKeysLoading" @blur="moreAggsKeyBlur" @change="moreAggsKeyChange">
                <el-option
                    v-for="(item,index) in moreAggsKeys.copyInfo"
                    :key="'mak-'+index"
                    v-html="item.aggsKey"
                    :value="item.aggsKey"

                />
              </el-select>
              <el-select v-model="moreAggsVal" placeholder="属性Val" no-data-text="无" filterable="true" style="margin-left:10px"
                         :disabled="moreAggsValDisabled"
                         v-loading="moreAggsValsLoading" @focus="moreAggsValAsync"  :filter-method="filterMoreAggsVal" @blur="moreAggsValBlur" @change="moreAggsValChange">
                <el-option
                    v-for="(item,index) in moreAggsVals.copyInfo"
                    :key="'mav-'+index"
                    :label="item.aggsVal"
                    :value="item.aggsVal"
                />
              </el-select>

            </el-form-item>
            <el-button type="primary" @click="drawer = false">确定</el-button>
            <el-button @click="resetUserProps">重置</el-button>

          </el-form>
        </el-scrollbar>
      </el-drawer>
      <el-row style="margin-top:12px" justify="center" :gutter="20">
        <div v-show="poetResult.total==0">
          <el-empty description="未找到" />
        </div>
        <el-col v-for="(info,index) in poetResult.poetInfoBOs" :key="'info_'+info.id" :span="5"
                style="margin:10px 0px;padding:0px 10px;">
          <el-card class="box-card highlight" size="large">
            <template #header>
              <div class="card-header">
                <span style="margin-left: 70px;" v-html="info.title"> </span>
                <span v-html="info.author"></span>

                <el-button title="放大" style="float:right;padding-bottom: 35px;" type="text"
                           @click="clickTargetCard(info)"
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
                        :persistent="false"  v-model:visible="baikeVisible[index+1]"
                    >
                      <slot name="title">
                        <a :href="poetBaike.info.baikeUrl" target="_blank">{{ poetBaike.info.baikeTitle }}</a>
                        <el-icon style="cursor:pointer;position: absolute;right: 10px;z-index: 99;"
                                 @click="baikeVisible[index+1]=false">
                          <Close style="color:red"/>
                        </el-icon>
                      </slot>
                      <slot name="content">
                        <el-scrollbar max-height="400px">
                          <p v-for="(baikeDesc,index) in poetBaike.info.baikeDescs" :key="'bk_desc_'+index"
                             style="max-width: 600px;">
                            &nbsp;&nbsp;&nbsp;&nbsp;{{ baikeDesc }}
                          </p>
                        </el-scrollbar>
                        <el-descriptions :border="true">
                          <el-descriptions-item v-for="(propertyBO,index) in poetBaike.info.propertyBOs"
                                                :key="'bk_prop_'+index" :label="propertyBO.key">
                            {{ propertyBO.value }}
                          </el-descriptions-item>
                        </el-descriptions>

                      </slot>
                      <template #reference>

                          <el-button style="float:right;padding-bottom: 35px;margin-right:10px;" title="百科" type="text"
                                     @click.stop="poetBaikeShow(info.id,index+1)" v-loading="baikeLoading[index+1]">
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
          <el-card :class="fullHighlightClass" size="large">
            <template #header>
              <div class="card-header">
                <el-switch v-model="fullHighlight" style="float: left;" title="高亮显示"/>
                <span v-html="targetCardRef.info.title" style="margin-left: 100px;"/> <span
                  v-html="targetCardRef.info.author"/>
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
                      :persistent="false" v-model:visible="baikeVisible[0]"
                  >
                    <slot name="title">
                      <a :href="poetBaike.info.baikeUrl" target="_blank">{{ poetBaike.info.baikeTitle }}</a>
                    </slot>
                    <slot name="content">
                      <el-scrollbar max-height="400px">
                        <p v-for="(baikeDesc,index) in poetBaike.info.baikeDescs" :key="'bk_desc_'+index"
                           style="max-width: 600px;margin-down:2px">
                          &nbsp;&nbsp;&nbsp;&nbsp;{{ baikeDesc }}
                        </p>
                      </el-scrollbar>
                      <el-descriptions :border="true">
                        <el-descriptions-item v-for="(propertyBO,index) in poetBaike.info.propertyBOs"
                                              :key="'bk_prop_'+index" :label="propertyBO.key">
                          {{ propertyBO.value }}
                        </el-descriptions-item>
                      </el-descriptions>

                    </slot>
                    <template #reference>
                      <el-button type="text" title="百科" style="float:right;padding-bottom: 35px;margin-right:10px;"
                                 @click.stop="poetBaikeShow(targetCardRef.info.id,0)" v-loading="baikeLoading[0]">
                        <el-icon>
                          <Document/>
                        </el-icon>
                      </el-button>
                    </template>
                  </el-popover>
                </template>

              </div>
            </template>
            <el-scrollbar :height="fullCardItem + 'px'">
              <div v-for="(p,index) in targetCardRef.info.paragraphs" :key="'p'+index" class="text item"
                   style="margin:8px 0px;padding:10px 0px;" v-html="p">
              </div>
            </el-scrollbar>
          </el-card>
        </el-col>
      </el-row>
    </div>

  </div>
  <!--      <el-footer>
        </el-footer>-->

  <a :href="'https://dict.baidu.com/s?wd=' + guidKey" target="_blank" :class="guideCss" :style="'position:fixed;top:'+ (mouseXY.y + 10) + 'px;left:' + (mouseXY.x + 10) +'px;'">
    <el-icon title="百度汉语"  :size="28" color="#62A3D9" >
      <guide/>
    </el-icon>
  </a>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {ref, reactive, watch, onMounted, computed} from 'vue'
import {useCookies} from 'vue3-cookies'
import axios from 'axios'

//是否加载搜索中
const searchAsyncLoading = ref(false);
//是否加载建议中
const aggsAsyncLoading = ref(false);
//more Key 加载中
const moreAggsKeysLoading = ref(false);
//more Val 加载中
const moreAggsValsLoading = ref(false);
//more Val 是否禁用
const moreAggsValDisabled = ref(true);
//筛选抽屉
const drawer = ref(false)
//是否单项展开
const fullScreen = ref(false);
//满屏高亮
var {cookies} = useCookies();
var fullHighlightVal = cookies.get("fullHighlightVal");
if (fullHighlightVal == undefined) {
  fullHighlightVal = true;
} else {
  fullHighlightVal = fullHighlightVal === 'true' ? true : false;
}
const fullHighlight = ref(fullHighlightVal);
//满屏高亮动态样式
const fullHighlightClass = reactive(
    {
      "box-card": true,
      "highlight": fullHighlight.value
    }
)

//目标单项
const targetCardRef = reactive({info: {}});

//锁定位置诗主体位置  计算 card高度
const mainPoetRef = ref("")
//卡片默认高度 350
const cardItem = ref(350);
const fullCardItem = ref(700);
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
  userProps: [],//用户筛选可选项  已筛选会赋值 未筛选会以 undefined的占位
  poetAggsBOs: {info: []},//筛选信息
  poetInfoBOs: {}//搜索信息
})
//计算往后台上送的用户筛选项
const userPropsComputer = computed({
  // 获取数据时调用
  get: () => {
    var props = []
    for (var i in poetResult.userProps) {
      if (poetResult.userProps[i]) {
        if (poetResult.poetAggsBOs.info[i]) {
          var key = poetResult.poetAggsBOs.info[i].key;
          var vals = poetResult.userProps[i];
          if (key && vals && vals.length > 0) {
            props.push({propKey: key, propVals: vals});
          }
        }
      }
    }
    if(moreAggsKey.value != ''&&moreAggsVal.value != ''){
       props.push({propKey: moreAggsKey.value, propVals: [moreAggsVal.value]})
    }
    return props;
  },
  set: () => {
    poetResult.userProps = [];
  }
})
function resetMoreAggs(){
  moreAggsKeys.hasAsync=false;
  moreAggsVals.hasAsync=false;
  moreAggsKeys.info = [];
  moreAggsVals.info = [];
  moreAggsKey.value = '';
  moreAggsVal.value = '';
  moreAggsValDisabled.value = true;
}

//监测searchKey变化
watch(searchKey, (newV, oldV) => {
  if (searchKey.value.length == 0) {
    suffixIcon.value = 'search'
  } else {
    suffixIcon.value = ''
  }
  if (newV != oldV) {
    userPropsComputer.value = [];
    poetResult.poetAggsBOs.info = [];
    resetMoreAggs();
    searchAsync();
  }

})

//百科显示
const poetBaike = reactive({info: {}})
const baikeLoading = ref([false, false, false, false, false, false, false, false, false]);
const baikeVisible = ref([false, false, false, false, false, false, false, false, false]);
const poetBaikeShow = (infoId, index) => {
  if (poetBaike.info) {
    if (poetBaike.info.infoId === infoId) {
      baikeVisible.value[index] = true;
      return
    } else {
      poetBaike.info = {};
      baikeVisible.value = [false, false, false, false, false, false, false, false, false];
    }
  }
  baikeLoading.value[index] = true;
  axios.post("/api/poet/baike?", "infoId=" + infoId)
      .then(
          (res) => {
            if (res.data.success) {
              poetBaike.info = res.data.info;
              poetBaike.info.infoId = infoId
              baikeVisible.value[index] = true;
            } else {
              ElMessage.warning(res.data.message);
            }
          })
      .catch(
          (err) => {
            console.error("err" + err);
            ElMessage.error("server error");
          }).finally(() => {
    baikeLoading.value[index] = false
  })
}


//后台访问  搜索
const searchAsync = () => {
  searchAsyncLoading.value = true;
  axios.post("/api/poet/search", {
    highlight: true,
    key: searchKey.value,
    size: pageSize.value,
    pageNum: pageNum.value,
    props: userPropsComputer.value
  })
      .then(
          (res) => {
            if (res.data.success) {
              poetResult.total = res.data.info.total;
              poetResult.pageNum = res.data.info.pageNum;
              if (res.data.info.poetInfoBOs) {
                for (var i in res.data.info.poetInfoBOs) {
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
  if (isChoosed === '1') {
    searchAsync();
  } else {
    if (poetResult.poetAggsBOs.info && poetResult.poetAggsBOs.info.length > 0) {
      drawer.value = true
      return;
    }
  }
  aggsAsyncLoading.value = true;
  axios.post("/api/poet/aggs", {key: searchKey.value, props: userPropsComputer.value})
      .then(
          (res) => {

            if (res.data.success) {
              drawer.value = true
              if (res.data.info) {
                //初始化相应的已筛选项  初始化更多
                userPropsComputer.value = [];
                var info = res.data.info;
                for (var i in info) {
                  var checkedVals = undefined;
                  if (info[i].choosePreSize > 0) {
                    checkedVals = [];
                    for (let j = 0; j < info[i].choosePreSize; j++) {
                      checkedVals.push(info[i].vals[j]);
                    }
                  }
                  poetResult.userProps.push(checkedVals)
                }
                moreAggsVal.value = '';
                poetResult.poetAggsBOs.info = info;
              } else {
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
const resetUserProps = () => {
  if (userPropsComputer.value.length < 1) {
    return;
  }
  userPropsComputer.value = [];
  aggsAsync('1');
  resetMoreAggs();
}

//监听fullHighlight
watch(fullHighlight, (newV) => {
  fullHighlightClass.highlight = newV;
  cookies.set("fullHighlightVal", newV);
})
//全局的点击事件
const bodyClick = () => {
  baikeVisible.value = [false, false, false, false, false, false, false, false, false]
}

onMounted(() => {
  searchAsync();
  // console.info("window.innerHeight" + window.innerHeight)
  //动态调整 卡片高度
  cardItem.value = (window.innerHeight - 240 - 36) / 2 - 150;
  fullCardItem.value = window.innerHeight - 300;
  document.addEventListener("mouseup",mouseXY.mouseupHandle);
  /*setTimeout(()=>//获取元素的高度 渲染时才可见
    const {y} = mainPoetRef.value.getBoundingClientRect();
    console.info("y="+y);
  },2000)*/

})

//更多筛选Keys  [{}]
const moreAggsKeys = reactive({
  info: []
})
//更多筛选Keys 已选
const moreAggsKey = ref('');
const moreAggsKeyOld = ref('');
//更多筛选Vals
const moreAggsVals = reactive({
  info: []
})
//更多筛选Vals 已选
const moreAggsVal = ref('');

//moreAggsKeyAsync  更多筛选key 访问后台
const moreAggsKeyAsync = () => {
  if (moreAggsKeys.hasAsync) {
    moreAggsKeys.copyInfo = moreAggsKeys.info.filter(() => true);
    return;
  }
  moreAggsKeysLoading.value = true;
  axios.post("/api/poet/getAggsKeys", {key: searchKey.value})
      .then(
          (res) => {
            // console.info("data" + res);
            if (res.data.success) {
              moreAggsKeys.hasAsync = true;
              moreAggsKeys.info = res.data.info;
              moreAggsKeys.copyInfo = JSON.parse(JSON.stringify(res.data.info))
            } else {
              ElMessage.warning(res.data.message);
            }
          }
      ).catch(
      (err) => {
        ElMessage.error("server error:" + err);
      }
  ).finally(() => {
        moreAggsKeysLoading.value = false;
      }
  )
}
//动态过滤 Aggs
const filterMoreAggsKey = (input) => {
  if (!input || (input == '')) {
    moreAggsKeys.copyInfo = moreAggsKeys.info.filter(() => true);
    return;
  }
  input = input.trim();
  moreAggsKeys.copyInfo = moreAggsKeys.info.filter((item) => {
    // console.info(item);
    if (/^[a-z]+$/.test(input.toLowerCase())) {
      if (item.fullPY.indexOf(input) == 0) {
        return true;
      } else if (item.firstPY.indexOf(input) == 0) {
        return true;
      }
    }
    return item.aggsKey.indexOf(input) > -1;

  });
  moreAggsKey.value = input;
}

//失云聚焦时 判定 是否为label中的值  不是则清空
const moreAggsKeyBlur = () => {
  if(!moreAggsKey.value){
    return;
  }
  var filter = moreAggsKeys.info.filter(item => item.aggsKey === moreAggsKey.value);
  if (filter.length < 1) {//非下拉选项的值 移除掉
    moreAggsKey.value = '';
    moreAggsVal.value = '';
    moreAggsValDisabled.value = true;
  }else{
    if(moreAggsKeyOld.value==moreAggsKey.value){
      return;
    }
    moreAggsKeyOld.value = moreAggsKey.value;
    moreAggsKeyChange();
  }
}

const moreAggsKeyChange = ()=>{
  // console.info("moreAggsKey.value==" + moreAggsKey.value)
  moreAggsValDisabled.value = false;
  moreAggsVals.hasAsync=false;
  moreAggsVal.value = '';
  moreAggsValAsync();
}


//moreAggsValAsync  更多筛选Val 访问后台
const moreAggsValAsync = () => {
  if (moreAggsVals.hasAsync) {
    moreAggsVals.copyInfo = moreAggsVals.info.filter(() => true);
    return;
  }
  moreAggsValsLoading.value = true;
  axios.post("/api/poet/getAggsKeyVals", {
    key: searchKey.value,
    aggsKey: moreAggsKey.value
  })
      .then(
          (res) => {
            // console.info("data" + res);
            if (res.data.success) {
              moreAggsVals.info = res.data.info;
              moreAggsVals.hasAsync = true;
              moreAggsVals.copyInfo = JSON.parse(JSON.stringify(res.data.info))
            } else {
              ElMessage.warning(res.data.message);
            }
          }
      ).catch(
      (err) => {
        ElMessage.error("server error:" + err);
      }
  ).finally(() => {
        moreAggsValsLoading.value = false;
      }
  )
}


const filterMoreAggsVal = (input) => {
  if (!input || (input == '')) {
    moreAggsVals.copyInfo = moreAggsVals.info.filter(() => true);
    return;
  }
  input = input.trim();
  moreAggsVals.copyInfo = moreAggsVals.info.filter((item) => {
    // console.info(item);
    if (/^[a-z]+$/.test(input.toLowerCase())) {
      if (item.fullPY.indexOf(input) == 0) {
        return true;
      } else if (item.firstPY.indexOf(input) == 0) {
        return true;
      }
    }
    return item.aggsVal.indexOf(input) > -1;

  });
  moreAggsVal.value = input;
}


const moreAggsValBlur = () => {
  if(!moreAggsVal.value){
    return;
  }
  var filter = moreAggsVals.info.filter(item => item.aggsVal === moreAggsVal.value);
  if (filter.length < 1) {//非下拉选项的值 移除掉
    moreAggsVal.value = '';
  }else{
     moreAggsValChange();
  }
}

const moreAggsValChange = ()=>{
 aggsAsync('1')
}

//鼠标选取事件 前往 百度汉语
const guideCss = reactive({
  'guid-show': false,
  'guid-hide': true
})
const guidKey = ref('');
const handleMouseSelect = ()=>{
  let selectedTxt = window.getSelection().toString()
  if(selectedTxt){
    guidKey.value = selectedTxt;
    guideCss["guid-hide"]=false;
    guideCss['guid-show']=true;
  }else{
    guideCss["guid-hide"]=true;
    guideCss["guid-show"]=false;
  }
}

const mouseXY = reactive({
    x:0,
    y:0,
    mouseupHandle:(e)=>{
      mouseXY.x = e.pageX;
      mouseXY.y = e.pageY;
      handleMouseSelect();
    }
});
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

.highlight >>> em {
  color: red;
  font-style: normal;
}

.poet-fullScreen >>> em {
  font-style: normal;
}

.guid-hide{
  display:none;
}

.guid-show{
  display:block;
  position: absolute;
  z-index: 999;
  cursor:pointer;
}
</style>