package com.pat.api.service.controller;

import com.pat.api.bo.*;
import com.pat.api.service.poet.IPoetEsSearchService;
import com.pat.api.service.poet.IPoetInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * PoetController
 *
 * @author chouway
 * @date 2022.03.14
 */
@Slf4j
@Controller
@RequestMapping(value = "/poet")
public class PoetController {

    @Autowired
    private IPoetEsSearchService poetEsSearchService;

    @Autowired
    private IPoetInfoService poetInfoService;

    @RequestMapping(value = "/search")
    @ResponseBody
    public ResultBO<PoetSearchResultBO> search(@RequestBody EsSearchBO esSearchBO) {
        ResultBO resultBO = new ResultBO();
        try {
            resultBO.setInfo(poetEsSearchService.searchBO(esSearchBO));
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
        log.debug("search-->resultBO：code="+ resultBO.getCode());
        return resultBO;
    }


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

    @RequestMapping(value = "/getBaikeById")
    @ResponseBody
    public ResultBO<List<EsSuggestBO>> getBaikeById(@RequestBody Long infoId) {
        ResultBO resultBO = new ResultBO();
        try {
            resultBO.setInfo(poetInfoService.getBaikeById(infoId));
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
        log.debug("getBaikeById-->resultBO：code="+ resultBO.getCode());
        return resultBO;
    }

}
