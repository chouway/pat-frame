package com.pat.app.poetry.synch.simple;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HttpClientDownloaderTest
 *
 * @author chouway
 * @date 2021.06.17
 */
@Slf4j
public class HttpClientDownloaderTest {

    @Test
    public void downloadBaikeQuery(){
        String searchKey = "度关山(曹操) - 百度百科";
        String searchUrl = String.format("https://baike.baidu.com/search?word=%s&pn=0&rn=0&enc=utf8",searchKey);
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Html html = httpClientDownloader.download(searchUrl);
        log.info("downloadBaikeQuery-->searchUrl={},html={}", searchUrl,html);

    }

    @Test
    public void downloadBiakeCiTiao(){
        String citiaoUrl = "https://baike.baidu.com/item/度关山/2356313";
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Html html = httpClientDownloader.download(citiaoUrl);
        log.info("downloadBiakeCiTiao-->citiaoUrl={},html={}", citiaoUrl,html);

    }


    @Test
    public void down(){
        String url = "http://www.51menmen.com/sitemap.xml";
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Html html = httpClientDownloader.download(url);
//      log.info("down-->html={}", html);
        List<String> locs = html.xpath("//sitemap/loc/text()").all();
        String regex = "http://www.51menmen.com/sitemap(?<yearMonthDay>\\d{8})/(?<number>\\d+).xml";
        Pattern pattern = Pattern.compile(regex);
        for (String loc : locs) {
            Matcher matcher = pattern.matcher(loc);
            if(matcher.matches()){
                String yearMonthDay = matcher.group("yearMonthDay");
                String number = matcher.group("number");
                log.info("down-->yearMonthDay={},number={}", yearMonthDay,number);
            }

        }
    }
}
