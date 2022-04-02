package com.pat.starter.oauth.common.util;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pat.api.bo.ResultBO;
import com.pat.api.constant.ErrorCode;
import com.pat.api.constant.PatConstant;
import com.pat.api.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HttpRespUtil
 *
 * @author chouway
 * @date 2022.04.01
 */
@Slf4j
public class HttpRespUtil {

    private HttpRespUtil() {

    }

    public static void writerError(ErrorCode errorCode, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json,charset=utf-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.writeValue(outputStream, ErrorCode.errorBO(errorCode));
        } catch (Exception e) {
            log.error("error:-->[[errorCode]]={}", JSON.toJSONString(new Object[]{errorCode}), e);
            throw new BusinessException("写出失败");
        } finally {
            IoUtil.close(outputStream);
        }
    }

    public static void writerOK(Object info, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ResultBO resultBO = new ResultBO();
            resultBO.setCode(PatConstant.CODE_SUCCESS);
            resultBO.setSuccess(true);
            resultBO.setInfo(info);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json,charset=utf-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.writeValue(response.getOutputStream(), resultBO);
        } catch (Exception e) {
            log.error("error:-->[[info]]={}", JSON.toJSONString(new Object[]{info}), e);
            throw new BusinessException("写出失败");
        } finally {
            IoUtil.close(outputStream);
        }
    }
}
