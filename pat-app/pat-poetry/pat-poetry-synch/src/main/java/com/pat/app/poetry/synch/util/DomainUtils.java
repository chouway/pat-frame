package com.pat.app.poetry.synch.util;

import cn.hutool.core.net.URLDecoder;
import org.apache.http.protocol.HTTP;
import us.codecraft.webmagic.utils.UrlUtils;

import java.nio.charset.StandardCharsets;

/**
 * DomainUtils
 *
 * @author chouway
 * @date 2022.03.04
 */
public class DomainUtils {

    /**
     * 完整url处理
     *
     * @param isHttps
     * @param domain
     * @param url     可以是完整的url 或者 是部分url
     * @param isUrlDecode     是否decode
     * @return
     */
    public static String completeUrl(boolean isHttps, String domain, String url,boolean isUrlDecode) {
        String completUrl = completUrl(isHttps, domain, url);
        if(isUrlDecode){
            return URLDecoder.decode(completUrl, StandardCharsets.UTF_8);
        }
        return completUrl;
    }

    public static String completUrl(boolean isHttps, String domain, String url) {
        if (domain == null) {
            return url;
        }

        if (domain.equals(UrlUtils.getDomain(url))) {
            return url;
        }


        if (url.indexOf("//") == 0) {
            return getHttpProtocol(isHttps) + domain + url;
        } else {
            return getHttpProtocol(isHttps) + domain + "/" + url;
        }
    }

    public static String getHttpProtocol(boolean isHttps) {
        if (isHttps) {
            return "https://";
        }
        return "http://";
    }

}
