package com.pat.app.poetry.synch.repo;

import com.alibaba.fastjson.JSON;
import com.pat.api.entity.PoetInfo;
import com.pat.api.mapper.PoetAuthorMapper;
import com.pat.api.mapper.PoetContentMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.app.poetry.synch.PoetSynchTest;
import com.pat.app.poetry.synch.eo.PoetInfoEO;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * PoetInfoRepositoryTest
 * elasticsearch更改mapping(不停服务重建索引)
 * https://www.iteye.com/blog/donlianli-1924721
 * @author chouway
 * @date 2022.03.01
 */
public class PoetInfoRepositoryTest extends PoetSynchTest {

    @Autowired
    private PoetInfoRepository poetInfoRepository;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Autowired
    private PoetAuthorMapper poetAuthorMapper;

    @Autowired
    private RestClientBuilder restClientBuilder;



    /**
     * 当前索引数量
     */
    @Test
    public void count(){
        long count = poetInfoRepository.count();
        log.info("count-->count={}", count);
    }

    @Test
    public void findById(){
        Optional<PoetInfoEO> poetInfoEO = poetInfoRepository.findById(1L);
        log.info("findById-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));

    }

    @Test
    public void page(){
        Pageable pageReq = Pageable.ofSize(1);
        boolean hasNext = false;
        do{
            Page<PoetInfoEO> pageResp = poetInfoRepository.findAll(pageReq);
            log.info("page-->pageResp={}", JSON.toJSONString(pageResp));
            if(hasNext = pageResp.hasNext()){
                pageReq = pageResp.nextPageable();
            }
        }while(hasNext);
    }


    @Test
    public void aliases(){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//        restHighLevelClient.searchTemplate()
//        restHighLevelClient.search()

    }


    @Test
    public void test_save(){
        test_save_1();
        test_save_2();
        test_save_3();
    }
    @Test
    public void test_save_1(){
        Long infoId = 1L;
        Map<String,String> properties = new LinkedHashMap<String,String>();
        properties.put("作品名称","度关山");
        properties.put("创作年代","东汉");
        properties.put("作者","曹操");
        properties.put("作品出处","《曹操集》");
        properties.put("作品体裁","四言诗");
        PoetInfoEO poetInfoEO = getPoetInfoEO(infoId, properties);

        log.info("test str-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
        poetInfoEO = poetInfoRepository.save(poetInfoEO);
        log.info("test end-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
    }

    @Test
    public void test_save_2(){
        Long infoId = 2L;
        Map<String,String> properties = new LinkedHashMap<String,String>();
        properties.put("作品名称","短歌行 其一");
        properties.put("作者","曹操");
        properties.put("创作年代","东汉");
        properties.put("作品出处","《曹操集》");
        properties.put("作品体裁","四言诗");
        properties.put("作品别名","短歌行");
        PoetInfoEO poetInfoEO = getPoetInfoEO(infoId, properties);

        log.info("test str-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
        poetInfoEO = poetInfoRepository.save(poetInfoEO);
        log.info("test end-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
    }

    @Test
    public void test_save_3(){
        Long infoId = 3L;
        Map<String,String> properties = new LinkedHashMap<String,String>();
        properties.put("作品名称","短歌行 其二");
        properties.put("作者","曹操");
        properties.put("创作年代","东汉");
        properties.put("作品出处","《曹操集》");
        properties.put("作品体裁","四言诗");
        properties.put("作品别名","短歌行");
        PoetInfoEO poetInfoEO = getPoetInfoEO(infoId, properties);

        log.info("test str-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
        poetInfoEO = poetInfoRepository.save(poetInfoEO);
        log.info("test end-->poetInfoEO={}", JSON.toJSONString(poetInfoEO));
    }





    private PoetInfoEO getPoetInfoEO(Long infoId, Map<String, String> properties) {
        PoetInfo poetInfo = poetInfoMapper.unique(infoId);
        IndexRequest indexRequest = new IndexRequest();
        PoetInfoEO poetInfoEO = new PoetInfoEO();
        poetInfoEO.setId(poetInfo.getId());
        poetInfoEO.setTitle(poetInfo.getTitle());
        poetInfoEO.setSubtitle(poetInfo.getSubtitle());
        poetInfoEO.setContent(poetContentMapper.getContent(infoId));
        poetInfoEO.setAuthor(poetAuthorMapper.unique(poetInfo.getAuthorId()).getName());

        poetInfoEO.setProperties(properties);
        poetInfoEO.setPropKeys(new ArrayList<String>(properties.keySet()));
        return poetInfoEO;
    }
}