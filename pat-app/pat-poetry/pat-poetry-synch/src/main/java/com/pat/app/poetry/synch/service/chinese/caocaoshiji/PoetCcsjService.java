package com.pat.app.poetry.synch.service.chinese.caocaoshiji;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pat.api.constant.PoetSetConstant;
import com.pat.api.entity.PoetAuthor;
import com.pat.api.entity.PoetContent;
import com.pat.api.entity.PoetInfo;
import com.pat.api.entity.PoetSet;
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
 * PoetCcsjService
 *
 * @author chouway
 * @date 2022.02.28
 */
@Slf4j
@Service
public class PoetCcsjService extends PoetAbstractService {

    @Override
    protected PoetSet initPoetSet() {
        PoetSet initPoetSet = new PoetSet();
        initPoetSet.setNameCn("曹操诗集");
        initPoetSet.setNameEn("caocaoshiji");
        initPoetSet.setDescribe("曹操喜欢用诗歌、散文来抒发自己政治抱负，反映民生疾苦，是魏晋文学的代表人物，其文学成就，主要表当今诗歌上，散文也很有特点。曹操的诗歌，今存20多篇，全部是乐府诗体。内容大体上可分三类。一类是关涉时事的，一类是以表述理想为主的，一类是游仙诗。");
        initPoetSet.setRemark("共收录曹操作诗词26首，部分诗词中标注了全网版本的疑字。 收录语言为简体中文。");
        List<PoetSetInfoBO> infos = new ArrayList<PoetSetInfoBO>();
        PoetSetInfoBO poetSetInfo = new PoetSetInfoBO();
        poetSetInfo.setFile("caocao.json");
        poetSetInfo.setDealNum(0);
        infos.add(poetSetInfo);
        initPoetSet.setInfos(JSON.toJSONString(infos));
        initPoetSet.setSetType(PoetSetConstant.SET_TYPE_CHINESE_CLASSIC);
        return initPoetSet;
    }

    /**
     * ``` json
     * {
     *     "title": "度关山",
     *     "paragraphs": [
     *         "天地间，人为贵。",
     *         "立君牧民，为之轨则。",
     *         "车辙马迹，经纬四极。",
     *         "黜陟幽明，黎庶繁息。",
     *         "於铄贤圣，总统邦域。",
     *         "封建五爵，井田刑狱。",
     *         "有燔丹书，无普赦赎。",
     *         "皋陶甫侯，何有失职？",
     *         "嗟哉后世，改制易律。",
     *         "劳民为君，役赋其力。",
     *         "舜漆食器，畔者十国，",
     *         "不及唐尧，采椽不斫。",
     *         "世叹伯夷，欲以厉俗。",
     *         "侈恶之大，俭为共德。",
     *         "许由推让，岂有讼曲？",
     *         "兼爱尚同，疏者为戚。"
     *     ]
     * }
     * ```
     * @param jsonObject
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PoetInfo synchInfo(JSONObject jsonObject,int index) {
        String author = "曹操";
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
        poetInfoDB  = poetInfo;
        poetInfoDB.setAuthorId(poetAuthor.getId());
        poetInfoDB.setCount(0L);
        poetInfoDB.setVersion(0L);
        poetInfoDB.setIndex(index);
        poetInfoDB.setUpdateTs(new Date());
        poetInfoMapper.insert(poetInfoDB);
        log.info("synchInfo-->poetInfo={}", JSON.toJSONString(poetInfo));

        JSONArray paragraphs = jsonObject.getJSONArray("paragraphs");
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
