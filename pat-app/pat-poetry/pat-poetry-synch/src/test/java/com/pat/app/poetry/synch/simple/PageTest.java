package com.pat.app.poetry.synch.simple;

import com.pat.app.poetry.synch.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * PageTest
 *
 * @author chouway
 * @date 2022.03.03
 */
@Slf4j
public class PageTest {
    /**
     * 00:30:27.794 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->resultTtiles=[<a class="result-title" href="https://baike.baidu.com/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/2356313" target="_blank"><em>度关山(曹操</em>诗作) - <em>百度百科</em></a>, <a class="result-title" href="/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22084627" target="_blank"><em>度关山(</em>柳恽诗作) - <em>百度百科</em></a>, <a class="result-title" href="/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22546029" target="_blank"><em>度关山(</em>王训诗作) - <em>百度百科</em></a>, <a class="result-title" href="/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22025160" target="_blank"><em>度关山(</em>萧纲诗作) - <em>百度百科</em></a>, <a class="result-title" href="/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22104194" target="_blank"><em>度关山(</em>戴暠诗作) - <em>百度百科</em></a>, <a class="result-title" href="https://baike.baidu.com/item/%E6%9B%B9%E6%93%8D/2364108" target="_blank"><em>曹操(</em>游戏《英雄杀》中的武将牌) - <em>百度百科</em></a>]
     * 00:30:27.828 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=https://baike.baidu.com/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/2356313,title=度关山(曹操诗作) - 百度百科
     * 00:30:27.830 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22084627,title=度关山(柳恽诗作) - 百度百科
     * 00:30:27.832 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22546029,title=度关山(王训诗作) - 百度百科
     * 00:30:27.838 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22025160,title=度关山(萧纲诗作) - 百度百科
     * 00:30:27.846 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=/item/%E5%BA%A6%E5%85%B3%E5%B1%B1/22104194,title=度关山(戴暠诗作) - 百度百科
     * 00:30:27.847 [main] INFO com.pat.app.poetry.synch.simple.PageTest - baidubaikeSearchKey-->href=https://baike.baidu.com/item/%E6%9B%B9%E6%93%8D/2364108,title=曹操(游戏《英雄杀》中的武将牌) - 百度百科
     */
    @Test
    public void baidubaikeSearchKey(){
        String url = "https://baike.baidu.com/search?word=度关山(曹操) - 百度百科&pn=0&rn=0&enc=utf8";
        String rawText = FileUtils.getContent("classpath:download/baidubaike_searchKey.txt");
        Page page = getSimplePage(url, rawText);
        Html html = page.getHtml();
        List<String> resultTtiles = html.xpath("//a[@class='result-title']").all();
        log.info("baidubaikeSearchKey-->resultTtiles={}", resultTtiles);
        for (String resultTitle : resultTtiles) {
            Html tempH = new Html(resultTitle);
            String href = tempH.xpath("//a/@href").get();
            String title = tempH.xpath("//a/allText()").get();
            log.info("baidubaikeSearchKey-->href={},title={}", href,title);
        }


    }

    private Page getSimplePage(String url, String rawText) {
        Page page = new Page();
        Request request = new Request(url);
        page.setRequest(request);
        page.setRawText(rawText);
        return page;
    }
}
