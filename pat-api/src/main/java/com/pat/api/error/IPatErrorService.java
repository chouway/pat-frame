package com.pat.api.error;

import com.pat.api.entity.PatError;
import com.pat.api.exception.BusinessException;

/**
 * IPatErrorService
 *
 * @author chouway
 * @date 2021.05.12
 */
public interface IPatErrorService {

    /**
     * 获取异常信息
     * @param id
     * @return
     * @throws BusinessException
     */
    public PatError getById(Long id)throws BusinessException;

    /**
     * 保存异常信息
     * @param aibkError
     * @return
     * @throws BusinessException
     */
    public Long add(PatError aibkError)throws BusinessException;

    /**
     * 处理异常
     * @return
     */
    public void handle(PatError aibkError)throws BusinessException;
}
