先setting  后mapping   在kibana开发区即可这么操作
===
DELETE suggest
PUT suggest
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1,
    "analysis": {
        "analyzer": {
            "ik_pinyin_analyzer": {
                "type": "custom",
                "tokenizer": "ik_max_word",
                "filter": ["my_pinyin", "word_delimiter"]
            }
        },
        "filter": {
            "my_pinyin": {
                "type": "pinyin",
                "first letter": "prefix",
                "padding_char": " "
            }
        }
    }    
  }
}

PUT suggest/_mapping
{
    
  "properties": {
      "keyword": {
          "type": "completion",
          "analyzer": "ik_pinyin_analyzer",
          "fields": {
              "key": {
                  "type": "keyword"
              }
          }
      },
      "id": {
          "type": "keyword"
      },
      "createDate": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
      }
  }
    
} 
===