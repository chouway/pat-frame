package com.pat.starter.common.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * HttpPubResponse
 *
 * @author chouway
 * @date 2020.08.13
 */
public class ResponseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int responseCode = -1;


    private String responseMessage = null;

    private Map<String, List<String>> headers;

    private String body;

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
