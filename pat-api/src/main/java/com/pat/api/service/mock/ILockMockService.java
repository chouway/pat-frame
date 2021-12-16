package com.pat.api.service.mock;

/**
 * ILockMockService
 * 分布式锁模拟接口
 * @author zhouyw
 * @date 2021.03.21
 */
public interface ILockMockService {

    String process(String url);
}
