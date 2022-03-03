package com.pat.api.service.template;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.pat.api.entity.PoetAuthor;
import com.pat.api.service.PoetServiceTest;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PatEsTemplateTest
 *
 * https://blog.csdn.net/qq_34168515/article/details/108352272
 * 2020年 elasticsearch7.9 搜索模板 restHighLevelClient 操作SearchTemplateAPI用法详解
 *
 *
 * @author chouway
 * @date 2022.03.02
 */
public class SimpleEsTemplateTest extends PoetServiceTest {

    @Autowired
    private RestClientBuilder restClientBuilder;

    private RestHighLevelClient restHighLevelClient;

    @BeforeEach
    public void before(){
        this.restHighLevelClient = new RestHighLevelClient(restClientBuilder);
    }

    @AfterEach
    public void after(){
        IoUtil.close(restHighLevelClient);
    }

    /**
     * _render/template
     * 渲染效果
     */
    @Test
    public void renderTemp(){
        String templateid = "test_1";
        Map<String,Object> map = new HashMap<String,Object>();

        SearchTemplateRequest request = new SearchTemplateRequest();

        request.setScriptType(ScriptType.STORED);

        request.setScript(templateid);

        request.setSimulate(true);

        request.setScriptParams(map);

        SearchTemplateResponse renderResponse = null;
        try {
            renderResponse = this.restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
        } catch (Exception  e) {//ElasticsearchStatusException  resource_not_found_exception
            log.error("error:getTemp-->e={}", e,e);
            return;
        }
        BytesReference source = renderResponse.getSource();

        String result = source.utf8ToString();
        log.info("getTemp-->result={}", result);

    }

    @Test
    public void searchByTemp2(){
        String tempId = "test_1";
        String indexId = "poet-info";
        Request request = new Request(Method.POST.toString(), String.format("%s/_search/template",indexId));
        Map<String,Object> jsonEntity = new HashMap<String,Object>();
        jsonEntity.put("id",tempId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("field","propKeys");
        map.put("size",10);
        jsonEntity.put("params",map);
        request.setJsonEntity(JSON.toJSONString(jsonEntity));
        reqES(request);
    }

    /**
     * indexName 按模版搜索
     */
    @Test
    public void searchByTemp(){
        String templateid = "test_1";
        String indexName = "poet-info";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("field","propKeys");
        map.put("size",10);
        SearchTemplateRequest request = new SearchTemplateRequest();

        request.setRequest(new SearchRequest(indexName));
        request.setScriptType(ScriptType.STORED);

        request.setScript(templateid);
        request.setScriptParams(map);

        SearchTemplateResponse renderResponse = null;
        try {
            renderResponse = this.restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
        } catch (Exception  e) {//ElasticsearchStatusException  resource_not_found_exception
            log.error("error:getTemp-->e={}", e,e);
            return;
        }
        Aggregations aggregations = renderResponse.getResponse().getAggregations();
        Aggregation test = aggregations.get("test");
        List<? extends Terms.Bucket> buckets = ((ParsedStringTerms) test).getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            log.info("searchByTemp-->key={},docCount={}", key,docCount);
        }

    }



    /**
     * 新增或者更新
     */
    @Test
    @SneakyThrows
    public void saveTemp(){
        String templateid = "test_0";
        String SCRIPT_META = "{\"script\":{\"lang\":\"mustache\",\"source\":%s}}";
        String tempSource = "{\"size\":0,\"aggs\":\"\"\"{ {{#aggs_infos}}{{/aggs_infos}} }}\"\"\"";
        String josnEntity = String.format(SCRIPT_META, tempSource);
        log.info("saveTemp-->templateid={},josnEntity={}", josnEntity);

        Request request = new Request("POST", "_scripts/"+templateid);
        request.setJsonEntity(josnEntity);
        reqES(request);
    }


    @Test
    public void getTemp(){
        String tempId = "test_1";
        Request request = new Request(Method.GET.toString(), String.format("_scripts/%s",tempId));
        reqES(request);
    }

    /**
     * 获取脚本清单
     */
    @Test
    @SneakyThrows
    public void getTemps() {
        //GET _cluster/state/metadata?pretty&filter_path=metadata.stored_scripts
        Request request = new Request("GET", "_cluster/state/metadata?pretty&filter_path=metadata.stored_scripts");
        reqES(request);
    }


    @SneakyThrows
    private String reqES(Request request){
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        return dealResp(response);
    }

    private String dealResp(Response response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        log.info("getTemps-->statusCode={},result={}", statusCode,result);
        if(statusCode != HttpStatus.HTTP_OK){
            throw new RuntimeException("处理失败:"+result);
        }
        return result;
    }



    @Test
    public void mustache(){
        String text = "One, two, {{three}}. Three sir!";
        Template tmpl = Mustache.compiler().compile(text);
        Map<String, String> data = new HashMap<String, String>();
        data.put("three", "five");
        log.info("mustache-->render={}", tmpl.execute(data));


        String tmplStr = "{{#poetAuthor}}{{name}}: {{describe}}{{/poetAuthor}}";
        PoetAuthor poetAuthor = new PoetAuthor();
        poetAuthor.setName("曹操");
        poetAuthor.setDescribe("东汉末年，杰出政治家、军事家、诗人。");
        Map<String, Object> dataT = new HashMap<String, Object>();
        dataT.put("poetAuthor", poetAuthor);
        String render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustache-->render={}", render);

        tmplStr = "{ {{#join}} {{#poetAuthors}}{{name}}: {{describe}}{{/poetAuthors}} {{/join}} }";
        PoetAuthor poetAuthor1 = new PoetAuthor();
        poetAuthor1.setName("曹操1");
        poetAuthor1.setDescribe("东汉末年，杰出政治家、军事家、诗人。1");
        List<PoetAuthor> poetAuthors= new ArrayList<PoetAuthor>();
        poetAuthors.add(poetAuthor);
        poetAuthors.add(poetAuthor1);
        dataT = new HashMap<String, Object>();
        dataT.put("poetAuthors", poetAuthors);
        render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustache-->render={}", render);

    }

    /**
     * mustache允许嵌套
     */
    @Test
    public void mustacheNest(){
        String tmplStr = "{{#a}}{{#b}}{{b}}{{/b}}{{/a}}";
        Map<String, Object> dataT = new HashMap<String, Object>();
//        dataT.put("a", "1");
        dataT.put("b", "b");
        String render = Mustache.compiler().compile(tmplStr).execute(dataT);
        log.info("mustacheNest-->render={}", render);
    }



    @Test
    public void tempA(){
        String SCRIPT_META = "{\"script\":{\"lang\":\"mustache\",\"source\":{%s}}";
        String tempSource = "{\"size\":0,\"aggs\":{{#aggs_infos}}{\"{{aggs_name}}\":{\"terms\":{\"field\":\"{{field}}\",\"size\":{{size}},}{{/aggs_infos}}}";
        String tempContent = String.format(SCRIPT_META,tempSource);
        log.info("tempA-->tempContent={}", tempContent);
        Map<String,List<Map<String,Object>>> runMap = new HashMap<String,List<Map<String,Object>>>();
        List<Map<String,Object>> aggs_infos = new ArrayList<Map<String,Object>>();
        Map<String,Object> aggs_info = new HashMap<String,Object>();
        aggs_info.put("aggs_name","num_per_propKey");
        aggs_info.put("field","propKeys");
        aggs_info.put("size",10);
        aggs_infos.add(aggs_info);
        runMap.put("aggs_infos",aggs_infos);
        String render = Mustache.compiler().compile(tempContent).execute(runMap);
        log.info("tempA-->render={}", render);


    }

}
