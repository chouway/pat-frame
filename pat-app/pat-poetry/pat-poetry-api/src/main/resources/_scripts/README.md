kibana 开发工具
对于 “”“   ”“” 包含的区域会对它包含的 json对象转字符串 后才向后台输送。 对于保存模版时有助于可视化编写。
{
  "script": {
    "lang": "mustache",
    "source":"""
    
    """
  }
}  

===
#使用模版查询
GET poet-info/_search/template
{
  "id": "test_2",
  "params": {
   "aggs_infos" :[
     {
       "aggs_name":"val_per_propKey_0",
       "field":"properties.作品体裁.keyword",
       "size":10,
        "end":","
       
     },
     {
       "aggs_name":"val_per_propKey_1",
       "field":"properties.作品出处.keyword",
       "size":10,
        "end":""
      
     }
    ]
  }
}

#获取模板  poet-suggest-page poet-search-page
GET _scripts/poet-suggest-page

#参数渲染模板
GET _render/template/poet-search-page
{
  "params": 

  {"from":0,"highlight":true,"key":"","pageNum":1,"props":[{"end":"","propKey":"作品名称","propVals":["短歌行二首"]}],"size":8}

}
===