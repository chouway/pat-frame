package com.pat.simple.es.service;

import com.pat.simple.es.listen.PatIndexActionListener;
import com.pat.simple.es.listen.PatUpdateActionListener;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PatEsRestClient
 *
 * https://www.jianshu.com/p/efe84c83185a RestHighLevelClient
 * 通用连接
 * @author chouway
 * @date 2022.02.23
 */
@Slf4j
@Service
public class PatEsRestService implements InitializingBean {

    private RestHighLevelClient restHighLevelClient = null;

    @Autowired
    private PatIndexActionListener patIndexActionListener;

    @Autowired
    private PatUpdateActionListener patUpdateActionListener;

    @Autowired
    private RestClientBuilder restClientBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.restHighLevelClient = new RestHighLevelClient(restClientBuilder);
    }


    /**
     * 异步发起建索引
     * @param indexRequest
     */
    public void indexAsync(IndexRequest indexRequest){
        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, patIndexActionListener);
    }


    /**
     * 异步发起建索引
     * @param indexRequest
     */
    public void indexAsync(IndexRequest indexRequest, RequestOptions options, ActionListener<IndexResponse> listener){
        restHighLevelClient.indexAsync(indexRequest, options,listener);
    }

    /**
     * 异步发起更新索引
     * @param updateRequest
     */
    public void updateAsync(UpdateRequest updateRequest){
        restHighLevelClient.updateAsync(updateRequest, RequestOptions.DEFAULT, patUpdateActionListener);
    }


    /**
     * 异步发起更新索引
     * @param indexRequest
     */
    public void updateAsync(UpdateRequest updateRequest, RequestOptions options, ActionListener<UpdateResponse> listener){
        restHighLevelClient.updateAsync(updateRequest, options,listener);
    }

}
