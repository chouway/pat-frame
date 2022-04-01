package com.pat.starter.oauth.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pat.api.bo.ResultBO;
import com.pat.api.constant.ErrorCode;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HttpRespUtil
 *
 * @author chouway
 * @date 2022.04.01
 */
public class HttpRespUtil {

    private HttpRespUtil(){

    }

    public static void writerError(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json,charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.writeValue(response.getOutputStream(), ErrorCode.errorBO(errorCode));
    }
}
