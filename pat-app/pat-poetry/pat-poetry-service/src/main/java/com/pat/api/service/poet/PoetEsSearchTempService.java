package com.pat.api.service.poet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.pat.api.exception.BusinessException;
import com.pat.starter.cache.annotation.LzxLockDistributed;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * PoetEsSearchTempService
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
@Service
public class PoetEsSearchTempService implements IPoetEsSearchTempService {

    private String SEARCH_TEMP_DIR = "classpath:_scripts/searchTemp";

    @Autowired
    private RestClientBuilder restClientBuilder;

    @Override
    public int pushSearchTemp2Es() {
        try{
            File dirFile = ResourceUtils.getFile(SEARCH_TEMP_DIR);
            File[] files = dirFile.listFiles();
            Map<String,Object> jsonEntityMap = new HashMap<String,Object>();
            Map<String,Object>  contentEntity = new HashMap<String,Object>();
            contentEntity.put("lang","mustache");
            jsonEntityMap.put("script",contentEntity);
            for (File file : files) {
                String tempId = FileUtil.getPrefix(file);
                String source = FileUtil.readString(file, StandardCharsets.UTF_8);
                log.info("pushSearchTemp2Es-->tempId={},source={}", tempId,source);
                Request request = new Request("POST", "_scripts/"+tempId);
                contentEntity.put("source",source);
                String jsonEntity = JSON.toJSONString(jsonEntityMap);
//              jsonEntity = StringEscapeUtils.unescapeJson(jsonEntity);
                request.setJsonEntity(jsonEntity);
                reqES(request);
            }
            return files.length;
        }catch(Exception e){
            log.error("error:-->[]={}", JSON.toJSONString(new Object[]{}),e);
           throw new BusinessException("推送搜索模版失败");
        }
    }

    @Override
    public String getEsSearchTemps() {
        try{
            Request request = new Request("GET", "_cluster/state/metadata?pretty&filter_path=metadata.stored_scripts");
            return reqES(request);
        }catch(Exception e){
            log.error("error:-->[]={}",JSON.toJSONString(new Object[]{}),e);
           throw new BusinessException("获取ES模版清单失败");
        }
    }

    @Override
    public String renderSearchTemp(String tempId, Object params) {
        try{
            Request request = new Request(Method.GET.toString(), String.format("_render/template/%s",tempId));
            Map<String,Object> jsonEntityMap = new HashMap<String,Object>();
            jsonEntityMap.put("params",params);
            String jsonEntity = JSON.toJSONString(jsonEntityMap);
//          jsonEntity = StringEscapeUtils.unescapeJson(jsonEntity);
            request.setJsonEntity(jsonEntity);
            return reqES(request);
        }catch (BusinessException e){
            log.error("busi error:{}-->[tempId, params]={}",e.getMessage(),JSON.toJSONString(new Object[]{tempId, params}),e);
           throw e;
        }catch (Exception e){
            log.error("error:{}-->[tempId, params]={}",e.getMessage(),JSON.toJSONString(new Object[]{tempId, params}),e);
           throw new BusinessException("ES模版渲染失败");
        }

    }

    @Override
    public String renderSearchTempLocal(String tempId, Object params) {
        try{
           log.info("renderSearchTempLocal-->tempId={},params={}", tempId,params);
           File tempFile = ResourceUtils.getFile(SEARCH_TEMP_DIR+File.separator+tempId+".mustache");
           String tempContent = FileUtil.readString(tempFile, StandardCharsets.UTF_8);
           Template compile = Mustache.compiler().compile(tempContent);
            String result = compile.execute(params);
            log.info("renderSearchTempLocal-->result={}", result);
            return result;
        }catch(Exception e){
            log.error("error:-->[tempId, params]={}",JSON.toJSONString(new Object[]{tempId, params}),e);
           throw new BusinessException("模版渲染失败");
        }
    }

    @Override
    public String searchByTemp(String indexName, Object params, String tempId) {
        try{
            Request request = new Request(Method.POST.toString(), String.format("%s/_search/template",indexName));
            Map<String,Object> jsonMap = new HashMap<String,Object>();
            jsonMap.put("id",tempId);
            jsonMap.put("params",params);
            String jsonEntity = JSON.toJSONString(jsonMap);
//          sonEntity = StringEscapeUtils.unescapeJson(jsonEntity);
            request.setJsonEntity(jsonEntity);
            return reqES(request);
        }catch (BusinessException e){
            log.error("busi error:{}-->[indexName, params, tempId]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, params, tempId}),e);
            throw e;
        }catch (Exception e){
            log.error("error:{}-->[indexName, params, tempId]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, params, tempId}),e);
           throw new BusinessException("搜索失败");
        }
    }

    @Override
    @LzxLockDistributed("ui-${indexName}-${docId}")
    public String updateIndex(String indexName, Map<String, Object> params, Long docId) {
        try{
            Map<String,Object> jsonMap = new HashMap<String,Object>();
            jsonMap.put("doc",params);
            String jsonEntity = JSON.toJSONString(jsonMap);
//            jsonEntity = StringEscapeUtils.unescapeJson(jsonEntity);
            log.info("updateIndex-->indexName={},docId={},jsonEntity={},", indexName,docId,jsonEntity);
            Request request = new Request(Method.POST.toString(), String.format("%s/_update/%s",indexName,docId));
            request.setJsonEntity(jsonEntity);
            String result = reqES(request);
            log.info("updateIndex-->result={}", result);
            return result;
        }catch (BusinessException e){
            log.error("busi error:{}-->[indexName, params, docId]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, params, docId}),e);
            throw e;
        }catch (Exception e){
            log.error("error:{}-->[indexName, params, docId]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, params, docId}),e);
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public String get(String indexName,Long id) {
        try{
           Request request = new Request(Method.GET.toString(), String.format("%s/_doc/%s",indexName,id));
           return reqES(request);
        }catch (BusinessException e){
            log.error("busi error:{}-->[indexName, id]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, id}),e);
           throw e;
        }catch (Exception e){
            log.error("error:{}-->[indexName, id]={}",e.getMessage(),JSON.toJSONString(new Object[]{indexName, id}),e);
           throw new BusinessException("获取信息失败");
        }
    }


    private String reqES(Request request) throws Exception {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        return dealResp(response);
    }

    private String dealResp(Response response) throws Exception {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
//      log.info("getTemps-->statusCode={},result={}", statusCode,result);
        if(statusCode != HttpStatus.HTTP_OK){
            throw new BusinessException("处理失败:"+result);
        }
        return result;
    }
}
