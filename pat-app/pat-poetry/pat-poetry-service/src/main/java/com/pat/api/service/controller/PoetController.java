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
        ResultBO<PoetSearchResultBO> resultBO = new ResultBO<PoetSearchResultBO>();
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
        log.debug("search-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }


    @RequestMapping(value = "/suggest")
    @ResponseBody
    public ResultBO<List<EsSuggestBO>> suggest(@RequestBody EsSuggestBO esSuggestBO) {
        ResultBO<List<EsSuggestBO>> resultBO = new ResultBO<List<EsSuggestBO>>();
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
        log.debug("suggest-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }

    @RequestMapping(value = "/baike")
    @ResponseBody
    public ResultBO<PoetBaikeBO> getBaikeById(Long infoId) {
        ResultBO<PoetBaikeBO> resultBO = new ResultBO<PoetBaikeBO>();
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
        log.debug("getBaikeById-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }

    @RequestMapping(value = "/aggs")
    @ResponseBody
    public ResultBO<List<PoetAggsBO>> aggs(@RequestBody EsSearchBO esSearchBO) {
        ResultBO<List<PoetAggsBO>> resultBO = new ResultBO<List<PoetAggsBO>>();
        try {
            resultBO.setInfo(poetEsSearchService.aggsBO(esSearchBO));
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
        log.debug("aggs-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }


    @RequestMapping(value = "/getAggsKeys")
    @ResponseBody
    public ResultBO<List<PoetAggsKeyBO>> getAggsKeys(@RequestBody EsSearchBO esSearchBO) {
        ResultBO<List<PoetAggsKeyBO>> resultBO = new ResultBO<List<PoetAggsKeyBO>>();
        try {
            resultBO.setInfo(poetEsSearchService.getAggsKeys(esSearchBO));
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
        log.debug("getAggsKeys-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }

    @RequestMapping(value = "/getAggsKeyVals")
    @ResponseBody
    public ResultBO<List<PoetAggsValBO>> getAggsKeyVals(@RequestBody EsSearchBO esSearchBO) {
        ResultBO<List<PoetAggsValBO>> resultBO = new ResultBO<List<PoetAggsValBO>>();
        try {
            resultBO.setInfo(poetEsSearchService.getAggsKeyVals(esSearchBO));
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
        log.debug("getAggsKeyVals-->resultBO:code="+ resultBO.getCode());
        return resultBO;
    }

}
