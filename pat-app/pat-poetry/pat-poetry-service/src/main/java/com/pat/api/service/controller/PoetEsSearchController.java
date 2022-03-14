package com.pat.api.service.controller;

import com.pat.api.bo.CodeEnum;
import com.pat.api.bo.EsSuggestBO;
import com.pat.api.bo.ResultBO;
import com.pat.api.service.poet.PoetEsSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * PoetEsSearchController
 *
 * @author chouway
 * @date 2022.03.14
 */
@Slf4j
@Controller
@RequestMapping(value = "/poet/es")
public class PoetEsSearchController {

    @Autowired
    private PoetEsSearchService poetEsSearchService;

    @RequestMapping(value = "/suggest")
    @ResponseBody
    public ResultBO<List<EsSuggestBO>> suggest(@RequestBody EsSuggestBO esSuggestBO) {
        ResultBO resultBO = new ResultBO();
        try {
            resultBO.setInfo(poetEsSearchService.suggest(esSuggestBO));
            resultBO.setSuccess(true);
            resultBO.setCode(CodeEnum.SUCCESS.getCode());
            resultBO.setMessage(CodeEnum.SUCCESS.getMessage());
        } catch(RuntimeException e){
            log.error("busi error", e);
            resultBO.setCode(CodeEnum.QUERY_ERROR.getCode());
            resultBO.setMessage(CodeEnum.QUERY_ERROR.getMessage());
        } catch (Exception e) {
            log.error("失败", e);
            resultBO.setCode(CodeEnum.QUERY_ERROR.getCode());
            resultBO.setMessage(CodeEnum.QUERY_ERROR.getMessage());
        }
        log.debug("suggest-->resultBO：code="+ resultBO.getCode());
        return resultBO;
    }

}
