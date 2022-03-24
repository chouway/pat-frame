package com.pat.app.poetry.synch.service.chinese.chuci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pat.api.constant.PoetSetConstant;
import com.pat.api.entity.*;
import com.pat.app.poetry.synch.bo.PoetSetInfoBO;
import com.pat.app.poetry.synch.service.chinese.PoetAbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PoetChuciService
 *
 * @author chouway
 * @date 2022.02.28
 */
@Slf4j
@Service
public class PoetChuciService extends PoetAbstractService {

    @Override
    protected PoetSet initPoetSet() {
        PoetSet initPoetSet = new PoetSet();
        initPoetSet.setNameCn("楚辞");
        initPoetSet.setNameEn("chuci");
        initPoetSet.setDescribe("楚辞，也作“楚词”，是战国时期楚国诗人屈原创作的一种新的诗歌体裁。屈原的抒情长诗《离骚》，具有浪漫主义风格，是楚辞的代表作，楚辞因此又称为“骚体”。");
        initPoetSet.setRemark("");
        List<PoetSetInfoBO> infos = new ArrayList<PoetSetInfoBO>();
        PoetSetInfoBO poetSetInfo = new PoetSetInfoBO();
        poetSetInfo.setFile("chuci.json");
        poetSetInfo.setDealNum(0);
        infos.add(poetSetInfo);
        initPoetSet.setInfos(JSON.toJSONString(infos));
        initPoetSet.setSetType(PoetSetConstant.SET_TYPE_CHINESE_CLASSIC);
        return initPoetSet;
    }

    /**
     * ``` json
         {
             "title": "离骚",
             "section": "离骚",
             "author": "屈原",
             "content": [
             "帝高阳之苗裔兮，朕皇考曰伯庸。",
             "摄提贞于孟陬兮，惟庚寅吾以降。",
             "皇览揆余初度兮，肇锡余以嘉名：",
             ...
             ]
         }
     * ```
     * @param jsonObject
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PoetInfo synchInfo(JSONObject jsonObject,int index) {
        String author = jsonObject.getString("author");
        PoetAuthor poetAuthor = this.getAuthorByName(author);

        PoetSet poetSet = this.getPoetSet();
        Long setId = poetSet.getId();
        String title = jsonObject.getString("title");
        PoetInfo poetInfo = new PoetInfo();
        poetInfo.setSetId(setId);
        poetInfo.setTitle(title);
        PoetInfo poetInfoDB = this.getInfoByTitleAndSetId(poetInfo);
        if(poetInfoDB != null){
            log.info("synchInfo exist-->title={}", title);
            return poetInfoDB;
        }
        String chapter = jsonObject.getString("section");
        PoetChapter poetChapter = this.getPoetChapter(setId, chapter);
        poetInfoDB  = poetInfo;
        poetInfoDB.setAuthorId(poetAuthor.getId());
        poetInfoDB.setChapterId(poetChapter.getId());
        poetInfoDB.setCount(0L);
        poetInfoDB.setVersion(0L);
        poetInfoDB.setIndex(index);
        poetInfoDB.setUpdateTs(new Date());
        poetInfoMapper.insert(poetInfoDB);
        log.info("synchInfo-->poetInfo={}", JSON.toJSONString(poetInfo));

        JSONArray paragraphs = jsonObject.getJSONArray("content");
        List<PoetContent>  poetContents = new ArrayList<PoetContent>();
        for (int i = 0; i < paragraphs.size(); i++) {
            String paragraph = paragraphs.getString(i);
            PoetContent poetContent = new PoetContent();
            poetContent.setParagraph(paragraph);
            poetContent.setIndex(i);
            poetContent.setInfoId(poetInfoDB.getId());
            poetContents.add(poetContent);
        }
        poetContentMapper.insertBatch(poetContents);
        log.info("synchInfo-->poetContents={}", JSON.toJSONString(poetContents));
        return poetInfoDB;
    }


}
