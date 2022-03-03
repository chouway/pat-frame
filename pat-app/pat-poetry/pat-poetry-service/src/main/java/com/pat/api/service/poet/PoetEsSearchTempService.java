package com.pat.api.service.poet;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.pat.api.exception.BusinessException;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

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

    @Override
    public int pushSearchTemp2Es() {
        try{
            File dirFile = ResourceUtils.getFile(SEARCH_TEMP_DIR);
            File[] files = dirFile.listFiles();
            for (File file : files) {
                String name = FileUtil.getPrefix(file);
                String content = FileUtil.readString(file, StandardCharsets.UTF_8);
                log.info("pushSearchTemp2Es-->name={},content={}", name,content);

            }
            return files.length;
        }catch(Exception e){
            log.error("error:-->[]={}", JSON.toJSONString(new Object[]{}),e);
           throw new BusinessException("推送搜索模版失败");
        }
    }

    @Override
    public String renderSearchTemp(String tempId, Object params) {
        return null;
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
    public String searchByTemp(String tempId, String paramsJson, String indexName) {
        return null;
    }
}
