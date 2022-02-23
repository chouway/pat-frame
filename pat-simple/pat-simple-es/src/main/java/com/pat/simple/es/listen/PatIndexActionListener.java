package com.pat.simple.es.listen;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.stereotype.Component;

/**
 * PatRestActionListener
 *
 * @author chouway
 * @date 2022.02.23
 */
@Slf4j
@Component
public class PatIndexActionListener implements ActionListener<IndexResponse> {
    /**
     * eg
     * 2022-02-23 17:48:11.657  INFO 8660 --- [           main] com.pat.simple.es.PatSimpleEsTest        : indexAsync-->docMap={organizer=Lee, name=Elasticsearch Denver}
     *
     * 2022-02-23 17:48:11.933  INFO 8660 --- [           main] com.pat.simple.es.PatSimpleEsTest        : sleep str-->time=10 s
     * 2022-02-23 17:48:12.215  INFO 8660 --- [/O dispatcher 1] c.p.s.es.listen.PatRestActionListener    : onResponse-->indexResponse=IndexResponse[index=get-together_group,type=_doc,id=1,version=1,result=created,seqNo=0,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
     * @param indexResponse
     */
    @Override
    public void onResponse(IndexResponse indexResponse) {
          log.info("onResponse-->indexResponse={}", indexResponse);
    }

    @Override
    public void onFailure(Exception e) {
        log.error("error:onFailure-->e={}", e,e);
    }
}
