package com.pat.starter.common.bo;

import java.io.Serializable;
import java.util.Map;

/**
 * HttpPubParam
 *
 * @author chouway
 * @date 2020.08.13
 */
public class RequestBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;

    private String params;

    private String chartSet;

    private String method;

    private Map<String, String> headers;
    /**
     * 是否自动跳转
     */
    private boolean followRedirects = true;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getChartSet() {
        return chartSet;
    }

    public void setChartSet(String chartSet) {
        this.chartSet = chartSet;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
