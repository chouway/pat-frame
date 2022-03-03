package com.pat.app.poetry.synch.service.baike;

import com.alibaba.fastjson.JSON;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * PoetBaikeService
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
@Service
public class PoetBaikeService {

    @Autowired
    private PoetInfoMapper poetInfoMapper;
    /**
     * 为id添加百科相关的属性
     * @param infoId
     */
    public void synchBaiduBaikeProps(Long infoId){
        try{
            Map<String, String> titleAndAuthor = poetInfoMapper.getTitleAndAuthorById(infoId);
            String title = titleAndAuthor.get("title");
            String author = titleAndAuthor.get("author");
            String searchKey = String.format("%s(%s) - 百度百科",title,author);
            log.info("synchBaiduBaikeProps-->infoId={},searchKey={}", infoId,searchKey);
            String searchUrl = String.format("https://baike.baidu.com/search?word=%s&pn=0&rn=0&enc=utf8",searchKey);

        }catch(Exception e){
            log.error("error:-->[infoId]={}", JSON.toJSONString(new Object[]{infoId}),e);
           throw new BusinessException("添加百科属性失败");
        }
    };
}
