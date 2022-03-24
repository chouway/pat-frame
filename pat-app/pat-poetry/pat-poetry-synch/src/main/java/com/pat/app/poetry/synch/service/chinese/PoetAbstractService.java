package com.pat.app.poetry.synch.service.chinese;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pat.api.entity.PoetAuthor;
import com.pat.api.entity.PoetChapter;
import com.pat.api.entity.PoetInfo;
import com.pat.api.entity.PoetSet;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.*;
import com.pat.app.poetry.synch.bo.PoetSetInfoBO;
import com.pat.app.poetry.synch.service.baike.PoetBaikeService;
import com.pat.app.poetry.synch.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

/**
 * PoetAbstractService
 *
 * @author chouway
 * @date 2022.02.25
 */
@Slf4j
public abstract class PoetAbstractService {

    @Value("${app.poetry.chinese.local-dir}")
    protected String LOCAL_DIR;

    @Autowired
    protected PoetSetMapper poetSetMapper;

    @Autowired
    private PoetChapterMapper poetChapterMapper;

    @Autowired
    protected PoetInfoMapper poetInfoMapper;

    @Autowired
    protected PoetAuthorMapper poetAuthorMapper;

    @Autowired
    protected PoetContentMapper poetContentMapper;

    /*
    @Autowired
    private PoetBaikeService poetBaikeService;
    */

    /**
     * 初始化文集数据
     * @return
     */
    protected abstract PoetSet initPoetSet();

    /**
     * 同步文
     * @param jsonObject
     */
    public abstract PoetInfo synchInfo(JSONObject jsonObject,int index);

    /**
     * 同步数据
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchData() {
           PoetSet poetSet = this.getPoetSet();
           String infos = poetSet.getInfos();
           List<PoetSetInfoBO> poetSetInfos = JSON.parseArray(infos, PoetSetInfoBO.class);
           if(CollectionUtils.isEmpty(poetSetInfos)){
               return;
           }
           for (PoetSetInfoBO poetSetInfo : poetSetInfos) {
               Integer dealNum = poetSetInfo.getDealNum();
               String fileContent = this.getFileContent(poetSetInfo.getFile());
                if(fileContent.indexOf("[")==0){
                    JSONArray jsonArray = JSON.parseArray(fileContent);
                    if(jsonArray==null){
                        return;
                    }
                    for (int i = dealNum; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        try{
                            PoetInfo poetInfo = this.synchInfo(jsonObject,i);
                        }catch(Exception e){
                            log.error("error:synchData-->e={}", e,e);
                            if(i>dealNum){
                                poetSetInfo.setDealNum(i);
                                this.saveInfos(JSON.toJSONString(poetSetInfos));
                            }
                            throw new BusinessException("同步文失败:poetSetInfo="+JSON.toJSONString(poetSetInfo),e);
                        }
                    }
                    if(dealNum < jsonArray.size()){
                        poetSetInfo.setDealNum(jsonArray.size());
                        this.saveInfos(JSON.toJSONString(poetSetInfos));
                    }
                }else{
                    if(dealNum>0){
                        continue;
                    }
                    JSONObject jsonObject = JSON.parseObject(fileContent);
                    PoetInfo poetInfo = this.synchInfo(jsonObject,0);
                    poetSetInfo.setDealNum(1);
                    this.saveInfos(JSON.toJSONString(poetSetInfos));
                }

           }
           //手动执行文集
//            poetBaikeService.synchPoetBaike();

   };

    /**
     * 获取 文集 PoetSet
     * 没有则初始化并创建保存 并返回
     * @param jsonObject
     */
    @Transactional
    public PoetSet getPoetSet(){
        PoetSet initPoetSet = this.initPoetSet();
        if(initPoetSet == null){
            throw new RuntimeException("未初始化");
        }
        PoetSet poetSet = this.getSetByNameAndType(initPoetSet);
        if(poetSet != null){
            return poetSet;
        }
        synchronized(this.getClass()){
            poetSet = this.getSetByNameAndType(initPoetSet);
            if(poetSet!=null){
                return poetSet;
            }
            poetSet = initPoetSet;
            poetSet.setUpdateTs(new Date());
            poetSetMapper.insert(poetSet);
            log.info("insert PoetSet-->poetSet={}", JSON.toJSONString(poetSet));
            return poetSet;
        }

    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInfos(String infos){
        PoetSet poetSet = this.getPoetSet();
        PoetSet poetSetUpdate = new PoetSet();
        poetSetUpdate.setId(poetSet.getId());
        poetSetUpdate.setInfos(infos);
        poetSetUpdate.setUpdateTs(new Date());
        int i = poetSetMapper.updateTemplateById(poetSetUpdate);
        log.info("saveInfoNum-->poetSetUpdate={}", JSON.toJSONString(poetSetUpdate));
    }


    /**
     * 根据名称及类型获取文集
     * @param poetSet
     * @return
     */
    public PoetSet getSetByNameAndType(PoetSet poetSet) {
        return poetSetMapper.createLambdaQuery().andEq(PoetSet::getSetType, poetSet.getSetType()).andEq(PoetSet::getNameEn, poetSet.getNameEn()).singleSimple();
    }

    /**
     * 根据名称及类型获取文集
     * @param initPoetSet
     * @return
     */
    public PoetInfo getInfoByTitleAndSetId(PoetInfo poetInfo) {
        return poetInfoMapper.createLambdaQuery().andEq(PoetInfo::getSetId, poetInfo.getSetId()).andEq(PoetInfo::getTitle, poetInfo.getTitle()).singleSimple();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PoetChapter getPoetChapter(Long setId,String chapter){
        PoetChapter poetChapter = poetChapterMapper.createLambdaQuery().andEq(PoetChapter::getSetId, setId).andEq(PoetChapter::getChapter, chapter).singleSimple();
        if(poetChapter == null){
            poetChapter = new PoetChapter();
            poetChapter.setChapter(chapter);
            poetChapter.setSetId(setId);
            Long count = poetChapterMapper.createLambdaQuery().andEq(PoetChapter::getSetId, setId).count();
            poetChapter.setIndex(count.intValue());
            poetChapterMapper.insert(poetChapter);
        }
        return poetChapter;
    }
    /**
     * 根据名称获取作者
     * @param name
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PoetAuthor getAuthorByName(String name){
        PoetAuthor poetAuthor = poetAuthorMapper.createLambdaQuery().andEq(PoetAuthor::getName, name).singleSimple();
        if(poetAuthor == null){
            poetAuthor = new PoetAuthor();
            poetAuthor.setName(name);
            poetAuthorMapper.insert(poetAuthor);
        }
        return poetAuthor;
    }

    /**
     *
     * 获取文件内容
     * @param fileName
     * @return
     */
    protected String getFileContent(String fileName){
            PoetSet poetSet = this.getPoetSet();
            String resourceLocation = this.LOCAL_DIR + File.separator + poetSet.getNameEn() + File.separator + fileName;
            return FileUtils.getContent(resourceLocation);

    }
}


