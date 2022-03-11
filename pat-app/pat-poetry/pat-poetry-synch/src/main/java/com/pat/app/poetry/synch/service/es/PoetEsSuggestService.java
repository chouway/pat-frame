package com.pat.app.poetry.synch.service.es;

import com.alibaba.fastjson.JSON;
import com.pat.api.constant.EsConstant;
import com.pat.api.constant.PatConstant;
import com.pat.api.entity.PoetAuthor;
import com.pat.api.entity.PoetInfo;
import com.pat.api.entity.PoetSuggest;
import com.pat.api.mapper.PoetAuthorMapper;
import com.pat.api.mapper.PoetContentMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.api.mapper.PoetSuggestMapper;
import com.pat.app.poetry.synch.eo.PoetSuggestEO;
import com.pat.app.poetry.synch.repo.PoetSuggestRepository;
import com.pat.app.poetry.synch.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PoetSuggestService
 *
 * @author chouway
 * @date 2022.03.10
 */
@Slf4j
@Service
public class PoetEsSuggestService {

    @Autowired
    private PoetSuggestRepository poetSuggestRepository;

    @Autowired
    private PoetAuthorMapper poetAuthorMapper;

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetContentMapper poetContentMapper;

    @Autowired
    private PoetSuggestMapper poetSuggestMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 初始化建议
     * 直接 sql直接 添加处理
     */
    @Transactional
    public void initPoetSuggest() {
        String initPoetSuggestAuthorSQL = FileUtils.getContent("classpath:sql/init-poet-suggest-author.sql");
        log.info("initPoetSuggest-->initPoetSuggestAuthorSQL={}", initPoetSuggestAuthorSQL);

        String initPoetSuggestInfoSQL = FileUtils.getContent("classpath:sql/init-poet-suggest-info.sql");
        log.info("initPoetSuggest-->initPoetSuggestInfoSQL={}", initPoetSuggestInfoSQL);


        String initPoetSuggestAuthorInfoSQL = FileUtils.getContent("classpath:sql/init-poet-suggest-author-info.sql");
        log.info("initPoetSuggest-->initPoetSuggestAuthorInfoSQL={}", initPoetSuggestAuthorInfoSQL);

        String initPoetSuggestParagraphSQL = FileUtils.getContent("classpath:sql/init-poet-suggest-paragraph.sql");
        log.info("initPoetSuggest-->initPoetSuggestParagraphSQL={}", initPoetSuggestParagraphSQL);
        jdbcTemplate.batchUpdate(initPoetSuggestAuthorSQL,initPoetSuggestInfoSQL,initPoetSuggestAuthorInfoSQL,initPoetSuggestParagraphSQL);
    }
    /**
     * 同步ES建议
     */
    public void synchPoetSuggests() {
        int total = 0;
        int pageNumber = 1;
        int pageSize = 500;
        PageResult<PoetSuggest> page = null;
        do{
            page = poetSuggestMapper.createLambdaQuery().andEq(PoetSuggest::getEsStatus,PatConstant.INIT).page(pageNumber, pageSize);
            List<PoetSuggest> list = page.getList();
            List<Long> infoIds = new ArrayList<Long>();
            for (PoetSuggest poetSuggest : list) {
                this.synchPoetSuggest(poetSuggest);
                ++total;
            }
            ++pageNumber;
        }while(pageNumber<page.getTotalPage());
        log.info("synchPoetSuggests-->total={}", total);
        
    }

    private void synchPoetSuggest(PoetSuggest poetSuggest) {
        PoetSuggestEO poetSuggestEO = new PoetSuggestEO();
        BeanUtils.copyProperties(poetSuggest,poetSuggestEO);
        poetSuggestEO.setFullPinyin(poetSuggestEO.getKeyword());
        poetSuggestEO.setPrefixPinyin(poetSuggestEO.getKeyword());
        poetSuggestEO.setSuggestText(poetSuggest.getKeyword());
        poetSuggestEO.setUpdateTs(new Date());
        try{
            poetSuggestRepository.save(poetSuggestEO);
            
            this.saveEsStatus(poetSuggest.getId(),PatConstant.TRUE,poetSuggest.getVersion());
        }catch(Exception e){
            if(EsConstant.isOK(e.getMessage())){
                this.saveEsStatus(poetSuggest.getId(),PatConstant.TRUE,poetSuggest.getVersion());
            }else{
                this.saveEsStatus(poetSuggest.getId(),PatConstant.FALSE,poetSuggest.getVersion());
                log.error("synchPoetEs error:-->[[]]={}", JSON.toJSONString(new Object[]{poetSuggest}),e);
            }
        }
    }

    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEsStatus(Long id, String esStatus, Long version) {
        PoetSuggest poetSuggest = new PoetSuggest();
        poetSuggest.setId(id);
        poetSuggest.setEsStatus(esStatus);
        poetSuggest.setUpdateTs(new Date());
        poetSuggest.setVersion(version);
        poetSuggestMapper.updateTemplateById(poetSuggest);
    }


}
