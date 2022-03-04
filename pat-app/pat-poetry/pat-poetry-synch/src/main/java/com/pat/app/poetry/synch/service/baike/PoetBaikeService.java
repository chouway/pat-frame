package com.pat.app.poetry.synch.service.baike;

import com.alibaba.fastjson.JSON;
import com.pat.api.constant.BaikeConstant;
import com.pat.api.constant.PoetRelConstant;
import com.pat.api.entity.PoetBaike;
import com.pat.api.exception.BusinessException;
import com.pat.api.mapper.PoetBaikeMapper;
import com.pat.api.mapper.PoetInfoMapper;
import com.pat.app.poetry.synch.util.DomainUtils;
import com.pat.app.poetry.synch.util.IKAnalyzerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PoetBaikeService
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
@Service
@Transactional
public class PoetBaikeService {

    @Autowired
    private PoetInfoMapper poetInfoMapper;

    @Autowired
    private PoetBaikeMapper poetBaikeMapper;

    private final double VALID_MIN_SIMILAR = 0.8d;
    /**
     * 为id添加百科相关的属性
     * @param infoId
     */
    public void synchBaiduBaikeProps(Long infoId){
        try{

            PoetBaike poetBaike = searchBaike(infoId);
            log.info("synchBaiduBaikeProps-->infoId={},baikeSearchTitle={},status={},baikeUrl={}", infoId,poetBaike.getBaikeSearchTitle(),poetBaike.getStatus(),poetBaike.getBaikeUrl());
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
     * 解析百科词条
     *
     * 获取词条标题 简要描述 及 信息栏
     * @param poetBaike
     */
    private void resolveBaike(PoetBaike poetBaike) {
        String baikeUrl = poetBaike.getBaikeUrl();
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
        String maxBaikeTtitle = "";
        Double maxSimilarValue = -1d;
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
            maxBaikeTtitle = maxSimilar.getKey();
            maxSimilarValue = maxSimilar.getValue();
            baikeUrl = baikeTitleAndUrls.get(maxBaikeTtitle);
            log.info("synchBaiduBaikeProps-->infoId={},maxBaikeTtitle={}，maxSimilarValue={},baikeUrl={}", infoId,maxBaikeTtitle,maxSimilarValue,baikeUrl);
        }else{
            log.info("searchBaike-->infoId={},baikeSearchKey={},未找到相关词条",infoId,baikeSearchKey);
        }

        poetBaike.setBaikeType(BaikeConstant.BAIKE_TYPE_BAIDU);
        poetBaike.setRelType(PoetRelConstant.REL_TYPE_INFO);
        poetBaike.setRelId(infoId);
        poetBaike.setBaikeSearchKey(baikeSearchKey);
        poetBaike.setBaikeSearchTitle(maxBaikeTtitle);
        poetBaike.setBaikeUrl(baikeUrl);
        poetBaike.setMaxSimilar(new BigDecimal(maxSimilarValue));
        poetBaike.setStatus(maxSimilarValue>VALID_MIN_SIMILAR?BaikeConstant.STATUS_SIMILAR_VALID:BaikeConstant.STATUS_SIMILAR_IN_VALID);
        if(poetBaike.getId()==null){
            poetBaikeMapper.insert(poetBaike);
        }else{
            poetBaikeMapper.updateById(poetBaike);
        }
        return poetBaike;
    }

    ;
}
