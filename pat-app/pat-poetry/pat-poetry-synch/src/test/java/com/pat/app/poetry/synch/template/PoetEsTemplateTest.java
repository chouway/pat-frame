package com.pat.app.poetry.synch.template;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Method;
import com.pat.app.poetry.synch.PoetSynchTest;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cn.hutool.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
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
public class PoetEsTemplateTest extends PoetSynchTest {

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
//        request.setProfile(true);

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

    /**
     * 新增或者更新
     */
    @Test
    @SneakyThrows
    public void saveTemp(){
        String templateid = "test_0";
        String SCRIPT_META = "{\"script\":{\"lang\":\"mustache\",\"source\":%s}}";
        String tempSource = "{\"size\":0,\"aggs\":{ {{#aggs_infos}}{{/aggs_infos}} }}";
        String josnEntity = String.format(SCRIPT_META, tempSource);
        log.info("saveTemp-->templateid={},josnEntity={}", josnEntity);

        Request scriptRequest = new Request("POST", "_scripts/"+templateid);
        scriptRequest.setJsonEntity(josnEntity);


        RestClient lowLevelClient = restHighLevelClient.getLowLevelClient();
        Response response = null;
        try {
            response = lowLevelClient.performRequest(scriptRequest);
        } catch (Exception e) {
            log.error("error:saveTemp-->e={}", e,e);
            return;
        }
        dealResp(response);
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


}
