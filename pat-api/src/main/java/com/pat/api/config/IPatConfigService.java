package com.pat.api.config;

import com.pat.api.entity.PatConfig;
import com.pat.api.exception.BusinessException;

import java.util.List;

/**
 * IPatConfigService
 *
 * configCode configValue parentId 组成唯一键
 *
 * @author chouway
 * @date 2021.03.26
 */
public interface IPatConfigService {
    /**
     * 获取配置项
     * @param typeCode
     * @return
     * @throws BusinessException
     */
    PatConfig uniqueByTypeCode(String typeCode)throws BusinessException;

    /**
     * 获取唯一配置子项
     * @param typeCode
     * @param configCode
     * @return
     * @throws BusinessException
     */
    PatConfig uniqueByTypeAndCode(String typeCode,String configCode)throws BusinessException;

    /**
     * 获取配置项
     * @param id
     * @return
     * @throws BusinessException
     */
    PatConfig uniqueById(Long id)throws BusinessException;

    /**
     * 获取子配置项列
     * @param typeCode
     * @return
     */
    List<PatConfig> getSonsByTypeCode(String typeCode)throws BusinessException;


    /**
     * 获取子配置项列
     * @param typeCode
     * @return
     */
    List<PatConfig> getSonsById(Long id)throws BusinessException;



    /**
     * 获取整个配置树
     * @param typeCode
     * @return
     * @throws BusinessException
     */
    List<PatConfig> getTreeByTypeCode(String typeCode)throws BusinessException;


    /**
     * 获取整个树配置
     * @param id
     * @return
     * @throws BusinessException
     */
    List<PatConfig> getTreeById(Long id)throws BusinessException;
}
