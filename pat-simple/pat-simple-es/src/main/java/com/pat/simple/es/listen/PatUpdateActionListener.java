package com.pat.simple.es.listen;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.stereotype.Component;

/**
 * PatRestActionListener
 *
 * @author chouway
 * @date 2022.02.23
 */
@Slf4j
@Component
public class PatUpdateActionListener implements ActionListener<UpdateResponse> {

    @Override
    public void onResponse(UpdateResponse updateResponse) {
        log.info("onResponse-->updateResponse={}", updateResponse);
    }

    @Override
    public void onFailure(Exception e) {
        log.error("error:onFailure-->e={}", e,e);
    }
}
