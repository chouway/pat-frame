package com.pat.api.service.poet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.pat.api.bo.*;
import com.pat.api.service.PoetServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PoetEsSearchServiceTest
 *
 * @author chouway
 * @date 2022.03.03
 */
public class PoetEsSearchServiceTest extends PoetServiceTest {

    @Autowired
    private PoetEsSearchService poetEsSearchService;

    @Autowired
    private IPoetInfoService poetInfoService;

    /**
     * 2022-03-03 16:32:41.267  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->esSearchBO={"key":"关山"}
     * 2022-03-03 16:32:41.431  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->result={"took":7,"timed_out":false,"_shards":{"total":2,"successful":2,"skipped":0,"failed":0},"hits":{"total":{"value":1,"relation":"eq"},"max_score":6.1652126,"hits":[{"_index":"poet-info_v0","_type":"_doc","_id":"1","_score":6.1652126,"_source":{"_class":"com.pat.app.poetry.synch.eo.PoetInfoEO","id":1,"title":"度关山","content":"天地间，人为贵。立君牧民，为之轨则。车辙马迹，经纬四极。黜陟幽明，黎庶繁息。於铄贤圣，总统邦域。封建五爵，井田刑狱。有燔丹书，无普赦赎。皋陶甫侯，何有失职？嗟哉后世，改制易律。劳民为君，役赋其力。舜漆食器，畔者十国，不及唐尧，采椽不斫。世叹伯夷，欲以厉俗。侈恶之大，俭为共德。许由推让，岂有讼曲？兼爱尚同，疏者为戚。","author":"曹操","propKeys":["作品名称","创作年代","作者","作品出处","作品体裁"],"properties":{"作品名称":"度关山","创作年代":"东汉","作者":"曹操","作品出处":"《曹操集》","作品体裁":"四言诗"}}}]},"aggregations":{"num_perKey":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"作品体裁","doc_count":1},{"key":"作品出处","doc_count":1},{"key":"作品名称","doc_count":1},{"key":"作者","doc_count":1},{"key":"创作年代","doc_count":1}]}}}
     * 2022-03-03 16:32:41.469  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggs-->aggsPropKeys=[作品体裁, 作品出处, 作品名称, 作者, 创作年代]
     * 2022-03-03 16:32:41.493  INFO 2780 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggs-->result={"took":8,"timed_out":false,"_shards":{"total":2,"successful":2,"skipped":0,"failed":0},"hits":{"total":{"value":1,"relation":"eq"},"max_score":null,"hits":[]},"aggregations":{"vals_perkey_0":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"四言诗","doc_count":1}]},"vals_perkey_1":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"《曹操集》","doc_count":1}]},"vals_perkey_2":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"度关山","doc_count":1}]},"vals_perkey_3":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"曹操","doc_count":1}]},"vals_perkey_4":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"东汉","doc_count":1}]}}}
     */
    @Test
    public void search(){
        EsSearchBO esSearchBO = new EsSearchBO();
        esSearchBO.setKey("关山");
//      esSearchBO.setKey("a");
        log.info("search-->esSearchBO={}", JSON.toJSONString(esSearchBO));
        String result = poetEsSearchService.search(esSearchBO);
        log.info("search-->result={}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray aggPropKeys = (JSONArray)JSONPath.eval(jsonObject, "$.aggregations.num_perKey.buckets.key");
        if(aggPropKeys!=null&&aggPropKeys.size()>0){
            List<String> aggsPropKeys = new ArrayList<String>();
            for (Object aggPropKey : aggPropKeys) {
                aggsPropKeys.add(aggPropKey.toString());
            }

            result = poetEsSearchService.aggs(esSearchBO);
            log.info("aggs-->result={}", result);
        }
    }

    private EsSearchBO getSearchBO(){
        EsSearchBO esSearchBO = new EsSearchBO();
        esSearchBO.setHighlight(true);
        esSearchBO.setKey("关山");
        return esSearchBO;
    }

    @Test
    public void searchBO(){
        EsSearchBO esSearchBO = getSearchBO();
        log.info("search-->esSearchBO={}", JSON.toJSONString(esSearchBO));
        PoetSearchResultBO result = poetEsSearchService.searchBO(esSearchBO);
        log.info("searchBO-->result={}", JSON.toJSONString(result));
    }


    @Test
    public void suggest(){
        String keyword = "人";
        log.info("suggest-->keyword={}", keyword);
        EsSuggestBO esSuggestBO = new EsSuggestBO();
        esSuggestBO.setKeyword(keyword);
        List<EsSuggestBO> suggests = poetEsSearchService.suggest(esSuggestBO);
        log.info("suggest-->suggests={}", JSON.toJSONString(suggests));
    }

    /**
     * 未筛选
     * 2022-03-24 09:37:44.195  INFO 84248 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->esSearchBO={"highlight":true,"key":"关山"}
     * 2022-03-24 09:37:45.185  INFO 84248 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : search-->searchResult={"poetInfoBOs":[{"author":"曹操","baikeId":1,"id":1,"paragraphs":["天地间，人为贵。","立君牧民，为之轨则。","车辙马迹，经纬四极。","黜陟幽明，黎庶繁息。","於铄贤圣，总统邦域。","封建五爵，井田刑狱。","有燔丹书，无普赦赎。","皋陶甫侯，何有失职？","嗟哉后世，改制易律。","劳民为君，役赋其力。","舜漆食器，畔者十国，","不及唐尧，采椽不斫。","世叹伯夷，欲以厉俗。","侈恶之大，俭为共德。","许由推让，岂有讼曲？","兼爱尚同，疏者为戚。"],"title":"度<em>关</em><em>山</em>"},{"author":"曹操","baikeId":23,"id":17,"paragraphs":["晨上散<em>关</em><em>山</em>，此道当何难！","晨上散<em>关</em><em>山</em>，此道当何难！","牛顿不起，车堕谷间。","坐磐石之上，弹五弦之琴。","作为清角韵，意中迷烦。","歌以言志，晨上散<em>关</em><em>山</em>。","有何三老公，卒来在我旁？","有何三老公，卒来在我旁？","负揜被裘，似非恒人。","谓卿云何困苦以自怨，徨徨所欲，来到此间？","歌以言志，有何三老公？","我居昆仑<em>山</em>，所谓者真人。","我居昆仑<em>山</em>，所谓者真人。","道深有可得。","名山历观，遨游八极，枕石漱流饮泉。","沈吟不决，遂上升天。","歌以言志，我居昆仑<em>山</em>。","去去不可追，长恨相牵攀。","去去不可追，长恨相牵攀。","夜夜安得寐，惆怅以自怜。","正而不谲，辞赋依因。","经传所过，西来所传。","歌以言志，去去不可追。"],"title":"秋胡行 其一"},{"author":"曹操","baikeId":24,"id":18,"paragraphs":["愿登泰华<em>山</em>，神人共远游。","愿登泰华<em>山</em>，神人共远游。","经历昆仑<em>山</em>，到蓬莱。","飘遥八极，与神人俱。","思得神药，万岁为期。","歌以言志，愿登泰华<em>山</em>。","天地何长久！人道居之短。","天地何长久！人道居之短。","世言伯阳，殊不知老；","赤松王乔，亦云得道。","得之未闻，庶以寿考。","歌以言志，天地何长久！","明明日月光，何所不光昭！","明明日月光，何所不光昭！","二仪合圣化，贵者独人不？","万国率土，莫非王臣。","仁义为名，礼乐为荣。","歌以言志，明明日月<em>关</em>。","四时更逝去，昼夜以成岁。","四时更逝去，昼夜以成岁。","大人先天而天弗违。","不戚年往，忧世不治。","存亡有命，虑之为蚩。","歌以言志，四时更逝去。","戚戚欲何念！欢笑意所之。","戚戚欲何念！欢笑意所之。","壮盛智愚，殊不再来。","爱时进趣，将以惠谁？","泛泛放逸，亦同何为！","歌以言志，戚戚欲何念！"],"title":"秋胡行 其二"},{"author":"曹操","baikeId":13,"id":7,"paragraphs":["<em>关</em>东有义士，兴兵讨群凶。","初期会盟津，乃心在咸阳。","军合力不齐，踌躇而雁行。","势利使人争，嗣还自相戕。","淮南弟称号，刻玺於北方。","铠甲生虮虱，万姓以死亡。","白骨露於野，千里无鸡鸣。","生民百遗一，念之断人肠。"],"title":"蒿里行"},{"author":"曹操","baikeId":18,"id":12,"paragraphs":["游君<em>山</em>，甚为真。","崔嵬砟硌，尔自为神。","乃到王母台，金阶玉为堂，芝草生殿旁。","东西厢，客满堂。","主人当行觞，坐者长寿遽何央。","长乐甫始宜孙子。","常愿主人增年，与天相守。"],"title":"气出唱 其三"},{"author":"曹操","baikeId":17,"id":11,"paragraphs":["驾六龙，乘风而行。","行四海，路下之八邦。","历登高<em>山</em>临溪谷，乘云而行。","行四海外，东到泰<em>山</em>。","仙人玉女，下来翱游。","骖驾六龙饮玉浆。","河水尽，不东流。","解愁腹，饮玉浆。","奉持行，东到蓬莱<em>山</em>，上至天之门。","玉阙下，引见得入，","赤松相对，四面顾望，视正焜煌。","开玉心正兴，其气百道至。","传告无穷闭其口，但当爱气寿万年。","东到海，与天连。","神仙之道，出窈入冥，常当专之。","心恬澹，无所愒。","欲闭门坐自守，天与期气。","愿得神之人，乘驾云车，","骖驾白鹿，上到天之门，来赐神之药。","跪受之，敬神齐。","当如此，道自来。"],"title":"气出唱 其二"},{"author":"曹操","baikeId":25,"id":19,"paragraphs":["古公亶甫，积德垂仁。","思弘一道，哲王于豳。","太伯仲雍，王德之仁。","行施百世，断发文身。","伯夷叔齐，古之遗贤。","让国不用，饿殂首<em>山</em>。","智哉<em>山</em>甫，相彼宣王。","何用杜伯，累我圣贤。","齐桓之霸，赖得仲父。","后任竖刁，虫流出户。","晏子平仲，积德兼仁。","与世沈德，未必思命。","仲尼之世，主国为君。","随制饮酒，扬波使官。"],"title":"善哉行 其一"},{"author":"曹操","baikeId":15,"id":9,"paragraphs":["北上太行<em>山</em>，艰哉何巍巍！","羊肠坂诘屈，车轮为之摧。","树木何萧瑟！北风声正悲。","熊罴对我蹲，虎豹夹路啼。","溪谷少人民，雪落何霏霏！","延颈长叹息，远行多所怀。","我心何怫郁？思欲一东归。","水深桥梁绝，中路正徘徊。","迷惑失故路，薄暮无宿栖。","行行日已远，人马同时饥。","担囊行取薪，斧冰持作糜。","悲彼东<em>山</em>诗，悠悠使我哀。"],"title":"苦寒行"},{"author":"曹操","baikeId":16,"id":10,"paragraphs":["华阴<em>山</em>，自以为大。","高百丈，浮云为之盖。","仙人欲来，出随风，列之雨。","吹我洞箫，鼓瑟琴，何訚訚！","酒与歌戏，今日相乐诚为乐。","玉女起，起舞移数时。","鼓吹一何嘈嘈。","从西北来时，仙道多驾烟，","乘云驾龙，郁何务务。","遨游八极，乃到昆仑之<em>山</em>，","西王母侧，神仙金止玉亭。","来者为谁？赤松王乔，乃德旋之门。","乐共饮食到黄昏。","多驾合坐，万岁长，宜子孙。"],"title":"气出唱 其一"},{"author":"曹操","baikeId":11,"id":5,"paragraphs":["东临碣石，以观沧海。","水何澹澹，<em>山</em>岛竦峙。","树木丛生，百草丰茂。","秋风萧瑟，洪波涌起。","日月之行，若出其中；","星汉灿烂，若出其里。","幸甚至哉，歌以咏志。"],"title":"观沧海"}],"propKeys":["文集","作者","作品名称","创作年代","作品出处","文学体裁","作品体裁","中文名","作品别名","朝代"],"total":12}
     * 2022-03-24 09:37:45.186  INFO 84248 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggsBO-->esSearchBO={"aggsPropKeys":["文集","作者","作品名称","创作年代","作品出处","文学体裁","作品体裁","中文名","作品别名","朝代"],"from":0,"highlight":true,"key":"关山","pageNum":1,"size":10}
     * 2022-03-24 09:37:45.211  INFO 84248 --- [           main] c.p.a.s.poet.PoetEsSearchServiceTest     : aggsBO-->aggsResult=[{"choosePreSize":0,"key":"文集","vals":["曹操诗集"]},{"choosePreSize":0,"key":"作者","vals":["曹操"]},{"choosePreSize":0,"key":"作品名称","vals":["气出唱","善哉行","度关山","短歌行二首","秋胡行其一","蒿里行","观沧海","陌上桑"]},{"choosePreSize":0,"key":"创作年代","vals":["东汉","魏晋","三国时期","东汉末年"]},{"choosePreSize":0,"key":"作品出处","vals":["《曹操集》","乐府诗集","善哉行其一"]},{"choosePreSize":0,"key":"文学体裁","vals":["诗歌；乐府诗","五言古诗","四言体","四言诗","词"]},{"choosePreSize":0,"key":"作品体裁","vals":["四言诗","乐府诗"]},{"choosePreSize":0,"key":"中文名","vals":["秋胡行其二"]},{"choosePreSize":0,"key":"作品别名","vals":["短歌行"]},{"choosePreSize":0,"key":"朝代","vals":["魏晋"]}]
     */
    @Test
    public void aggsBO(){
        EsSearchBO esSearchBO = getSearchBO();
        log.info("aggsBO-->esSearchBO={}", JSON.toJSONString(esSearchBO));
        List<PoetAggsBO> aggsResult = poetEsSearchService.aggsBO(esSearchBO);
        log.info("aggsBO-->aggsResult={}", JSON.toJSONString(aggsResult));

    }

}