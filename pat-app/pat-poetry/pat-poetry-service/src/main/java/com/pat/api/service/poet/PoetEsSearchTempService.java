package com.pat.api.service.poet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.pat.api.exception.BusinessException;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;
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
            Map<String,Object> jsonEntity = new HashMap<String,Object>();
            Map<String,Object>  contentEntity = new HashMap<String,Object>();
            contentEntity.put("lang","mustache");
            jsonEntity.put("script",contentEntity);
            for (File file : files) {
                String tempId = FileUtil.getPrefix(file);
                String source = FileUtil.readString(file, StandardCharsets.UTF_8);
                log.info("pushSearchTemp2Es-->tempId={},source={}", tempId,source);
                Request request = new Request("POST", "_scripts/"+tempId);
                contentEntity.put("source",source);
                String jsonEntityStr = JSON.toJSONString(jsonEntity);
                request.setJsonEntity(jsonEntityStr);
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
            Map<String,Object> jsonEntity = new HashMap<String,Object>();
            jsonEntity.put("params",params);
            request.setJsonEntity(JSON.toJSONString(jsonEntity));
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
           return compile.execute(params);
        }catch(Exception e){
            log.error("error:-->[tempId, params]={}",JSON.toJSONString(new Object[]{tempId, params}),e);
           throw new BusinessException("模版渲染失败");
        }
    }

    @Override
    public String searchByTemp(String tempId, Object params, String indexName) {
        try{
            Request request = new Request(Method.POST.toString(), String.format("%s/_search/template",indexName));
            Map<String,Object> jsonEntity = new HashMap<String,Object>();
            jsonEntity.put("id",tempId);
            jsonEntity.put("params",params);
            request.setJsonEntity(JSON.toJSONString(jsonEntity));
            return reqES(request);
        }catch (BusinessException e){
            log.error("busi error:{}-->[tempId, params, indexName]={}",e.getMessage(),JSON.toJSONString(new Object[]{tempId, params, indexName}),e);
            throw e;
        }catch (Exception e){
            log.error("error:{}-->[tempId, params, indexName]={}",e.getMessage(),JSON.toJSONString(new Object[]{tempId, params, indexName}),e);
           throw new BusinessException("搜索失败");
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
        log.info("getTemps-->statusCode={},result={}", statusCode,result);
        if(statusCode != HttpStatus.HTTP_OK){
            throw new BusinessException("处理失败:"+result);
        }
        return result;
    }
}
