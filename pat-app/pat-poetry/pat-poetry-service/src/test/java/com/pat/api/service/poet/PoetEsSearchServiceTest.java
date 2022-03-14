package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.pat.api.bo.EsSearchBO;
import com.pat.api.bo.EsSuggestBO;
import com.pat.api.service.PoetServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsSearchServiceTest
 *
 * @author chouway
 * @date 2022.03.03
 */
public class PoetEsSearchServiceTest extends PoetServiceTest {

    @Autowired
    private PoetEsSearchService poetEsSearchService;

    /**
     * 2022-03-03 16:32:41.267  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->esSearchBO={"key":"关山"}
     * 2022-03-03 16:32:41.431  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->result={"took":7,"timed_out":false,"_shards":{"total":2,"successful":2,"skipped":0,"failed":0},"hits":{"total":{"value":1,"relation":"eq"},"max_score":6.1652126,"hits":[{"_index":"poet-info_v0","_type":"_doc","_id":"1","_score":6.1652126,"_source":{"_class":"com.pat.app.poetry.synch.eo.PoetInfoEO","id":1,"title":"度关山","content":"天地间，人为贵。立君牧民，为之轨则。车辙马迹，经纬四极。黜陟幽明，黎庶繁息。於铄贤圣，总统邦域。封建五爵，井田刑狱。有燔丹书，无普赦赎。皋陶甫侯，何有失职？嗟哉后世，改制易律。劳民为君，役赋其力。舜漆食器，畔者十国，不及唐尧，采椽不斫。世叹伯夷，欲以厉俗。侈恶之大，俭为共德。许由推让，岂有讼曲？兼爱尚同，疏者为戚。","author":"曹操","propKeys":["作品名称","创作年代","作者","作品出处","作品体裁"],"properties":{"作品名称":"度关山","创作年代":"东汉","作者":"曹操","作品出处":"《曹操集》","作品体裁":"四言诗"}}}]},"aggregations":{"num_perKey":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"作品体裁","doc_count":1},{"key":"作品出处","doc_count":1},{"key":"作品名称","doc_count":1},{"key":"作者","doc_count":1},{"key":"创作年代","doc_count":1}]}}}
     * 2022-03-03 16:32:41.469  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggs-->aggsPropKeys=[作品体裁, 作品出处, 作品名称, 作者, 创作年代]
     * 2022-03-03 16:32:41.493  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggs-->result={"took":8,"timed_out":false,"_shards":{"total":2,"successful":2,"skipped":0,"failed":0},"hits":{"total":{"value":1,"relation":"eq"},"max_score":null,"hits":[]},"aggregations":{"vals_perkey_0":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"四言诗","doc_count":1}]},"vals_perkey_1":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"《曹操集》","doc_count":1}]},"vals_perkey_2":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"度关山","doc_count":1}]},"vals_perkey_3":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"曹操","doc_count":1}]},"vals_perkey_4":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"东汉","doc_count":1}]}}}
     */
    @Test
    public void search(){
        EsSearchBO esSearchBO = new EsSearchBO();
        esSearchBO.setKey("关山");
        log.info("search-->esSearchBO={}", JSON.toJSONString(esSearchBO));
        String result = poetEsSearchService.search(esSearchBO);
        log.info("search-->result={}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray aggPropKeys = (JSONArray)JSONPath.eval(jsonObject, "$.aggregations.num_perKey.buckets.key");
        if(aggPropKeys!=null&&aggPropKeys.size()>0){
            List<String> aggsPropKeys = new ArrayList<String>();
            for (Object aggPropKey : aggPropKeys) {
                aggsPropKeys.add(aggPropKey.toString());
            }
            esSearchBO.setAggsPropKeys(aggsPropKeys);
            log.info("aggs-->aggsPropKeys={}", aggsPropKeys);
            result = poetEsSearchService.aggs(esSearchBO);
            log.info("aggs-->result={}", result);
        }
    }

    @Test
    public void suggest(){
        String keyword = "人";
        log.info("suggest-->keyword={}", keyword);
        EsSuggestBO esSuggestBO = new EsSuggestBO();
        esSuggestBO.setKeyword(keyword);
        List<EsSuggestBO> suggests = poetEsSearchService.suggest(esSuggestBO);
        log.info("suggest-->suggests={}", JSON.toJSONString(suggests));

    }
}