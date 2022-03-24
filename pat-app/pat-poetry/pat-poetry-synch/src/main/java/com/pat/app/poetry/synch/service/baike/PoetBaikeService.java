package com.pat.app.poetry.synch.service.baike;

import com.alibaba.fastjson.JSON;
import com.pat.api.constant.BaikeConstant;
import com.pat.api.constant.PatConstant;
import com.pat.api.constant.PoetCharConstant;
import com.pat.api.constant.PoetRelConstant;
import com.pat.api.entity.PoetBaike;
import com.pat.api.entity.PoetInfo;
import com.pat.api.entity.PoetProperty;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetAuthorMapper;
import com.pat.api.mapper.PoetBaikeMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.api.mapper.PoetPropertyMapper;
import com.pat.app.poetry.synch.util.DomainUtils;
import com.pat.app.poetry.synch.util.IKAnalyzerUtils;
import com.pat.starter.cache.constant.CacheConstant;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

import java.math.BigDecimal;
import java.util.*;

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

    @Autowired
    private PoetBaikeMapper poetBaikeMapper;

    @Autowired
    private PoetPropertyMapper poetPropertyMapper;

    @Autowired
    private CacheManager cacheManager;

    private final double VALID_MIN_SIMILAR = 0.8d;

    @Value("${app.poetry.baike.sleepT}")
    private Long sleepT;

    /**
     * 同步所有百科
     */
    public void synchPoetBaike(){
        int pageNumber = 1;
        int pageSize = 500;
        PageResult<PoetInfo> page = null;
        do{
            page = poetInfoMapper.createLambdaQuery().andIsNull(PoetInfo::getEsStatus).page(pageNumber, pageSize, PoetInfo::getId);
            List<PoetInfo> list = page.getList();
            List<Long> infoIds = new ArrayList<Long>();
            for (PoetInfo poetInfo : list) {
                Long infoId = poetInfo.getId();
                this.synchBaiduBaikeProps(infoId);
                this.resetBaikeCache(infoId);
            }
            ++pageNumber;
        }while(pageNumber<page.getTotalPage());
    }
    /**
     * 为id添加百科相关的属性
     * @param infoId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void synchBaiduBaikeProps(Long infoId){
        try{

            PoetBaike poetBaike = searchBaike(infoId);
            log.info("synchBaiduBaikeProps-->infoId={},baikeSearchTitle={},status={},baikeUrl={}", infoId,poetBaike.getBaikeSearchTitle(),poetBaike.getStatus(),poetBaike.getBaikeUrl());

            if(sleepT>0){
                Thread.sleep(sleepT);
            }

            //相似度有效 ，待解析 目标百科词条
            if(BaikeConstant.STATUS_SIMILAR_VALID.equals(poetBaike.getStatus())){
                resolveBaike(poetBaike);
            }
        }catch(Exception e){
            log.error("error:-->[infoId]={}", JSON.toJSONString(new Object[]{infoId}),e);
           throw new BusinessException("添加百科属性失败");
        }
    }

    /**
     * 清掉百科缓存
     */
    public void resetBaikeCache(Long infoId){
        String cacheKey = CacheConstant.DAY;
        Cache cache = cacheManager.getCache(cacheKey);
        String cacheVal = "bk_" + infoId;
        cache.evictIfPresent(cacheVal);
    }

    /**
     * 解析百科词条
     *
     * 获取词条标题 简要描述 及 信息栏
     * @param poetBaike
     */
    private void resolveBaike(PoetBaike poetBaike) {
        String baikeUrl = poetBaike.getBaikeUrl();
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Html html = httpClientDownloader.download(baikeUrl);
        String baiduTitle = html.xpath("//head/title/text()").get();
        //标题
        log.info("baidubaikeCitiao-->baiduTitle={}", baiduTitle);
        poetBaike.setBaikeTitle(baiduTitle);

        List<String> lemmaSummarys = html.xpath("//div[@class='lemma-summary']/div[@class='para']/allText()").all();
        //概述
        log.info("baidubaikeCitiao-->lemmaSummarys={}", lemmaSummarys);
        dealBaikeDesc(lemmaSummarys,poetBaike);


        List<String> basicNames = html.xpath("//dt[@class='basicInfo-item']/allText()").all();
        List<String> basicValues = html.xpath("//dd[@class='basicInfo-item']/allText()").all();
        //信息栏
        Map<String,String> basicInfos = new LinkedHashMap<String, String>();
        if(!CollectionUtils.isEmpty(basicNames)&&!CollectionUtils.isEmpty(basicValues)&&basicNames.size() == basicValues.size()){
            for (int i = 0; i < basicNames.size(); i++) {
                String basicName = basicNames.get(i);
                String basicValue = basicValues.get(i);
                log.info("baidubaikeCitiao-->basicName={},basicValue={}", basicName,basicValues.get(i));
                basicInfos.put(cleanBaike(basicName),cleanBaike(basicValue));
            }
        }

        if(!PatConstant.TRUE.equals(poetBaike.getBaikeCheck())){//跳过已核验的
            if(PoetRelConstant.REL_TYPE_INFO.equals(poetBaike.getRelType())){
                Map<String, String> titleAndAuthor = poetInfoMapper.getTitleAndAuthorById(poetBaike.getRelId());
                String author = titleAndAuthor.get("author");
                //概述及信息栏都没有author 需要人工介入
                if(!poetBaike.getBaikeDesc().contains(author)){
                    if(!basicInfos.values().contains(author)){
                        poetBaike.setBaikeCheck(PatConstant.FALSE);
                    }
                }
            }
        }
        poetBaike.setStatus(BaikeConstant.STATUS_BAIKE_OK);
        poetBaike.setUpdateTs(new Date());
        poetBaikeMapper.updateTemplateById(poetBaike);
        if(PoetRelConstant.REL_TYPE_INFO.equals(poetBaike.getRelType())){
                PoetInfo poetInfo = poetInfoMapper.single(poetBaike.getRelId());
                poetInfo.setEsStatus(PatConstant.INIT);
                poetInfo.setEsCheck(PatConstant.INIT);
                poetInfoMapper.updateTemplateById(poetInfo);//待推送es

        }
        //处理信息栏数据
        Long baikeId = poetBaike.getId();
        Long relId = poetBaike.getRelId();
        String relType = poetBaike.getRelType();
        if(!CollectionUtils.isEmpty(basicInfos)){
            List<PoetProperty> poetProperties = poetPropertyMapper.createLambdaQuery().andEq(PoetProperty::getBaikeId, baikeId).selectSimple();
            int index = 0;
            for (Map.Entry<String, String> entry : basicInfos.entrySet()) {
                String basicName = entry.getKey();
                String basicValue = entry.getValue();
                PoetProperty poetProperty = null;
                if(index < poetProperties.size()){
                    poetProperty = poetProperties.get(index);
                }else{
                    poetProperty = new PoetProperty();
                }
                poetProperty.setRelId(relId);
                poetProperty.setRelType(relType);
                poetProperty.setKey(basicName);
                poetProperty.setValue(basicValue);
                poetProperty.setRelId(relId);
                poetProperty.setRelType(relType);
                poetProperty.setBaikeId(baikeId);
                poetProperty.setIndex(index);
                poetProperty.setStatus(PatConstant.TRUE);
                if(poetProperty.getId() == null){
                    poetPropertyMapper.insert(poetProperty);
                }else{
                    poetPropertyMapper.updateById(poetProperty);
                }
                index++;
            }

            for (int i = basicInfos.size(); i < poetProperties.size(); i++) {// 旧的数据多的置为无效
                PoetProperty poetProperty = poetProperties.get(i);
                poetProperty.setStatus(PatConstant.FAIL);
                poetPropertyMapper.updateById(poetProperty);
            }
        }


    }

    private void dealBaikeDesc(List<String> lemmaSummarys, PoetBaike poetBaike) {
        if(CollectionUtils.isEmpty(lemmaSummarys)){
            throw new RuntimeException("百科描述不存在");
        }
        int paraIndex = 0;
        StringBuffer summarySbf = new StringBuffer();
        StringBuffer paraSbf = new StringBuffer();
        for (String lemmaSummary : lemmaSummarys) {
            lemmaSummary = cleanBaike(lemmaSummary);
            summarySbf.append(lemmaSummary);
            paraIndex+=lemmaSummary.length();
            paraSbf.append(paraIndex).append(PoetCharConstant.CHAR_COMMA);
        }
        paraSbf.deleteCharAt(paraSbf.length()-1);
        poetBaike.setBaikeDesc(summarySbf.toString());
        poetBaike.setBaikeDescParas(paraSbf.toString());

    }

    /**
     * 搜索百科并入库
     *
     * 当发现搜索key 和搜索结果不一致时，需要人工介入， 把searchBaikeTitle改为目标百科词  手工加上对应的词条 baikeUrl  ,并且修改相似度为2 ，改status 为 1。  当整合后台时到时加下这个操作。
     *
     * @param infoId
     * @return
     * @throws Exception
     */
    private PoetBaike searchBaike(Long infoId) throws Exception {
        PoetBaike poetBaike = poetBaikeMapper.createLambdaQuery().andEq(PoetBaike::getRelType, PoetRelConstant.REL_TYPE_INFO).andEq(PoetBaike::getRelId, infoId).singleSimple();
        if(poetBaike == null){
            poetBaike = new PoetBaike();
        }else{
            if(!BaikeConstant.STATUS_SIMILAR_IN_VALID.equals(poetBaike.getStatus())){
                return poetBaike;
            }
        }
        if(!StringUtils.hasText(poetBaike.getBaikeSearchKey())){
            Map<String, String> titleAndAuthor = poetInfoMapper.getTitleAndAuthorById(infoId);
            String title = titleAndAuthor.get("title");
            String author = titleAndAuthor.get("author");
            String searchKey = String.format("%s(%s) - 百度百科",title,author);
            poetBaike.setBaikeSearchKey(searchKey);
        }
        String baikeSearchKey = poetBaike.getBaikeSearchKey();
        log.info("synchBaiduBaikeProps-->infoId={},baikeSearchKey={}", infoId,baikeSearchKey);

        String searchUrl = String.format("https://baike.baidu.com/search?word=%s&pn=0&rn=0&enc=utf8",baikeSearchKey);
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Html html = httpClientDownloader.download(searchUrl);
        List<String> resultTtiles = html.xpath("//a[@class='result-title']").all();
        String baikeSearchTitle = "";
        Double similarVal = -1d;
        String baikeUrl = null;
        if(!CollectionUtils.isEmpty(resultTtiles)){
            String baikeDomain = "baike.baidu.com";
            Map<String,String> baikeTitleAndUrls = new HashMap<String,String>();
            for (String resultTitle : resultTtiles) {
                Html tempH = new Html(resultTitle);
                String baikeUrlT = tempH.xpath("//a/@href").get();
                String baikeTitle = tempH.xpath("//a/allText()").get();
                baikeUrlT = DomainUtils.completeUrl(true,baikeDomain,baikeUrlT,true);
    //          log.info("baidubaikeSearchKey-->href={},title={}", baikeUrl,baikeTitle);
                baikeTitleAndUrls.put(baikeTitle,baikeUrlT);
            }
            Map.Entry<String, Double> maxSimilar = IKAnalyzerUtils.maxSimilar(baikeSearchKey, baikeTitleAndUrls.keySet());
            baikeSearchTitle = maxSimilar.getKey();
            similarVal = maxSimilar.getValue();
            baikeUrl = baikeTitleAndUrls.get(baikeSearchTitle);
            log.info("synchBaiduBaikeProps-->infoId={},baikeSearchTitle={}，similarVal={},baikeUrl={}", infoId,baikeSearchTitle,similarVal,baikeUrl);
        }else{
            log.info("searchBaike-->infoId={},baikeSearchKey={},未找到相关词条",infoId,baikeSearchKey);
        }

        poetBaike.setBaikeType(BaikeConstant.BAIKE_TYPE_BAIDU);
        poetBaike.setRelType(PoetRelConstant.REL_TYPE_INFO);
        poetBaike.setRelId(infoId);
        poetBaike.setBaikeSearchKey(baikeSearchKey);
        poetBaike.setBaikeSearchTitle(baikeSearchTitle);
        poetBaike.setBaikeUrl(baikeUrl);
        poetBaike.setSimilarVal(new BigDecimal(similarVal));
        poetBaike.setStatus(similarVal>VALID_MIN_SIMILAR?BaikeConstant.STATUS_SIMILAR_VALID:BaikeConstant.STATUS_SIMILAR_IN_VALID);
        poetBaike.setUpdateTs(new Date());
        if(poetBaike.getId()==null){
            poetBaikeMapper.insert(poetBaike);
        }else{
            poetBaikeMapper.updateById(poetBaike);
        }
        return poetBaike;
    }



    private String cleanBaike(String source){
        if(source == null){
            return "";
        }
        return source.replaceAll("\\[\\d+\\]", "").replaceAll("\\s+","");
    }
}
