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
